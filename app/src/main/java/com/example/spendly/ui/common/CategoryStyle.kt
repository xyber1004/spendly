package com.example.spendly.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.enums.Category.SHOPPING



data class CategoryStyle(
    val icon: ImageVector,
    val tint: Color,
    val container: Color,
    val chart: Color,
)

val Category.style: CategoryStyle
    get() = when (this) {
        Category.FOOD -> CategoryStyle(
            Icons.Rounded.Restaurant, Color(0xFF0F6B4F), Color(0xFFDCEFE7), Color(0xFF0F6B4F),
        )
        Category.BILLS -> CategoryStyle(
            Icons.Rounded.Bolt, Color(0xFF9A5B00), Color(0xFFFBEBD0), Color(0xFFE8A33D),
        )
        Category.TRANSPORT -> CategoryStyle(
            Icons.Rounded.DirectionsBus, Color(0xFF2D6FA8), Color(0xFFDDEAF6), Color(0xFF3B82C4),
        )
        SHOPPING -> CategoryStyle(
            Icons.Rounded.ShoppingBag, Color(0xFF7546A0), Color(0xFFEEE4F6), Color(0xFF8E5BB5),
        )
        Category.OTHER -> CategoryStyle(
            Icons.Rounded.MoreHoriz, Color(0xFF55625D), Color(0xFFE7EBE9), Color(0xFFB8C2BE),
        )
    }

@Composable
fun CategoryIcon(category: Category, modifier: Modifier = Modifier) {
    val style = category.style
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(style.container)
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.tint,
            modifier = Modifier.size(22.dp)
        )
    }
}