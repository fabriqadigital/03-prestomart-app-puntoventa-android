package com.ecommerce.ecommerceposapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandRed,
    onPrimary = Color.White,
    primaryContainer = BrandRedDark,
    onPrimaryContainer = Color.White,
    secondary = BlueLight,
    onSecondary = Color.White,
    secondaryContainer = BlueDeep,
    onSecondaryContainer = Color.White,
    tertiary = BluePale,
    background = NavyDark,
    onBackground = TextOnDark,
    surface = NavyMedium,
    onSurface = TextOnDark,
    surfaceVariant = NavyLight,
    onSurfaceVariant = TextMuted,
    error = RedError,
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = BrandRedDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = BrandRedDeep,
    secondary = BlueDeep,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E4FF),
    onSecondaryContainer = Color(0xFF001B3F),
    tertiary = BlueMedium,
    background = GrayLight,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    surfaceVariant = GrayMedium,
    onSurfaceVariant = GrayDark,
    error = BrandRedDark,
    onError = Color.White,
)

@Composable
fun EcommercePosAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
