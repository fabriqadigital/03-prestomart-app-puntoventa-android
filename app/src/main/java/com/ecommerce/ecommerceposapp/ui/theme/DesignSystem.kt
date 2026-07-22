package com.ecommerce.ecommerceposapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
//  TOKENS DE ESPACIADO
// ─────────────────────────────────────────────────────────────────────────────
object Spacing {
    val xs  = 4.dp
    val sm  = 8.dp
    val md  = 12.dp
    val lg  = 16.dp
    val xl  = 24.dp
    val xxl = 32.dp
}

// ─────────────────────────────────────────────────────────────────────────────
//  TOKENS DE RADIO
// ─────────────────────────────────────────────────────────────────────────────
object Radius {
    val sm   = 8.dp
    val md   = 12.dp
    val lg   = 16.dp
    val xl   = 20.dp
    val xxl  = 28.dp
    val pill = 999.dp
}

// ─────────────────────────────────────────────────────────────────────────────
//  TOKENS DE ELEVACIÓN
// ─────────────────────────────────────────────────────────────────────────────
object Elevation {
    val none   = 0.dp
    val low    = 1.dp
    val medium = 3.dp
    val high   = 6.dp
    val dialog = 12.dp
}

// ─────────────────────────────────────────────────────────────────────────────
//  TOKENS DE TAMAÑO DE BOTÓN
// ─────────────────────────────────────────────────────────────────────────────
object ButtonSize {
    val height       = 50.dp
    val heightSmall  = 40.dp
    val heightLarge  = 56.dp
    val paddingH     = 20.dp
    val paddingV     = 0.dp   // manejado por height
}

