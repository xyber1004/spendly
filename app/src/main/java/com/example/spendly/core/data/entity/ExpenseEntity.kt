package com.example.spendly.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.spendly.core.domain.enums.Category

@Entity(
    tableName = "expenses",
    indices = [Index("spent_at")],
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "amount_paise") val amountPaise: Long,
    val category: String,
    val note: String,
    @ColumnInfo(name = "spent_at") val spentAt: Long
)