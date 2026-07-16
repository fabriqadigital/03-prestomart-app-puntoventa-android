package com.ecommerce.ecommerceposapp.presentation.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.CrudEditDeleteIcons
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.ToolbarAddIconButton
import com.ecommerce.ecommerceposapp.presentation.common.copyPickedProductImage
import com.ecommerce.ecommerceposapp.presentation.common.FieldBorderColor
import com.ecommerce.ecommerceposapp.presentation.common.FieldTextColor
import com.ecommerce.ecommerceposapp.presentation.common.parseDouble
import com.ecommerce.ecommerceposapp.presentation.products.ProductsViewModel

@Composable
fun ProductsCrudScreen(vm: ProductsViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    var editing by remember { mutableStateOf<ProductAdminRow?>(null) }
    var creating by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedSubcategoryId by remember { mutableStateOf<Long?>(null) }

    val activeCategories = state.categories.filter { it.active }
    val visibleSubcategories = state.subcategories.filter {
        it.active && it.categoryId == selectedCategoryId
    }
    val filteredProducts = state.products
        .filter { it.active }
        .filter { selectedCategoryId == null || it.categoryId == selectedCategoryId }
        .filter { selectedSubcategoryId == null || it.subcategoryId == selectedSubcategoryId }
        .filter {
            search.isBlank() ||
                it.name.contains(search, ignoreCase = true) ||
                it.code.contains(search, ignoreCase = true)
        }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Productos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton({ creating = true }, "Nuevo producto")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar producto") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
            item {
                FilterChip(
                    selected = selectedCategoryId == null,
                    onClick = {
                        selectedCategoryId = null
                        selectedSubcategoryId = null
                    },
                    label = { Text("Todos") },
                )
            }
            items(activeCategories, key = { it.id }) { category ->
                Spacer(Modifier.padding(horizontal = 4.dp))
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = {
                        selectedCategoryId = category.id
                        selectedSubcategoryId = null
                    },
                    label = { Text(category.name) },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        if (visibleSubcategories.isNotEmpty()) {
            LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
                item {
                    FilterChip(
                        selected = selectedSubcategoryId == null,
                        onClick = { selectedSubcategoryId = null },
                        label = { Text("Todas") },
                    )
                }
                items(visibleSubcategories, key = { it.id }) { subcategory ->
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    FilterChip(
                        selected = selectedSubcategoryId == subcategory.id,
                        onClick = { selectedSubcategoryId = subcategory.id },
                        label = { Text(subcategory.name) },
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 170.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(filteredProducts, key = { it.id }) { product ->
                ProductCrudCard(
                    product = product,
                    onEdit = { editing = product },
                    onDelete = {
                        pendingConfirm = PendingConfirm(
                            title = "Desactivar producto",
                            body = "Desactivar " + product.name + "? Dejara de mostrarse para la venta.",
                            confirmButtonText = "Desactivar",
                            onConfirm = { vm.remove(product.id) },
                        )
                    },
                )
            }
        }
    }

    if (creating) {
        ProductEditDialog(
            initial = ProductAdminRow(
                id = 0,
                categoryId = activeCategories.firstOrNull()?.id ?: 0L,
                subcategoryId = 0,
                name = "",
                code = "",
                imageUrl = "",
                price = 0.0,
                stock = 0.0,
                active = true,
            ),
            categories = state.categories,
            subcategories = state.subcategories,
            onDismiss = { creating = false; vm.clearMessages() },
            onSave = { vm.save(it); creating = false },
        )
    }
    editing?.let { product ->
        ProductEditDialog(
            initial = product,
            categories = state.categories,
            subcategories = state.subcategories,
            onDismiss = { editing = null; vm.clearMessages() },
            onSave = { vm.save(it); editing = null },
        )
    }
    ConfirmDestructiveDialog(pendingConfirm) { pendingConfirm = null }
}

@Composable
private fun ProductCrudCard(
    product: ProductAdminRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val primaryBlue = Color(0xFFfd0505)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (product.imageUrl.isBlank()) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                        )
                    } else {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
            Text(
                text = product.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "S/ ${"%.2f".format(product.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = primaryBlue,
            )
            Spacer(Modifier.height(2.dp))
            Text("Stock: ${product.stock.toInt()}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar producto",
                        tint = Color(0xFF333333),
                    )
                }
                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar producto",
                        tint = MaterialTheme.colorScheme.onError,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductEditDialog(
    initial: ProductAdminRow,
    categories: List<CategoryAdminRow>,
    subcategories: List<SubcategoryAdminRow>,
    onDismiss: () -> Unit,
    onSave: (ProductAdminRow) -> Unit,
) {
    val context = LocalContext.current
    var name by remember(initial) { mutableStateOf(initial.name) }
    var code by remember(initial) { mutableStateOf(initial.code) }
    var imageUrl by remember(initial) { mutableStateOf(initial.imageUrl) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imageUrl = copyPickedProductImage(context, uri)
    }
    var categoryId by remember(initial) { mutableStateOf(initial.categoryId) }
    var subcategoryId by remember(initial) { mutableStateOf(initial.subcategoryId) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }
    var price by remember(initial) { mutableStateOf(initial.price.toString()) }
    var stock by remember(initial) { mutableStateOf(initial.stock.toString()) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    val activeCategories = categories.filter { it.active }
    val availableSubcategories = subcategories.filter { it.active && it.categoryId == categoryId }
    val selectedCategoryName = activeCategories.firstOrNull { it.id == categoryId }?.name ?: "Seleccione categoria"
    val selectedSubcategoryName = availableSubcategories.firstOrNull { it.id == subcategoryId }?.name ?: "Sin subcategoria"
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nuevo producto" else "Editar producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Codigo") }, modifier = Modifier.fillMaxWidth())
                OutlinedButton(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, FieldBorderColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FieldTextColor),
                ) {
                    Icon(Icons.Filled.Image, contentDescription = null)
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text(if (imageUrl.isBlank()) "Subir imagen" else "Cambiar imagen")
                }
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                }
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { categoryExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, FieldBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FieldTextColor),
                    ) {
                        Text(selectedCategoryName, modifier = Modifier.weight(1f))
                    }
                    DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                        activeCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    categoryId = category.id
                                    subcategoryId = 0L
                                    categoryExpanded = false
                                },
                            )
                        }
                    }
                }
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { subcategoryExpanded = availableSubcategories.isNotEmpty() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, FieldBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FieldTextColor),
                    ) {
                        Text(selectedSubcategoryName, modifier = Modifier.weight(1f))
                    }
                    DropdownMenu(expanded = subcategoryExpanded, onDismissRequest = { subcategoryExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Sin subcategoria") },
                            onClick = {
                                subcategoryId = 0L
                                subcategoryExpanded = false
                            },
                        )
                        availableSubcategories.forEach { subcategory ->
                            DropdownMenuItem(
                                text = { Text(subcategory.name) },
                                onClick = {
                                    subcategoryId = subcategory.id
                                    subcategoryExpanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
                Row { FilterChip(selected = active, onClick = { active = !active }, label = { Text("Activo") }) }
            }
        },
        confirmButton = {
            IconButton(onClick = {
                onSave(
                    ProductAdminRow(
                        id = initial.id,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        name = name,
                        code = code,
                        imageUrl = imageUrl,
                        price = parseDouble(price, initial.price),
                        stock = parseDouble(stock, initial.stock),
                        active = active,
                    ),
                )
            }) { Icon(Icons.Filled.Check, contentDescription = "Guardar") }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}
