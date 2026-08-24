package com.ecommerce.ecommerceposapp.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed

/**
 * Indicador de pull-to-refresh de marca:
 * - Spinner rojo con animación de escala proporcional al gesto
 * - Punto rojo central como acento
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.AppPullRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
) {
    val scale by animateFloatAsState(
        targetValue = if (isRefreshing) 1f else state.distanceFraction.coerceIn(0f, 1f),
        label = "pullRefreshScale",
    )

    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .pullToRefreshIndicator(
                state          = state,
                isRefreshing   = isRefreshing,
                // Fondo blanco, sin elevación para evitar sombra negra
                containerColor = Color.White,
                shape          = CircleShape,
                elevation      = 0.dp,
            )
            .size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Spinner de marca
        CircularProgressIndicator(
            modifier    = Modifier
                .size(26.dp)
                .scale(scale),
            color       = BrandRed,
            strokeWidth = 2.5.dp,
            trackColor  = Color.Transparent,
        )
        // Punto rojo central
        Box(
            modifier = Modifier
                .size(5.dp)
                .scale(scale)
                .background(BrandRed, CircleShape),
        )
    }
}
