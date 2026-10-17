package com.example.spendly.feature.expenses.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale


fun lastMonths(count: Int, from: YearMonth = YearMonth.now()): List<YearMonth> = List(count) {
    from.minusMonths(it.toLong())
}

private val MonthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

@Composable
fun MonthDropdownMenu(
    selected: YearMonth,
    onSelect: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    months: List<YearMonth> = remember { lastMonths(12) }
) {
    var expanded by remember { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Box(modifier) {
        Surface(
            onClick = { expanded = true },
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xffDDE3E0)),
            modifier = Modifier.heightIn(min = 44.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 16.dp, end = 14.dp)
            ) {
                Text(
                    text = selected.format(MonthFormatter),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff15201C)
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xff15201C),
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(chevronRotation)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month.format(MonthFormatter)) },
                    onClick = {
                        onSelect(month)
                        expanded = false
                    },
                    trailingIcon = if (month == selected) {
                        { Icon(Icons.Rounded.Check, contentDescription = "Selected") }
                    } else null
                )
            }
        }
    }
}