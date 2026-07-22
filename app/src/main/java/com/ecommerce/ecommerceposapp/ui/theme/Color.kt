package com.ecommerce.ecommerceposapp.ui.theme

import androidx.compose.ui.graphics.Color

// ── Brand ────────────────────────────────────────────────────────────────────
val BrandRed          = Color(0xFFFD0505)
val BrandRedDark      = Color(0xFFB80404)
val BrandRedDeep      = Color(0xFF7B0000)
val BrandRedLight     = Color(0xFFFFE8E8)   // fondo suave para badges / tint

// ── Amarillo (secundario / acciones / resaltados) ────────────────────────────
val BrandYellow       = Color(0xFFFFC107)
val BrandYellowDark   = Color(0xFFE6A800)
val BrandYellowLight  = Color(0xFFFFF8E1)   // fondo suave amarillo

// ── Fondos — siempre blancos / muy claros ────────────────────────────────────
val AppBackground     = Color(0xFFFFFFFF)   // fondo raíz
val SurfaceWhite      = Color(0xFFFFFFFF)   // tarjetas, paneles
val SurfaceSubtle     = Color(0xFFF9FAFB)   // superficies secundarias
val SurfaceMuted      = Color(0xFFF3F4F6)   // chips, tags, separadores

// ── Bordes y divisores ───────────────────────────────────────────────────────
val BorderDefault     = Color(0xFFE5E7EB)
val BorderFocus       = BrandRed

// ── Texto ────────────────────────────────────────────────────────────────────
val TextPrimary       = Color(0xFF111827)
val TextSecondary     = Color(0xFF6B7280)
val TextTertiary      = Color(0xFF9CA3AF)
val TextOnBrand       = Color(0xFFFFFFFF)
val TextOnYellow      = Color(0xFF111827)   // texto oscuro sobre fondo amarillo

// ── Estados ──────────────────────────────────────────────────────────────────
val GreenSuccess      = Color(0xFF16A34A)
val GreenSuccessLight = Color(0xFFDCFCE7)
val RedDanger         = Color(0xFFDC2626)
val RedDangerLight    = Color(0xFFFFE4E6)
val BlueInfo          = Color(0xFF2563EB)
val BlueInfoLight     = Color(0xFFEFF6FF)
val OrangeWarning     = Color(0xFFF59E0B)
val OrangeWarningLight= Color(0xFFFFF7ED)

// ── Grises para textos únicamente ────────────────────────────────────────────
val GrayDark          = Color(0xFF374151)
val GrayMedium        = Color(0xFF6B7280)
val GrayLight         = Color(0xFFD1D5DB)
val GrayMuted         = Color(0xFF9E9E9E)

// ── Aliases del tema antiguo para no romper compilación ─────────────────────
@Deprecated("Usa AppBackground") val GrayLightBg = AppBackground

// ── Dark mode (reservado) ────────────────────────────────────────────────────
val NavyDark          = Color(0xFF0D1117)
val NavyMedium        = Color(0xFF1A1F36)
val NavyLight         = Color(0xFF1E2A4A)
val DarkSurface       = Color(0xFF111827)
val DarkCard          = Color(0xFF1C2333)
val BlueDeep          = Color(0xFF1E40AF)
val BlueMedium        = Color(0xFF2563EB)
val BlueLight         = Color(0xFF3B82F6)
val BluePale          = Color(0xFF60A5FA)
val GreenSuccessDark  = Color(0xFF4CAF50)
val RedError          = Color(0xFFCF6679)
val TextOnDark        = Color(0xFFFFFFFF)
val TextMuted         = Color(0xFFB0BEC5)
val TextDark          = Color(0xFF212121)
