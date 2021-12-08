package com.vsahin.praytimes.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val colors = Colors(
    primary = Gray500,
    primaryVariant = Gray500,
    secondary = Blue500
)

@Composable
fun PrayTimesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}