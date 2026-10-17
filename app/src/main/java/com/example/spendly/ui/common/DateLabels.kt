package com.example.spendly.ui.common

import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DayFormatter = DateTimeFormatter.ofPattern("EEE d MMM", Locale.getDefault())
private val DayWithYearFormatter = DateTimeFormatter.ofPattern("EEE d MMM yyyy", Locale.getDefault())
private val TimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
private val MonthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
private val MonthNameFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())

fun friendlyDate(
    date: LocalDate,
    today: LocalDate = LocalDate.now(),
    separator: String = " - "
): String {
    val formatter = if (date.year == today.year) DayFormatter else DayWithYearFormatter
    val day = date.format(formatter)
    return when (date) {
        today -> "Today$separator$day"
        today.minusDays(1) -> "Yesterday$separator$day"
        else -> day
    }
}

fun formatTime(time: LocalTime): String = time.format(TimeFormatter).lowercase(Locale.getDefault())

fun formatMonth(month: YearMonth): String = month.format(MonthFormatter)

fun formatMonthName(month: YearMonth): String = month.format(MonthNameFormatter)