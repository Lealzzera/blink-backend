package com.blink.backend.domain.clinic.chat.service

import com.blink.backend.controller.message.dto.SendMessageRequest
import com.blink.backend.domain.clinic.chat.model.WhatsAppConversation
import com.blink.backend.domain.clinic.chat.model.WhatsAppConversationHistory
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
            .filter { conversationsDto -> !conversationsDto.lastMessage.data.isGroup }
            .map { wahaConversation ->
                val patient: Patient =
                    patientRepository.findByClinic_CodeAndPhoneNumber(clinic.code, wahaConversation.getSender())
                        .map { entity -> entity.toDomain() }
                        .orElseGet { Patient(aiAnswer = true, name = "", phoneNumber = "") }
                wahaConversation.toDomain(patient.aiAnswer, patientName = patient.name)
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