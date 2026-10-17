package com.example.spendly.core.domain.models

import com.example.spendly.core.domain.enums.Category
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Expense(
    val id: Long = 0,
    val amountPaise: Long,
    val category: Category,
    val note: String,
    val spentAt: java.time.Instant,
) {
    // If note is null then show category
    val title: String get() = note.ifBlank { category.label }
}