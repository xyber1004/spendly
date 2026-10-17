package com.example.spendly.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.spendly.core.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query(
        """
            SELECT * FROM expenses
            WHERE spent_at >= :startMillis AND spent_at <= :endMillis
            ORDER BY spent_at DESC
        """
    )
    fun observeBetween(startMillis: Long, endMillis: Long): Flow<List<ExpenseEntity>>

    @Upsert
    suspend fun upsert(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)
}