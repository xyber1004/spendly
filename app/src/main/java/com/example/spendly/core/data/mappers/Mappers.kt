package com.example.spendly.core.data.mappers

import com.example.spendly.core.data.entity.ExpenseEntity
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.models.Expense
import java.time.Instant


fun ExpenseEntity.toDomain(): Expense = Expense(
    id = id,
    amountPaise = amountPaise,
    category = Category.entries.firstOrNull { it.name == category } ?: Category.OTHER,
    note = note,
    spentAt = Instant.ofEpochMilli(spentAt)
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    amountPaise = amountPaise,
    category = category.name,
    note = note,
    spentAt = spentAt.toEpochMilli()
)