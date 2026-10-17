package com.example.spendly.core.domain.models


import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object Money {

    private val IndianLocale = Locale("en", "IN")

    private val AmountInput = Regex("^\\d{0,9}(\\.\\d{0,2})?$")

    fun format(paise: Long): String {
        val wholeRupees = paise % 100 == 0L
        val formatter = NumberFormat.getCurrencyInstance(IndianLocale).apply {
            minimumFractionDigits = if (wholeRupees) 0 else 2
            maximumFractionDigits = if (wholeRupees) 0 else 2
        }
        return formatter.format(BigDecimal.valueOf(paise, 2))
    }

    fun parseToPaise(text: String): Long? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return null
        return try {
            BigDecimal(trimmed)
                .setScale(2, RoundingMode.UNNECESSARY)
                .movePointRight(2)
                .longValueExact()
        } catch (e: NumberFormatException) {
            null
        } catch (e: ArithmeticException) {
            null
        }
    }

    fun isAcceptableInput(text: String): Boolean = AmountInput.matches(text)
}