package com.blink.backend.controller.configuration.dto

import java.time.LocalDate
import java.time.LocalTime

data class AtypicalWorkdayDto(
    val date: LocalDate,
    val isWorkingDay: Boolean,
    val open: LocalTime? = null,
    val close: LocalTime? = null,
    val breakStart: LocalTime? = null,
    val breakEnd: LocalTime? = null,
) {
    init {
        if (isWorkingDay) {
            require(open != null) { "open is required when isWorkingDay is true" }
            require(close != null) { "close is required when isWorkingDay is true" }
        }
        require((breakStart == null) == (breakEnd == null)) {
            "breakStart and breakEnd must both be provided or both be null"
        }
    }
}