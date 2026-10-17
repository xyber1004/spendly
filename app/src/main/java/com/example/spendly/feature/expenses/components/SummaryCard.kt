package com.example.spendly.feature.expenses.components

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spendly.core.domain.models.Money
import com.example.spendly.feature.expenses.ExpensesUiState
import com.example.spendly.ui.common.formatMonthName
import com.example.spendly.ui.common.style
import java.time.YearMonth
import kotlin.math.roundToInt

@Composable
fun SummaryCard(state: ExpensesUiState, modifier: Modifier = Modifier) {

    val category = state.selectedCategory
    val label = when {
        category != null -> "Spent on ${category.label}"
        state.month == YearMonth.now() -> "Spent this month"
        else -> "Spent in ${formatMonthName(state.month)}"
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (state.shownCount == 1) "1 expense" else "${state.shownCount} expenses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(Money.format(state.shownTotalInPaise), style = MaterialTheme.typography.displayMedium)

            if (state.monthTotalToPaise > 0) {
                if (category == null) {
                    SegmentBar(state.breakdown.map {
                        BarSegment(it.totalPaise, it.category.style.chart)
                    })
                    Legend(state)
                } else {
                    val rest = state.monthTotalToPaise - state.shownTotalInPaise
                    SegmentBar(
                        listOf(
                            BarSegment(state.shownTotalInPaise, category.style.chart),
                            BarSegment(rest, MaterialTheme.colorScheme.outlineVariant)
                        )
                    )
                    val percent = (state.shownTotalInPaise * 100.0 / state.monthTotalToPaise).roundToInt()
                    Text(
                        text = "$percent% of ${Money.format(state.monthTotalToPaise)} spent in ${formatMonthName(state.month)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private data class BarSegment(val weight: Long, val color: Color)

@Composable
private fun SegmentBar(segments: List<BarSegment>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        segments
            .filter { it.weight > 0 }
            .forEach { segment ->
                Box(
                    Modifier
                        .weight(segment.weight.toFloat())
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(5.dp))
                        .background(segment.color)
                )
            }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Legend(state: ExpensesUiState) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.breakdown.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(item.category.style.chart)
                )
                Text(
                    text = "${item.category.label} ${Money.format(item.totalPaise)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}