package com.example.spendly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.spendly.R

// Bricolage Grotesque is a variable font (one .ttf covers every weight).
// Each Font(...) entry picks a point on the weight axis.
//
// It also has an optical-size axis ("opsz", 12–96): letter shapes tuned for small or
// huge text. Its default is 96, which looks cramped below display sizes, so there are
// three families, one per size band:
//   BricolageDisplay (opsz 96) - the big totals
//   Bricolage        (opsz 24) - titles
//   BricolageText    (opsz 14) - body text and labels
@OptIn(ExperimentalTextApi::class)
private fun bricolage(weight: FontWeight, opticalSize: Float) = Font(
    R.font.bricolage,
    weight = weight,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight.weight),
        FontVariation.Setting("opsz", opticalSize),
    ),
)

val BricolageDisplay = FontFamily(
    bricolage(FontWeight.SemiBold, 96f),
    bricolage(FontWeight.Bold, 96f),
)

val Bricolage = FontFamily(
    bricolage(FontWeight.SemiBold, 24f),
    bricolage(FontWeight.Bold, 24f),
)

val BricolageText = FontFamily(
    bricolage(FontWeight.Normal, 14f),
    bricolage(FontWeight.Medium, 14f),
    bricolage(FontWeight.SemiBold, 14f),
)

/** Tabular digits: every number is the same width, so totals don't jiggle as they change. */
private const val TabularNumbers = "tnum"

val AppTypography = Typography(
    // Amount you type on the Add screen
    displayLarge = TextStyle(
        fontFamily = BricolageDisplay, fontWeight = FontWeight.Bold,
        fontSize = 64.sp, lineHeight = 68.sp, letterSpacing = (-2).sp,
        fontFeatureSettings = TabularNumbers,
    ),
    // Month total on the summary card
    displayMedium = TextStyle(
        fontFamily = BricolageDisplay, fontWeight = FontWeight.Bold,
        fontSize = 46.sp, lineHeight = 48.sp, letterSpacing = (-1.5).sp,
        fontFeatureSettings = TabularNumbers,
    ),
    // "Spendly" wordmark
    headlineSmall = TextStyle(
        fontFamily = Bricolage, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = (-0.5).sp,
    ),
    // Screen titles ("New expense")
    titleLarge = TextStyle(
        fontFamily = Bricolage, fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.3).sp,
    ),
    // Row titles, row amounts
    titleMedium = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp,
        fontFeatureSettings = TabularNumbers,
    ),
    // Section labels ("Category", "Note")
    titleSmall = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 18.sp,
    ),
    // Buttons
    labelLarge = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp, lineHeight = 20.sp,
    ),
    // Day headers ("TODAY · THU 8 OCT")
    labelMedium = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.8.sp,
        fontFeatureSettings = TabularNumbers,
    ),
    labelSmall = TextStyle(
        fontFamily = BricolageText, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 16.sp,
    ),
)
