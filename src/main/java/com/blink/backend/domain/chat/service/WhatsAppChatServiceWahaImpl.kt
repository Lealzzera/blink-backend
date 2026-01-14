package com.blink.backend.domain.chat.service

import com.blink.backend.controller.chat.dto.SendMessageRequest
import com.blink.backend.domain.chat.model.WhatsAppConversation
import com.blink.backend.domain.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.chat.model.WhatsAppStatus
import com.blink.backend.domain.exception.message.WhatsAppNotConnectedException
import com.blink.backend.domain.integration.waha.WahaClient
import com.blink.backend.domain.integration.waha.WahaPhoneResolverService
import com.blink.backend.domain.integration.waha.dto.ChatHistory
import com.blink.backend.domain.integration.waha.dto.SendWahaMessageRequest
import com.blink.backend.domain.integration.waha.dto.WahaPresenceDto
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.Patient
import com.blink.backend.persistence.repository.PatientRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class WhatsAppChatServiceWahaImpl(
    val wahaClient: WahaClient,
    val patientRepository: PatientRepository,
    val wahaPhoneResolverService: WahaPhoneResolverService,
    val wahaAuthService: WhatsAppAuthService,
    val logger: Logger = LoggerFactory.getLogger(WhatsAppChatServiceWahaImpl::class.java)
) : WhatsAppChatService {

    override fun sendMessageByClinic(
        clinic: Clinic,
        messageToSend: SendMessageRequest
    ) {
        val status = wahaAuthService.getStatusByClinic(clinic).status
        require(status != WhatsAppStatus.Status.DISCONNECTED) {
            throw WhatsAppNotConnectedException()
        }

        Thread.sleep(Duration.ofSeconds(messageToSend.wait.toLong()))

        val chatId = "${messageToSend.phoneNumber}@c.us"
        val typingTimeMs = 70L * messageToSend.message.length

        val presence = WahaPresenceDto.builder()
            .session(clinic.wahaSession)
            .chatId(chatId)
            .build()

        runCatching {
            wahaClient.sendSeen(presence)
        }.onFailure {
            logger.warn("Seen not sent, continuing flow: {}", it.message)
        }

        wahaClient.startTyping(presence)
        Thread.sleep(typingTimeMs)

        wahaClient.sendMessage(
            SendWahaMessageRequest.builder()
                .session(clinic.wahaSession)
                .phoneNumber(chatId)
                .text(messageToSend.message)
                .build()
        )

        wahaClient.stopTyping(presence)
    }

    override fun getConversationsByClinic(
        clinic: Clinic,
        page: Int,
        pageSize: Int
    ): List<WhatsAppConversation> {
        val wahaConversations =
            wahaClient.getOverview(clinic.wahaSession, limit = pageSize, offset = page * pageSize)

        logger.info("Fetched ${wahaConversations.size} conversations")
        return wahaConversations
            .filter { conversationsDto -> wahaPhoneResolverService.isIndividualChat(conversationsDto.chat.id) }
            .mapNotNull { wahaConversation ->
                val phoneNumber =
                    wahaPhoneResolverService.resolvePhoneNumber(clinic.wahaSession, wahaConversation.getChatId())
                        ?: return@mapNotNull null
                val patient: Patient =
                    patientRepository.findByClinic_CodeAndPhoneNumber(clinic.code, phoneNumber)
                        .map { entity -> entity.toDomain() }
                        .orElseGet {
                            Patient(
                                null,
                                aiAnswer = false,
                                phoneNumber = phoneNumber,
                                name = wahaConversation.name ?: ""
                            )
                        }
                wahaConversation.toDomain(patient.aiAnswer!!, patientName = patient.name, phoneNumber = phoneNumber)
            }

    }

    override fun getConversationHistoryByClinicAndNumber(
        clinic: Clinic,
        phoneNumber: String,
        page: Int,
        pageSize: Int
    ): List<WhatsAppConversationHistory> {
        return wahaClient.getMessages(
            session = clinic.wahaSession,
            chatId = phoneNumber,
            limit = pageSize,
            offset = page * pageSize
        )
            .map { conversation ->
                conversation.toDomain()
            }
    }
}