package com.blink.backend.domain.chat.service

import com.blink.backend.controller.message.dto.SendMessageRequest
import com.blink.backend.domain.chat.model.WhatsAppConversation
import com.blink.backend.domain.chat.model.WhatsAppConversationHistory
import com.blink.backend.domain.integration.waha.WahaClient
import com.blink.backend.domain.model.Clinic
import com.blink.backend.domain.model.Patient
import com.blink.backend.persistence.repository.PatientRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class WhatsAppChatServiceWahaImpl(
    val wahaClientImpl: WahaClient,
    val patientRepository: PatientRepository,
) : WhatsAppChatService {

    companion object {
        private const val LID_SUFFIX = "@lid"
    }

    private fun resolvePhoneNumber(session: String, chatId: String): String? {
        return if (chatId.endsWith(LID_SUFFIX)) {
            val lid = chatId.substringBefore("@")
            wahaClientImpl.getPhoneNumberByLid(session, lid)?.pn
        } else {
            chatId
        }
            ?.substringBefore("@")
            ?.substringAfter(":")
    }

    override fun sendMessageByClinic(
        clinic: Clinic,
        sendMessageRequest: SendMessageRequest
    ) {
        TODO("Not yet implemented")
    }

    override fun getConversationsByClinic(
        clinic: Clinic,
        page: Int,
        pageSize: Int
    ): List<WhatsAppConversation> {
        val wahaConversations =
            wahaClientImpl.getOverview(clinic.wahaSession, limit = pageSize, offset = page * pageSize)

        return wahaConversations
            .filter { conversationsDto -> !conversationsDto.chat.id.contains("@g.us") }
            .mapNotNull { wahaConversation ->
                val phoneNumber = resolvePhoneNumber(clinic.wahaSession, wahaConversation.getChatId())
                    ?: return@mapNotNull null
                val patient: Patient =
                    patientRepository.findByClinic_CodeAndPhoneNumber(clinic.code, phoneNumber)
                        .map { entity -> entity.toDomain() }
                        .orElseGet { Patient(aiAnswer = true, name = "", phoneNumber = phoneNumber) }
                wahaConversation.toDomain(patient.aiAnswer, patientName = patient.name, phoneNumber = phoneNumber)
            }

    }

    override fun getConversationHistoryByClinicAndNumber(
        clinic: Clinic,
        phoneNumber: String,
        page: Int,
        pageSize: Int
    ): Page<WhatsAppConversationHistory> {
        TODO("Not yet implemented")
    }
}