// ─────────────────────────────────────────────────────────────────────────────
//  BOTÓN PRIMARIO  (Rojo #FD0505)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
    height: Dp = ButtonSize.height,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(Radius.md),
        colors = ButtonDefaults.buttonColors(
            containerColor         = BrandRed,
            contentColor           = Color.White,
            disabledContainerColor = GrayLight,
            disabledContentColor   = GrayMedium,
        ),
        contentPadding = PaddingValues(horizontal = ButtonSize.paddingH),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
            Spacer(Modifier.width(Spacing.sm))
        } else if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(Spacing.sm))
        }
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  BOTÓN SECUNDARIO  (Amarillo #FFC107)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    height: Dp = ButtonSize.height,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(Radius.md),
        colors = ButtonDefaults.buttonColors(
            containerColor         = BrandYellow,
            contentColor           = TextOnYellow,
            disabledContainerColor = GrayLight,
            disabledContentColor   = GrayMedium,
        ),
        contentPadding = PaddingValues(horizontal = ButtonSize.paddingH),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp),
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(Spacing.sm))
        }
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  BOTÓN OUTLINE  (borde rojo, fondo blanco)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    height: Dp = ButtonSize.height,
    color: Color = BrandRed,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = RoundedCornerShape(Radius.md),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor           = color,
            disabledContentColor   = GrayMedium,
        ),
        border = BorderStroke(1.dp, if (enabled) color else GrayLight),
        contentPadding = PaddingValues(horizontal = ButtonSize.paddingH),
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(Spacing.sm))
        }
        Text(text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  BOTÓN GHOST / TEXT  (sin fondo)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = TextSecondary,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(Radius.md),
        colors = ButtonDefaults.textButtonColors(
            contentColor         = color,
            disabledContentColor = GrayMedium,
        ),
    ) {
        Text(text, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  TEXTFIELD UNIFICADO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    minLines: Int = 1,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it, color = TextTertiary) } },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = null, tint = if (isError) RedDanger else TextSecondary, modifier = Modifier.size(20.dp)) }
            },
            trailingIcon = trailingContent,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            minLines = minLines,
            shape = RoundedCornerShape(Radius.md),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = BrandRed,
                unfocusedBorderColor = BorderDefault,
                errorBorderColor     = RedDanger,
                focusedLabelColor    = BrandRed,
                unfocusedLabelColor  = TextSecondary,
                cursorColor          = BrandRed,
                focusedContainerColor   = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                disabledContainerColor  = SurfaceMuted,
            ),
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(Modifier.height(Spacing.xs))
            Text(
                errorMessage,
                color = RedDanger,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = Spacing.sm),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  SEARCH BAR
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar...",
    trailingContent: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(placeholder, color = TextTertiary) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        },
        trailingIcon = trailingContent,
        singleLine = true,
        shape = RoundedCornerShape(Radius.lg),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = BrandRed,
            unfocusedBorderColor    = BorderDefault,
            focusedContainerColor   = SurfaceWhite,
            unfocusedContainerColor = SurfaceWhite,
            cursorColor             = BrandRed,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  CARD SURFACE  (tarjeta genérica con sombra ligera)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosCard(
    modifier: Modifier = Modifier,
    elevation: Dp = Elevation.low,
    radius: Dp = Radius.lg,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier        = modifier,
        shape           = RoundedCornerShape(radius),
        color           = SurfaceWhite,
        shadowElevation = elevation,
        tonalElevation  = 0.dp,
        content         = content,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  CHIP DE FILTRO  (rojo cuando activo)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick  = onClick,
        label    = {
            Text(
                label,
                style      = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        },
        modifier = modifier,
        shape    = RoundedCornerShape(Radius.pill),
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandRed,
            selectedLabelColor     = Color.White,
            containerColor         = SurfaceMuted,
            labelColor             = TextSecondary,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled              = true,
            selected             = selected,
            selectedBorderColor  = BrandRed,
            borderColor          = BorderDefault,
            selectedBorderWidth  = 1.dp,
            borderWidth          = 1.dp,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  BADGE / PILL DE ESTADO
// ─────────────────────────────────────────────────────────────────────────────
enum class BadgeVariant { Default, Success, Warning, Error, Info, Muted }

@Composable
fun PosBadge(
    text: String,
    variant: BadgeVariant = BadgeVariant.Default,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) = when (variant) {
        BadgeVariant.Success -> GreenSuccessLight to GreenSuccess
        BadgeVariant.Warning -> OrangeWarningLight to OrangeWarning
        BadgeVariant.Error   -> RedDangerLight to RedDanger
        BadgeVariant.Info    -> BlueInfoLight to BlueInfo
        BadgeVariant.Muted   -> SurfaceMuted to TextSecondary
        BadgeVariant.Default -> BrandRedLight to BrandRed
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(bg)
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            color      = fg,
            style      = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  EMPTY STATE PROFESIONAL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosEmptyState(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier            = modifier.padding(Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(Radius.xxl))
                .background(SurfaceMuted),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint               = GrayMedium,
                modifier           = Modifier.size(40.dp),
            )
        }
        Spacer(Modifier.height(Spacing.lg))
        Text(
            title,
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
            textAlign  = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            description,
            style     = MaterialTheme.typography.bodyMedium,
            color     = TextSecondary,
            textAlign = TextAlign.Center,
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Spacing.xl))
            PrimaryButton(
                text    = actionLabel,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(0.6f),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  LOADING STATE
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosLoadingState(
    modifier: Modifier = Modifier,
    message: String = "Cargando...",
) {
    Column(
        modifier            = modifier.padding(Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            color       = BrandRed,
            strokeWidth = 3.dp,
            modifier    = Modifier.size(44.dp),
        )
        Spacer(Modifier.height(Spacing.lg))
        Text(
            message,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  LOADING BAR INLINE
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosLinearLoader(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier  = modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.pill)),
        color     = BrandRed,
        trackColor = BrandRedLight,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  SECTION HEADER (etiqueta + contador)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    count: Int? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                title,
                style      = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
            )
            if (count != null) {
                PosBadge("$count", BadgeVariant.Muted)
            }
        }
        trailingContent?.invoke()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  DIVIDER LIGERO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PosDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BorderDefault),
    )
}
