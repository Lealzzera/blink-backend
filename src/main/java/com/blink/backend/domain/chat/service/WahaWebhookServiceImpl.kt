package com.blink.backend.domain.chat.service

import com.blink.backend.domain.chat.model.WhatsAppMessage
import com.blink.backend.domain.integration.waha.WahaPhoneResolverService
import com.blink.backend.domain.integration.waha.dto.WahaSessionStatus
import com.blink.backend.domain.model.Clinic
import com.blink.backend.persistence.repository.clinic.ClinicRepositoryService
import com.blink.backend.persistence.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WahaWebhookServiceImpl(
    val userRepository: UserRepository,
    val simpMessagingTemplate: SimpMessagingTemplate,
    val clinicRepositoryService: ClinicRepositoryService,
    val wahaPhoneResolverService: WahaPhoneResolverService,
    val logger: Logger = LoggerFactory.getLogger(WahaWebhookService::class.java),
) : WahaWebhookService {
    override fun receiveMessage(whatsAppMessage: WhatsAppMessage) {
        val clinic: Clinic = clinicRepositoryService.findByWahaSession(whatsAppMessage.session).toDomain()

        val phoneNumber = wahaPhoneResolverService.resolvePhoneNumber(clinic.wahaSession, whatsAppMessage.sender)
            ?: run {
                logger.warn("Could not resolve phone number for sender {}", whatsAppMessage.sender)
                return
            }

        if (!wahaPhoneResolverService.isIndividualChat(whatsAppMessage.sender)) {
            logger.info("Ignoring non-individual chat message from {}", whatsAppMessage.sender)
            return
        }

        val resolvedMessage = whatsAppMessage.copy(sender = phoneNumber)

        sendReceivedMessageToBlinkFe(resolvedMessage, clinicCode = clinic.code)
        /* TODO
         *val optionalPatient: Optional<PatientEntity?> = patientRepository.findByPhoneNumber(sender)
         * if (!isAiResponseTurnedOn(optionalPatient)) {
          *    WahaService.log.info("Automatic response turned off for patient {}", sender)
          *    return
          *}
          *sendReceivedMessageToN8n(sender, message, optionalPatient, clinic)
          *
         */
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

}