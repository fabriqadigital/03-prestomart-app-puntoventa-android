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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
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

private val PosBrand = Color(0xFFfd0505)
private val PosBg = Color(0xFFFFFFFF)
private val PosTextPrimary = Color(0xFF111827)
private val PosTextSecondary = Color(0xFF64748B)

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
        modifier = modifier.animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1F2937),
        shadowElevation = 8.dp,
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("$completed", color = PosBrand, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Vende con MiniMarket POS", fontWeight = FontWeight.Bold, color = Color.White)
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
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    SetupStepRow(true, "Organiza tu inventario", "Agrega tu primer producto", onNewProduct)
                    SetupStepRow(hasCart, "Controla tu stock", "Agrega productos al carrito", { expanded = false })
                    SetupStepRow(false, "Vende rapido", "Cobra y vuelve a vender", { expanded = false })
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
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (done) Icons.Filled.Check else Icons.Filled.AddShoppingCart,
            contentDescription = null,
            tint = if (done) PosBrand else Color.White,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodySmall)
        }
        Text(">", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

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
            (state.search.isBlank() || it.name.contains(state.search, ignoreCase = true) ||
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

    Column(modifier = modifier.fillMaxSize().background(PosBg).padding(12.dp)) {
        val searchField: @Composable (Modifier) -> Unit = { fieldModifier ->
            OutlinedTextField(
                value = state.search,
                onValueChange = onSearch,
                placeholder = { Text(if (scanMode) "Código de barras" else "Buscar productos") },
                leadingIcon = {
                    Icon(
                        if (scanMode) Icons.Filled.QrCodeScanner else Icons.Filled.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = if (scanMode && state.search.isNotBlank()) {
                    {
                        IconButton(onClick = { onSearch("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Limpiar código")
                        }
                    }
                } else null,
                modifier = fieldModifier
                    .focusRequester(scanFocusRequester)
                    .onPreviewKeyEvent { event ->
                        if (scanMode && event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                            submitBarcode()
                            true
                        } else false
                    },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (scanMode) submitBarcode() }),
            )
        }
        val scanButton: @Composable (Modifier) -> Unit = { buttonModifier ->
            Surface(
                modifier = buttonModifier,
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 1.dp,
            ) {
                IconButton(
                    onClick = {
                        scanMode = !scanMode
                        onSearch("")
                    },
                    modifier = Modifier.size(56.dp),
                ) {
                    Icon(
                        if (scanMode) Icons.Filled.Close else Icons.Filled.QrCodeScanner,
                        contentDescription = if (scanMode) "Cerrar escaneo" else "Escanear código de barras",
                        tint = PosTextPrimary,
                    )
                }
            }
        }
        val newProductButton: @Composable (Modifier) -> Unit = { buttonModifier ->
            OutlinedButton(
                onClick = onNewProduct,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = PosTextPrimary),
                border = BorderStroke(1.dp, Color(0xFFD7DCE3)),
                modifier = buttonModifier.height(56.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Nuevo producto")
            }
        }
        if (compact) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                searchField(Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    scanButton(Modifier)
                    newProductButton(Modifier.weight(1f))
                }
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                searchField(Modifier.weight(1f))
                scanButton(Modifier)
                newProductButton(Modifier)
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
            item {
                FilterChip(
                    selected = state.selectedCategoryId == null,
                    onClick = { onCategory(null) },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PosBrand,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
            items(state.categories.size) { index ->
                val category = state.categories[index]
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = state.selectedCategoryId == category.id,
                    onClick = { onCategory(category.id) },
                    label = { Text(category.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PosBrand,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        if (visibleSubcategories.isNotEmpty()) {
            LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
                item {
                    FilterChip(
                        selected = state.selectedSubcategoryId == null,
                        onClick = { onSubcategory(null) },
                        label = { Text("Todas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PosBrand,
                            selectedLabelColor = Color.White,
                        ),
                    )
                }
                items(visibleSubcategories.size) { index ->
                    val subcategory = visibleSubcategories[index]
                    Spacer(Modifier.width(8.dp))
                    FilterChip(
                        selected = state.selectedSubcategoryId == subcategory.id,
                        onClick = { onSubcategory(subcategory.id) },
                        label = { Text(subcategory.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PosBrand,
                            selectedLabelColor = Color.White,
                        ),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = gridMinWidth),
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(products, key = { it.id }) { product ->
                ProductSaleCard(
                    product = product,
                    onAddToCart = onAddToCart,
                    onToggleFeatured = onToggleFeatured,
                )
            }
        }
    }
}

@Composable
private fun ProductSaleCard(
    product: ProductItem,
    onAddToCart: (ProductItem) -> Unit,
    onToggleFeatured: (ProductItem) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .clickable(enabled = product.stock > 0.0) { onAddToCart(product) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = PosTextPrimary,
            disabledContainerColor = Color.White,
            disabledContentColor = PosTextSecondary,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                if (product.stock <= 0.0) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).zIndex(2f),
                        shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp),
                        color = PosBrand,
                    ) {
                        Text("Sin stock", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 11.sp)
                    }
                } else {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).zIndex(2f),
                        shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                    ) {
                        IconButton(
                            onClick = { onToggleFeatured(product) },
                            modifier = Modifier.size(38.dp),
                        ) {
                            Icon(
                                imageVector = if (product.featuredInPos) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = if (product.featuredInPos) "Quitar de destacados" else "Destacar producto",
                                tint = if (product.featuredInPos) PosBrand else Color(0xFF64748B),
                                modifier = Modifier.size(21.dp),
                            )
                        }
                    }
                }
                if (product.imageUrl.isBlank()) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = Color(0xFFBDBDBD), modifier = Modifier.size(44.dp))
                } else {
                    var coilFailed by rememberSaveable(product.imageUrl) { mutableStateOf(false) }
                    if (coilFailed) {
                        Icon(Icons.Filled.Image, contentDescription = null, tint = Color(0xFFBDBDBD), modifier = Modifier.size(44.dp))
                    } else {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            onError = { coilFailed = true },
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(999.dp), color = Color(0xFFF3F4F6)) {
                Text(
                    "Inv. ${product.stock.toInt()}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = PosTextSecondary,
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = PosTextPrimary,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "S/ ${"%.2f".format(product.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = PosTextPrimary,
            )
        }
    }
}
