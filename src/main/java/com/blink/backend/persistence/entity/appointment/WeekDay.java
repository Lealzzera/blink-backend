package com.blink.backend.persistence.entity.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WeekDay {
    SEGUNDA(DayOfWeek.MONDAY),
    TERCA(DayOfWeek.TUESDAY),
    QUARTA(DayOfWeek.WEDNESDAY),
    QUINTA(DayOfWeek.THURSDAY),
    SEXTA(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY),
    DOMINGO(DayOfWeek.SUNDAY);

    private final DayOfWeek weekDay;

    public static WeekDay fromDate(LocalDate date) {
        return Arrays.stream(WeekDay.values())
                .filter(dayWeek -> date.getDayOfWeek().equals(dayWeek.getWeekDay()))
                .findFirst()
                .orElseThrow();
    }
}
