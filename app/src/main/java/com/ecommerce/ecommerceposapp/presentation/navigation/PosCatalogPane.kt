package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductItem
import com.ecommerce.ecommerceposapp.presentation.pos.PosUiState
import com.ecommerce.ecommerceposapp.ui.theme.AppBackground
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedDark
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedLight
import com.ecommerce.ecommerceposapp.ui.theme.BrandYellow
import com.ecommerce.ecommerceposapp.ui.theme.BrandYellowLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayMedium
import com.ecommerce.ecommerceposapp.ui.theme.PosEmptyState
import com.ecommerce.ecommerceposapp.ui.theme.PosFilterChip
import com.ecommerce.ecommerceposapp.ui.theme.Radius
import com.ecommerce.ecommerceposapp.ui.theme.Spacing
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceSubtle
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import com.ecommerce.ecommerceposapp.ui.theme.TextTertiary

// ─────────────────────────────────────────────────────────────────────────────
//  ONBOARDING STEPS  (sin cambios estructurales, solo ajuste de colores)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun PosOnboardingSteps(
    hasProducts: Boolean,
    hasCart: Boolean,
    onNewProduct: () -> Unit,
    onHide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val completed = listOf(hasProducts, hasCart).count { it }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        modifier        = modifier.animateContentSize(),
        shape           = RoundedCornerShape(Radius.lg),
        color           = Color(0xFF1F2937),
        shadowElevation = 8.dp,
    ) {
        Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("$completed", color = BrandRed, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(Spacing.md))
                Column(Modifier.weight(1f)) {
                    Text("Vende con PrestoMart POS", fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        "$completed / 3 pasos completados",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.86f),
                    )
                }
                Text(if (expanded) "^" else "v", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            if (expanded) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Radius.md))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    SetupStepRow(true, "Organiza tu inventario", "Agrega tu primer producto", onNewProduct)
                    SetupStepRow(hasCart, "Controla tu stock", "Agrega productos al carrito") { expanded = false }
                    SetupStepRow(false, "Vende rápido", "Cobra y vuelve a vender") { expanded = false }
                }
                TextButton(onClick = onHide, modifier = Modifier.align(Alignment.End)) {
                    Text("Ocultar pasos", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun SetupStepRow(
    done: Boolean,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.sm))
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (done) Icons.Filled.Check else Icons.Filled.AddShoppingCart,
            contentDescription = null,
            tint = if (done) BrandRed else Color.White,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(Spacing.md))
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodySmall)
        }
        Text(">", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CATALOG PANE
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun CatalogPane(
    modifier: Modifier,
    state: PosUiState,
    onSearch: (String) -> Unit,
    onCategory: (Long?) -> Unit,
    onSubcategory: (Long?) -> Unit,
    onAddToCart: (ProductItem) -> Unit,
    onToggleFeatured: (ProductItem) -> Unit,
    onNewProduct: () -> Unit,
) {
    var scanMode by rememberSaveable { mutableStateOf(false) }
    val scanFocusRequester = remember { FocusRequester() }

    fun submitBarcode() {
        val scannedCode = state.search.trim()
        if (scannedCode.isBlank()) return
        val product = state.products.firstOrNull {
            it.code.equals(scannedCode, ignoreCase = true) ||
                it.barcode.equals(scannedCode, ignoreCase = true)
        }
        if (product != null && product.stock > 0.0) {
            onAddToCart(product)
            onSearch("")
        }
    }

    LaunchedEffect(scanMode) {
        if (scanMode) scanFocusRequester.requestFocus()
    }

    val products = state.products.filter {
        (state.selectedCategoryId == null || it.categoryId == state.selectedCategoryId) &&
            (state.selectedSubcategoryId == null || it.subcategoryId == state.selectedSubcategoryId) &&
            (state.search.isBlank() ||
                it.name.contains(state.search, ignoreCase = true) ||
                (it.code.isNotBlank() && it.code.contains(state.search, ignoreCase = true)) ||
                (it.barcode.isNotBlank() && it.barcode.contains(state.search, ignoreCase = true)))
    }

    val visibleSubcategories = state.subcategories.filter { it.categoryId == state.selectedCategoryId }
    val compact = LocalConfiguration.current.screenWidthDp < 600
    val gridMinWidth = when {
        LocalConfiguration.current.screenWidthDp >= 900 -> 178.dp
        LocalConfiguration.current.screenWidthDp >= 600 -> 164.dp
        else -> 150.dp
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(Spacing.md),
    ) {
        // ── Barra de búsqueda + botones ──────────────────────────────────────
        val searchField: @Composable (Modifier) -> Unit = { fieldMod ->
            OutlinedTextField(
                value       = state.search,
                onValueChange = onSearch,
                placeholder = {
                    Text(
                        if (scanMode) "Escanear código de barras..." else "Buscar producto o código...",
                        color = TextTertiary,
                    )
                },
                leadingIcon = {
                    Icon(
                        if (scanMode) Icons.Filled.QrCodeScanner else Icons.Filled.Search,
                        contentDescription = null,
                        tint = if (scanMode) BrandRed else TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = if (state.search.isNotBlank()) {
                    {
                        IconButton(onClick = { onSearch("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Limpiar", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                } else null,
                modifier = fieldMod
                    .focusRequester(scanFocusRequester)
                    .onPreviewKeyEvent { event ->
                        if (scanMode && event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                            submitBarcode(); true
                        } else false
                    },
                shape = RoundedCornerShape(Radius.lg),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (scanMode) submitBarcode() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = BrandRed,
                    unfocusedBorderColor    = BorderDefault,
                    focusedContainerColor   = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                    cursorColor             = BrandRed,
                ),
            )
        }

        val scanButton: @Composable (Modifier) -> Unit = { btnMod ->
            Surface(
                modifier        = btnMod,
                shape           = RoundedCornerShape(Radius.md),
                color           = if (scanMode) BrandRedLight else SurfaceWhite,
                shadowElevation = 1.dp,
            ) {
                IconButton(
                    onClick = { scanMode = !scanMode; onSearch("") },
                    modifier = Modifier.size(50.dp),
                ) {
                    Icon(
                        if (scanMode) Icons.Filled.Close else Icons.Filled.QrCodeScanner,
                        contentDescription = if (scanMode) "Cerrar escaneo" else "Escanear código",
                        tint = if (scanMode) BrandRed else TextSecondary,
                    )
                }
            }
        }

        val newProductButton: @Composable (Modifier) -> Unit = { btnMod ->
            OutlinedButton(
                onClick = onNewProduct,
                shape = RoundedCornerShape(Radius.md),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfaceWhite,
                    contentColor   = TextPrimary,
                ),
                border   = BorderStroke(1.dp, BorderDefault),
                modifier = btnMod.height(50.dp),
                contentPadding = PaddingValues(horizontal = Spacing.md),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(Spacing.xs))
                Text("Nuevo", fontWeight = FontWeight.SemiBold)
            }
        }

        if (compact) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                searchField(Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    scanButton(Modifier)
                    newProductButton(Modifier.weight(1f))
                }
            }
        } else {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                searchField(Modifier.weight(1f))
                scanButton(Modifier)
                newProductButton(Modifier)
            }
        }

        Spacer(Modifier.height(Spacing.sm))

        // ── Chips de categoría ───────────────────────────────────────────────
        LazyRow(
            contentPadding     = PaddingValues(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            item {
                PosFilterChip(
                    label    = "Todos",
                    selected = state.selectedCategoryId == null,
                    onClick  = { onCategory(null) },
                )
            }
            items(state.categories.size) { i ->
                val cat = state.categories[i]
                PosFilterChip(
                    label    = cat.name,
                    selected = state.selectedCategoryId == cat.id,
                    onClick  = { onCategory(cat.id) },
                )
            }
        }

        // ── Chips de subcategoría ────────────────────────────────────────────
        if (visibleSubcategories.isNotEmpty()) {
            Spacer(Modifier.height(Spacing.sm))
            LazyRow(
                contentPadding     = PaddingValues(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                item {
                    PosFilterChip(
                        label    = "Todas",
                        selected = state.selectedSubcategoryId == null,
                        onClick  = { onSubcategory(null) },
                    )
                }
                items(visibleSubcategories.size) { i ->
                    val sub = visibleSubcategories[i]
                    PosFilterChip(
                        label    = sub.name,
                        selected = state.selectedSubcategoryId == sub.id,
                        onClick  = { onSubcategory(sub.id) },
                    )
                }
            }
        }

        Spacer(Modifier.height(Spacing.md))

        // ── Grid de productos ────────────────────────────────────────────────
        if (products.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                PosEmptyState(
                    icon        = Icons.Filled.Inventory2,
                    title       = if (state.search.isNotBlank()) "Sin resultados" else "Sin productos",
                    description = if (state.search.isNotBlank())
                        "No encontramos productos para \"${state.search}\".\nRevisa el término o cambia el filtro."
                    else
                        "Aún no tienes productos en este catálogo.\nAgrega uno para comenzar a vender.",
                    actionLabel = if (state.search.isBlank()) "Agregar producto" else null,
                    onAction    = if (state.search.isBlank()) onNewProduct else null,
                )
            }
        } else {
            LazyVerticalGrid(
                columns  = GridCells.Adaptive(minSize = gridMinWidth),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding     = PaddingValues(Spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalArrangement   = Arrangement.spacedBy(Spacing.md),
            ) {
                items(products, key = { it.id }) { product ->
                    ProductSaleCard(
                        product          = product,
                        onAddToCart      = onAddToCart,
                        onToggleFeatured = onToggleFeatured,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  PRODUCT SALE CARD  — rediseñada
//  • Sin fondo coloreado detrás de la estrella
//  • Bordes redondeados, sombra ligera
//  • Espaciado interno consistente
//  • Precio en rojo de marca, nombre centrado
//  • Badge "Sin stock" en rojo como ribbon
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProductSaleCard(
    product: ProductItem,
    onAddToCart: (ProductItem) -> Unit,
    onToggleFeatured: (ProductItem) -> Unit,
) {
    val outOfStock = product.stock <= 0.0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .shadow(
                elevation = if (outOfStock) 0.dp else 2.dp,
                shape     = RoundedCornerShape(Radius.lg),
                ambientColor  = Color(0x14000000),
                spotColor     = Color(0x14000000),
            )
            .clip(RoundedCornerShape(Radius.lg))
            .background(SurfaceWhite)
            .clickable(enabled = !outOfStock) { onAddToCart(product) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Imagen + acciones superpuestas ───────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(Radius.md))
                    .background(SurfaceSubtle),
                contentAlignment = Alignment.Center,
            ) {
                // Imagen / placeholder
                if (product.imageUrl.isBlank()) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = null,
                        tint     = GrayLight,
                        modifier = Modifier.size(40.dp),
                    )
                } else {
                    var coilFailed by rememberSaveable(product.imageUrl) { mutableStateOf(false) }
                    if (coilFailed) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = null,
                            tint     = GrayLight,
                            modifier = Modifier.size(40.dp),
                        )
                    } else {
                        AsyncImage(
                            model              = product.imageUrl,
                            contentDescription = product.name,
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Fit,
                            onError            = { coilFailed = true },
                        )
                    }
                }

                // ── Estrella — SIN fondo coloreado ───────────────────────────
                if (!outOfStock) {
                    IconButton(
                        onClick  = { onToggleFeatured(product) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .zIndex(2f)
                            .size(36.dp),
                    ) {
                        Icon(
                            imageVector = if (product.featuredInPos) Icons.Filled.Star
                                          else Icons.Outlined.StarBorder,
                            contentDescription = if (product.featuredInPos)
                                "Quitar de destacados" else "Destacar producto",
                            tint     = if (product.featuredInPos) BrandRed else GrayMedium,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                // ── Badge "Sin stock" ─────────────────────────────────────────
                if (outOfStock) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .zIndex(2f)
                            .clip(RoundedCornerShape(bottomEnd = Radius.md))
                            .background(BrandRed)
                            .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    ) {
                        Text(
                            "Sin stock",
                            color  = Color.White,
                            style  = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            Spacer(Modifier.height(Spacing.sm))

            // ── Inventario pill ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(if (outOfStock) BrandRedLight else SurfaceMuted)
                    .padding(horizontal = Spacing.sm, vertical = 2.dp),
            ) {
                Text(
                    "Stock: ${product.stock.toInt()}",
                    style  = MaterialTheme.typography.labelSmall,
                    color  = if (outOfStock) BrandRed else TextSecondary,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(Modifier.height(Spacing.xs))

            // ── Nombre ───────────────────────────────────────────────────────
            Text(
                text       = product.name,
                style      = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                maxLines   = 2,
                minLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                textAlign  = TextAlign.Center,
                color      = if (outOfStock) TextSecondary else TextPrimary,
                modifier   = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(Spacing.xs))

            // ── Precio ───────────────────────────────────────────────────────
            Text(
                text       = "S/ ${"%.2f".format(product.price)}",
                style      = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color      = if (outOfStock) GrayMedium else BrandRed,
                textAlign  = TextAlign.Center,
            )
        }
    }
}
