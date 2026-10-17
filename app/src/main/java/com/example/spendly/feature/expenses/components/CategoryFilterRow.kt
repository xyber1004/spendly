package com.example.spendly.feature.expenses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spendly.core.domain.enums.Category

@Composable
fun CategoryFilterRow(
    selected: Category?,
    onSelected: (Category?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier
    ) {
        item(key = "all") {
            SpendlyChip(label = "All", selected = selected == null, showCheck = false, onClick = { onSelected(null) })
        }
        items(Category.entries, key = { it.name }) { category ->
            SpendlyChip(
                label = category.label,
                selected = selected == category,
                showCheck = true,
                onClick = { onSelected(category) }
            )
        }
    }
}

@Composable
private fun SpendlyChip(
    label: String,
    selected: Boolean,
    showCheck: Boolean,
    onClick: () -> Unit
) {
    val ink = MaterialTheme.colorScheme.onSurface
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = if (selected && showCheck) {
            { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null,
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = ink,
            selectedContainerColor = ink,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = ink,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp,
        ),
        modifier = Modifier.height(40.dp),
    )
}