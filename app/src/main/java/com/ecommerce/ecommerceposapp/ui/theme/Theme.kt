package com.ecommerce.ecommerceposapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Light scheme — fondo blanco predominante, acento rojo + amarillo ──────────
private val LightColorScheme = lightColorScheme(
    // Primario: Rojo #FD0505
    primary                = BrandRed,
    onPrimary              = TextOnBrand,
    primaryContainer       = BrandRedLight,
    onPrimaryContainer     = BrandRedDark,

    // Secundario: Amarillo #FFC107
    secondary              = BrandYellow,
    onSecondary            = TextOnYellow,
    secondaryContainer     = BrandYellowLight,
    onSecondaryContainer   = GrayDark,

    // Terciario: éxito verde (informativo)
    tertiary               = GreenSuccess,
    onTertiary             = TextOnBrand,
    tertiaryContainer      = GreenSuccessLight,
    onTertiaryContainer    = GrayDark,

    // Fondos: blanco puro
    background             = AppBackground,
    onBackground           = TextPrimary,

    // Superficies: blanco puro
    surface                = SurfaceWhite,
    onSurface              = TextPrimary,
    surfaceVariant         = SurfaceMuted,
    onSurfaceVariant       = TextSecondary,
    surfaceTint            = Color.Transparent,

    // Bordes y divisores
    outline                = BorderDefault,
    outlineVariant         = GrayLight,

    // Errores
    error                  = RedDanger,
    onError                = TextOnBrand,
    errorContainer         = RedDangerLight,
    onErrorContainer       = BrandRedDark,

    // Inverso (tooltips, snackbars oscuros)
    inverseSurface         = DarkSurface,
    inverseOnSurface       = TextOnDark,
    inversePrimary         = BrandRedLight,

    scrim                  = Color(0x52000000),
)

// ── Dark scheme — reservado para futura implementación ───────────────────────
private val DarkColorScheme = darkColorScheme(
    primary                = BrandRed,
    onPrimary              = TextOnBrand,
    primaryContainer       = BrandRedDeep,
    onPrimaryContainer     = BrandRedLight,
    secondary              = BrandYellow,
    onSecondary            = TextOnYellow,
    secondaryContainer     = BrandYellowDark,
    onSecondaryContainer   = BrandYellowLight,
    background             = NavyDark,
    onBackground           = TextOnDark,
    surface                = NavyMedium,
    onSurface              = TextOnDark,
    surfaceVariant         = NavyLight,
    onSurfaceVariant       = TextMuted,
    error                  = RedError,
    onError                = TextOnBrand,
)

@Composable
fun EcommercePosAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content,
    )
}
