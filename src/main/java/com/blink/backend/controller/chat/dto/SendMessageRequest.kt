package com.blink.backend.controller.chat.dto

class SendMessageRequest(
    val message: String,
    val phoneNumber: String,
    val wait: Int = 0,
)