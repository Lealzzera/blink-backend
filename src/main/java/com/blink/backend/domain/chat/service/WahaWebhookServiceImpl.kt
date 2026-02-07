package com.blink.backend.domain.chat.service

import com.blink.backend.domain.chat.model.WhatsAppMessage
import com.blink.backend.domain.integration.n8n.N8nClient
import com.blink.backend.domain.integration.n8n.dto.MessageReceived
import com.blink.backend.domain.integration.waha.WahaPhoneResolverService
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.Patient
import com.blink.backend.persistence.entity.appointment.PatientEntity
import com.blink.backend.persistence.patient.repository.PatientRepositoryService
import com.blink.backend.persistence.repository.AppointmentsRepository
import com.blink.backend.persistence.repository.ClinicConfigurationRepository
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService
import com.blink.backend.persistence.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WahaWebhookServiceImpl(
    val userRepository: UserRepository,
    val simpMessagingTemplate: SimpMessagingTemplate,
    val clinicRepositoryService: ClinicRepositoryService,
    val clinicConfigurationRepository: ClinicConfigurationRepository,
    val wahaPhoneResolverService: WahaPhoneResolverService,
    val patientRepositoryService: PatientRepositoryService,
    val logger: Logger = LoggerFactory.getLogger(WahaWebhookService::class.java),
    val n8nClient: N8nClient,
    val appointmentsRepository: AppointmentsRepository,
) : WahaWebhookService {
    override fun receiveMessage(whatsAppMessage: WhatsAppMessage) {
        val clinicEntity = clinicRepositoryService.findByWahaSession(whatsAppMessage.session)
        val clinicConfiguration = clinicConfigurationRepository.findByClinicId(clinicEntity.id)
        val clinic: Clinic = clinicEntity.toDomain()

        logger.info("Resolving received message phone number, session=${whatsAppMessage.session}")
        val phoneNumber = wahaPhoneResolverService.resolvePhoneNumber(clinic.wahaSession, whatsAppMessage.phoneNumber)
            ?: run {
                logger.warn(
                    "Could not resolve phone number, " +
                            "session=${whatsAppMessage.session}, " +
                            "sender=${whatsAppMessage.phoneNumber}"
                )
                return
            }

        if (!wahaPhoneResolverService.isIndividualChat(whatsAppMessage.phoneNumber)) {
            logger.info("Ignoring non-individual chat message from {}", whatsAppMessage.phoneNumber)
            return
        }

        val resolvedMessage = whatsAppMessage.copy(phoneNumber = phoneNumber.replace("@c.us", ""))

        sendReceivedMessageToBlinkFe(resolvedMessage, clinicCode = clinic.code)

        val patient: Patient = patientRepositoryService.findByClinicCodeAndPhoneNumber(clinic.code, phoneNumber)
            ?.toDomain()
            ?: run {

                logger.info("Creating new patient, clinicCode=${clinic.code}, phoneNumber=$phoneNumber")
                val newPatient = PatientEntity.builder()
                    .phoneNumber(phoneNumber)
                    .name(whatsAppMessage.name ?: "")
                    .clinic(clinicEntity)
                    .aiAnswer(clinicConfiguration.defaultAiAnswer)
                    .createdAt(LocalDateTime.now())
                    .build()
                patientRepositoryService.save(newPatient).toDomain()
            }

        if (resolvedMessage.fromMe || !patient.aiAnswer!!) {
            logger.info(
                "Skipping ai answer, " +
                        "session=${whatsAppMessage.session}, " +
                        "sender=${whatsAppMessage.phoneNumber}"
            )
            return
        }

        sendReceivedMessageToN8n(resolvedMessage, patient, clinic, clinicConfiguration.aiName)
    }

    override fun sessionStatusUpdated(
        session: String,
        status: WahaSessionStatus,
        phoneNumber: String?
    ) {
        logger.info("Received session $session status $status, phoneNumber $phoneNumber")
    }

    private fun sendReceivedMessageToBlinkFe(
        whatsAppMessage: WhatsAppMessage,
        clinicCode: String
    ) {
        logger.info(
            "Sending message to blink-fe, " +
                    "session=${whatsAppMessage.session}, " +
                    "sender=${whatsAppMessage.phoneNumber}"
        )
        runCatching {
            userRepository.findAllByClinicCode(clinicCode)
                .mapNotNull { it.userId?.toString() }
                .also { userIds ->
                    if (userIds.isEmpty()) {
                        logger.warn(
                            "No users found for clinicId: {}. Cannot send websocket message.",
                            clinicCode
                        )
                    }
                }
                .forEach { userId ->
                    simpMessagingTemplate.convertAndSendToUser(
                        userId,
                        "/notify/message-received",
                        whatsAppMessage
                    )
                    logger.info("Sent message to user {} for clinic {}", userId, clinicCode)
                }
        }.onFailure { e ->
            logger.error("Message not sent to front end", e)
        }
    }

    private fun sendReceivedMessageToN8n(
        whatsAppMessage: WhatsAppMessage,
        patient: Patient,
        clinic: Clinic,
        aiName: String
    ) {
        logger.info("Sending message to N8n, clinicId={}, phoneNumber={}", clinic.code, whatsAppMessage.phoneNumber)

        val patientName = patient.name
        val appointment = patient.code?.let {
            appointmentsRepository.findAllByPatientCodeAndScheduledTimeAfter(it, LocalDateTime.now().minusDays(7))
        } ?: emptyList()

        try {
            n8nClient.receiveMessage(
                MessageReceived(
                    sender = whatsAppMessage.phoneNumber,
                    message = whatsAppMessage.message,
                    patientName = patientName,
                    appointmentsData = appointment.filter { appointment1 -> appointment1.isNotCancelled }
                        .map { MessageReceived.AppointmentsData.fromAppointment(it) },
                    clinicName = clinic.name,
                    clinicCode = clinic.code,
                    aiName = aiName,
                    sentAt = Instant.ofEpochSecond(whatsAppMessage.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                )
            )
        } catch (e: Exception) {
            logger.error("N8n responded with an error, e={}", e.message)
        }
    }

}