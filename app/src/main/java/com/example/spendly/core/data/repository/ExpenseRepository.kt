package com.example.spendly.core.data.repository

import com.example.spendly.core.data.dao.ExpenseDao
import com.example.spendly.core.data.mappers.toDomain
import com.example.spendly.core.data.mappers.toEntity
import com.example.spendly.core.domain.models.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.ZoneId

interface ExpenseRepository {
    fun observeMonth(month: YearMonth): Flow<List<Expense>>
    suspend fun save(expense: Expense)
    suspend fun delete(expense: Expense)
}

class RoomExpenseRepository(
    private val dao: ExpenseDao,
    private val zone: ZoneId = ZoneId.systemDefault()
): ExpenseRepository {
    override fun observeMonth(month: YearMonth): Flow<List<Expense>> {
        val start = month.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end = month.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        return dao.observeBetween(start, end).map { rows -> rows.map { it.toDomain() } }
    }

    override suspend fun save(expense: Expense) = dao.upsert(expense.toEntity())

    override suspend fun delete(expense: Expense) = dao.delete(expense.toEntity())

}