package com.example.spendly.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SpendlyColors = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = Green,
    background = Paper,
    onBackground = Ink,
    surface = Card,
    onSurface = Ink,
    onSurfaceVariant = InkMuted,
    surfaceVariant = LineSoft,
    // M3 tints menus/dialogs with these; set them so nothing turns lilac.
    surfaceContainerLowest = Card,
    surfaceContainerLow = Card,
    surfaceContainer = Card,
    surfaceContainerHigh = Card,
    surfaceContainerHighest = LineSoft,
    outline = Line,
    outlineVariant = LineSoft,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed,
    // Snackbar = inverseSurface, its action button = inversePrimary
    inverseSurface = Ink,
    inverseOnSurface = Color.White,
    inversePrimary = Mint,
)

@Composable
fun SpendlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SpendlyColors,
        typography = AppTypography,
        content = content,
    )
}