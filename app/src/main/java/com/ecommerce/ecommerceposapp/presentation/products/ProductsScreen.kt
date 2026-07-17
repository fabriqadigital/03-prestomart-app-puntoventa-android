package com.ecommerce.ecommerceposapp.presentation.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.copyPickedProductImage
import com.ecommerce.ecommerceposapp.presentation.common.parseDouble
import com.ecommerce.ecommerceposapp.presentation.products.ProductsViewModel

@Composable
fun ProductsCrudScreen(
    vm: ProductsViewModel,
    openCreateAdvanced: Boolean = false,
    onCreateAdvancedConsumed: () -> Unit = {},
) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    var editing by remember { mutableStateOf<ProductAdminRow?>(null) }
    var creatingAdvanced by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedSubcategoryId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(openCreateAdvanced) {
        if (openCreateAdvanced) {
            creatingAdvanced = true
            onCreateAdvancedConsumed()
        }
    }

    val activeCategories = state.categories.filter { it.active }
    val visibleSubcategories = state.subcategories.filter {
        it.active && it.categoryId == selectedCategoryId
    }
    val filteredProducts = state.products
        .filter { selectedCategoryId == null || it.categoryId == selectedCategoryId }
        .filter { selectedSubcategoryId == null || it.subcategoryId == selectedSubcategoryId }
        .filter {
            search.isBlank() ||
                it.name.contains(search, ignoreCase = true) ||
                it.code.contains(search, ignoreCase = true)
        }
    val compactScreen = LocalConfiguration.current.screenWidthDp < 600

    if (creatingAdvanced || editing != null) {
        ProductAdvancedEditorView(
            initial = editing ?: ProductAdminRow(
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
            onBack = {
                creatingAdvanced = false
                editing = null
                vm.clearMessages()
            },
            onSave = {
                vm.save(it)
                creatingAdvanced = false
                editing = null
            },
        )
        return
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        if (compactScreen) Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Column {
                Text("Productos y servicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Crea, edita y administra cada detalle de los productos que vendes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569),
                )
            }
            Button(
                onClick = { creatingAdvanced = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("+ Nuevo producto")
            }
        } else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Productos y servicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Crea, edita y administra cada detalle de los productos que vendes.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))
            }
            Button(onClick = { creatingAdvanced = true }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White)) {
                Text("+ Nuevo producto")
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(12.dp))
        ProductsTable(
            products = filteredProducts,
            search = search,
            onSearch = { search = it },
            categories = activeCategories,
            selectedCategoryId = selectedCategoryId,
            onCategory = {
                selectedCategoryId = it
                selectedSubcategoryId = null
            },
            subcategories = visibleSubcategories,
            selectedSubcategoryId = selectedSubcategoryId,
            onSubcategory = { selectedSubcategoryId = it },
            onEdit = { editing = it },
            onDelete = { product ->
                        pendingConfirm = PendingConfirm(
                            title = "Eliminar producto",
                            body = "¿Eliminar ${product.name} definitivamente? Esta acción también lo eliminará de la web.",
                            confirmButtonText = "Eliminar",
                            onConfirm = { vm.remove(product.id) },
                        )
            },
        )
    }
    ConfirmDestructiveDialog(pendingConfirm) { pendingConfirm = null }
}

@Composable
private fun ProductsTable(
    products: List<ProductAdminRow>,
    search: String,
    onSearch: (String) -> Unit,
    categories: List<CategoryAdminRow>,
    selectedCategoryId: Long?,
    onCategory: (Long?) -> Unit,
    subcategories: List<SubcategoryAdminRow>,
    selectedSubcategoryId: Long?,
    onSubcategory: (Long?) -> Unit,
    onEdit: (ProductAdminRow) -> Unit,
    onDelete: (ProductAdminRow) -> Unit,
) {
    val compact = LocalConfiguration.current.screenWidthDp < 760
    var pageSize by remember { mutableStateOf(10) }
    var pageSizeExpanded by remember { mutableStateOf(false) }
    val totalPages = maxOf(1, (products.size + pageSize - 1) / pageSize)
    var currentPage by remember(products.size, pageSize) { mutableStateOf(0) }
    val pageProducts = products.drop(currentPage * pageSize).take(pageSize)
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = Color(0xFF111827),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column {
            val searchField: @Composable (Modifier) -> Unit = { fieldModifier ->
                OutlinedTextField(
                    value = search,
                    onValueChange = onSearch,
                    placeholder = { Text("Buscar") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = fieldModifier,
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
            }
            val filterList: @Composable (Modifier) -> Unit = { listModifier ->
                LazyRow(modifier = listModifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryId == null,
                            onClick = { onCategory(null) },
                            label = { Text("Todos") },
                        )
                    }
                    items(categories, key = { it.id }) { category ->
                        FilterChip(
                            selected = selectedCategoryId == category.id,
                            onClick = { onCategory(category.id) },
                            label = { Text(category.name) },
                        )
                    }
                    if (subcategories.isNotEmpty()) {
                        item {
                            FilterChip(
                                selected = selectedSubcategoryId == null,
                                onClick = { onSubcategory(null) },
                                label = { Text("Todas") },
                            )
                        }
                        items(subcategories, key = { it.id }) { subcategory ->
                            FilterChip(
                                selected = selectedSubcategoryId == subcategory.id,
                                onClick = { onSubcategory(subcategory.id) },
                                label = { Text(subcategory.name) },
                            )
                        }
                    }
                }
            }
            if (compact) {
                Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    searchField(Modifier.fillMaxWidth())
                    filterList(Modifier.fillMaxWidth())
                }
            } else {
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    searchField(Modifier.widthIn(min = 260.dp, max = 360.dp))
                    filterList(Modifier.weight(1f))
                }
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Nombre", modifier = Modifier.weight(2f), fontWeight = FontWeight.SemiBold)
                if (!compact) Text("Código Producto", modifier = Modifier.weight(1.4f), fontWeight = FontWeight.SemiBold)
                Text("Precio público", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                if (!compact) Text("Stock", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(48.dp))
            }
            HorizontalDivider(color = Color(0xFFE2E8F0))
            LazyColumn(Modifier.fillMaxWidth().weight(1f).background(Color.White)) {
                items(pageProducts, key = { it.id }) { product ->
                    ProductTableRow(product = product, compact = compact, onEdit = onEdit, onDelete = onDelete)
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                }
            }
            val paginationInfo: @Composable () -> Unit = {
                val from = if (products.isEmpty()) 0 else currentPage * pageSize + 1
                val to = minOf(products.size, (currentPage + 1) * pageSize)
                Text(if (compact) "Filas:" else "Registros por pagina:", color = Color(0xFF475569))
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = Color(0xFF111827)) }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                        DropdownMenu(expanded = pageSizeExpanded, onDismissRequest = { pageSizeExpanded = false }) {
                            listOf(10, 20, 50).forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size.toString()) },
                                    onClick = { pageSize = size; pageSizeExpanded = false },
                                )
                            }
                        }
                    }
                }
                Text("$from-$to de ${products.size}", color = Color(0xFF475569))
            }
            val paginationButtons: @Composable () -> Unit = {
                IconButton(onClick = { currentPage-- }, enabled = currentPage > 0) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
                }
                IconButton(onClick = { currentPage++ }, enabled = currentPage < totalPages - 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
                }
            }
            if (compact) {
                Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { paginationInfo() }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                        Text("Pagina ${currentPage + 1} de $totalPages", color = Color(0xFF475569))
                        paginationButtons()
                    }
                }
            } else {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    paginationInfo()
                    Spacer(Modifier.weight(1f))
                    Text("Pagina ${currentPage + 1} de $totalPages", color = Color(0xFF475569))
                    paginationButtons()
                }
            }
        }
    }
}

@Composable
private fun ProductTableRow(
    product: ProductAdminRow,
    compact: Boolean,
    onEdit: (ProductAdminRow) -> Unit,
    onDelete: (ProductAdminRow) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(Color.White).clickable { onEdit(product) }.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(2f)) {
            Text(product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (compact) Text("Stock: ${product.stock.toInt()}", color = Color(0xFF475569), style = MaterialTheme.typography.labelSmall)
            if (product.syncState == "PENDING") {
                Text(
                    "Pendiente de sincronizar",
                    color = Color(0xFFE11D2E),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        if (!compact) Text(product.code.ifBlank { "-" }, modifier = Modifier.weight(1.4f), color = Color(0xFF475569))
        Text("S/ %.2f".format(product.price), modifier = Modifier.weight(1f), color = Color(0xFF0F172A))
        if (!compact) Text(product.stock.toInt().toString(), modifier = Modifier.weight(1f), color = Color(0xFF0F172A))
        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Opciones")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        menuExpanded = false
                        onEdit(product)
                    },
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        menuExpanded = false
                        onDelete(product)
                    },
                )
            }
        }
    }
}

@Composable
private fun ProductAdvancedEditorView(
    initial: ProductAdminRow,
    categories: List<CategoryAdminRow>,
    subcategories: List<SubcategoryAdminRow>,
    onBack: () -> Unit,
    onSave: (ProductAdminRow) -> Unit,
) {
    val context = LocalContext.current
    var name by remember(initial) { mutableStateOf(initial.name) }
    var code by remember(initial) { mutableStateOf(initial.code) }
    var barcode by remember(initial) { mutableStateOf(initial.barcode) }
    var imageUrl by remember(initial) { mutableStateOf(initial.imageUrl) }
    var categoryId by remember(initial) { mutableStateOf(initial.categoryId) }
    var subcategoryId by remember(initial) { mutableStateOf(initial.subcategoryId) }
    var selectedSubcategoryIds by remember(initial) {
        mutableStateOf(initial.subcategoryIds.ifEmpty { listOfNotNull(initial.subcategoryId.takeIf { it > 0L }) }.toSet())
    }
    var categoryExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }
    var price by remember(initial) { mutableStateOf(initial.price.toString()) }
    var stock by remember(initial) { mutableStateOf(initial.stock.toString()) }
    var costPrice by remember(initial) { mutableStateOf(initial.costPrice.toString()) }
    var oldPrice by remember(initial) { mutableStateOf(initial.oldPrice.toString()) }
    var wholesalePrice by remember(initial) { mutableStateOf(initial.wholesalePrice.toString()) }
    var wholesaleOldPrice by remember(initial) { mutableStateOf(initial.wholesaleOldPrice.toString()) }
    var yapePrice by remember(initial) { mutableStateOf(initial.yapePrice.toString()) }
    var minimumStock by remember(initial) { mutableStateOf(initial.minimumStock.toString()) }
    var description by remember(initial) { mutableStateOf(initial.description) }
    var location by remember(initial) { mutableStateOf(initial.location) }
    var weightKg by remember(initial) { mutableStateOf(initial.weightKg.toString()) }
    var packageMeasures by remember(initial) { mutableStateOf(initial.packageMeasures) }
    var packageDimension by remember(initial) { mutableStateOf(initial.packageDimension) }
    var promoCutoffTime by remember(initial) { mutableStateOf(initial.promoCutoffTime) }
    var saturdayCutoffTime by remember(initial) { mutableStateOf(initial.saturdayCutoffTime) }
    var offerMaxQuantity by remember(initial) { mutableStateOf(initial.offerMaxQuantity.toString()) }
    var offerMaxQuantityPrice by remember(initial) { mutableStateOf(initial.offerMaxQuantityPrice.toString()) }
    var ratingsEnabled by remember(initial) { mutableStateOf(initial.ratingsEnabled) }
    var adminRating by remember(initial) { mutableStateOf(initial.adminRating.toString()) }
    var metaTitle by remember(initial) { mutableStateOf(initial.metaTitle) }
    var metaDescription by remember(initial) { mutableStateOf(initial.metaDescription) }
    var salesChannel by remember(initial) { mutableStateOf(initial.salesChannel.ifBlank { "ambos" }) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    val activeCategories = categories.filter { it.active }
    val availableSubcategories = subcategories.filter { it.active && it.categoryId == categoryId }
    val selectedCategoryName = activeCategories.firstOrNull { it.id == categoryId }?.name ?: "Seleccionar"
    val selectedSubcategoryName = availableSubcategories.filter { it.id in selectedSubcategoryIds }
        .joinToString { it.name }.ifBlank { "Sin subcategoria" }
    val canSaveProduct = name.isNotBlank() && categoryId > 0L && parseDouble(price, 0.0) > 0.0 &&
        parseDouble(stock, -1.0) >= 0.0 && metaTitle.isNotBlank() && metaDescription.isNotBlank()
    val compactEditor = LocalConfiguration.current.screenWidthDp < 900
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imageUrl = copyPickedProductImage(context, uri)
    }
    val draftProduct = {
        initial.copy(
            categoryId = categoryId, subcategoryId = selectedSubcategoryIds.firstOrNull() ?: 0L,
            subcategoryIds = selectedSubcategoryIds.toList(), name = name.trim(), code = code.trim(),
            barcode = barcode.trim(), imageUrl = imageUrl, price = parseDouble(price, initial.price),
            stock = parseDouble(stock, initial.stock), costPrice = parseDouble(costPrice, initial.costPrice),
            oldPrice = parseDouble(oldPrice, initial.oldPrice), wholesalePrice = parseDouble(wholesalePrice, initial.wholesalePrice),
            wholesaleOldPrice = parseDouble(wholesaleOldPrice, initial.wholesaleOldPrice), yapePrice = parseDouble(yapePrice, initial.yapePrice),
            minimumStock = parseDouble(minimumStock, initial.minimumStock), description = description, location = location,
            weightKg = parseDouble(weightKg, initial.weightKg), packageMeasures = packageMeasures, packageDimension = packageDimension,
            promoCutoffTime = promoCutoffTime, saturdayCutoffTime = saturdayCutoffTime,
            offerMaxQuantity = parseDouble(offerMaxQuantity, initial.offerMaxQuantity),
            offerMaxQuantityPrice = parseDouble(offerMaxQuantityPrice, initial.offerMaxQuantityPrice),
            ratingsEnabled = ratingsEnabled, adminRating = parseDouble(adminRating, initial.adminRating),
            metaTitle = metaTitle, metaDescription = metaDescription, salesChannel = salesChannel, active = active,
        )
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("< Volver") }
            Text(
                if (initial.id == 0L) "Nuevo producto de venta" else "Editar producto",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(
                modifier = Modifier.weight(1f).fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
            ) {
                Column(Modifier.padding(18.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Canal de venta", fontWeight = FontWeight.Bold)
                    Text("¿Dónde se vende este producto?", color = Color(0xFF475569), fontWeight = FontWeight.SemiBold)
                    Text("Si elige Venta Física, el producto no aparecerá en la tienda online. Ambos o Venta Ecommerce lo muestran en la tienda.", color = Color(0xFF64748B))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SalesChannelChip("ambos", "Ambos", salesChannel) { salesChannel = it }
                        SalesChannelChip("fisica", "Venta física", salesChannel) { salesChannel = it }
                        SalesChannelChip("ecommerce", "Venta Ecommerce", salesChannel) { salesChannel = it }
                    }
                    Text("Información del producto", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 2, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(code, { code = it }, label = { Text("Código Producto") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(barcode, { barcode = it }, label = { Text("Código Barra") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                    OutlinedTextField(name, { name = it }, label = { Text("Nombre *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    FilterChip(ratingsEnabled, { ratingsEnabled = !ratingsEnabled }, { Text("Permitir calificación por usuarios") })
                    OutlinedTextField(description, { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

                    Text("Precios", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 3, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(oldPrice, { oldPrice = it }, label = { Text("Precio público anterior") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(price, { price = it }, label = { Text("Precio público *") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(wholesaleOldPrice, { wholesaleOldPrice = it }, label = { Text("Precio mayorista anterior") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(wholesalePrice, { wholesalePrice = it }, label = { Text("Precio mayorista") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(yapePrice, { yapePrice = it }, label = { Text("Precio Yape") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(offerMaxQuantity, { offerMaxQuantity = it }, label = { Text("Cantidad mínima (oferta por volumen)") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(offerMaxQuantityPrice, { offerMaxQuantityPrice = it }, label = { Text("Precio unitario desde esa cantidad") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true)
                    }

                    Text("Inventario y Categorías", fontWeight = FontWeight.Bold)
                    OutlinedTextField(stock, { stock = it }, label = { Text("Stock *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 2, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.weight(1f)) {
                            Column {
                                Text("Categoría *", color = Color(0xFF475569), fontWeight = FontWeight.Medium)
                                OutlinedButton(onClick = { categoryExpanded = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                                    Text(selectedCategoryName, modifier = Modifier.weight(1f)); Icon(Icons.Filled.KeyboardArrowDown, null)
                                }
                            }
                            DropdownMenu(categoryExpanded, { categoryExpanded = false }) {
                                activeCategories.forEach { item -> DropdownMenuItem({ Text(item.name) }, {
                                    categoryId = item.id; subcategoryId = 0L; selectedSubcategoryIds = emptySet(); categoryExpanded = false
                                }) }
                            }
                        }
                        Box(Modifier.weight(1f)) {
                            Column {
                                Text("Sub categoría", color = Color(0xFF475569), fontWeight = FontWeight.Medium)
                                OutlinedButton(onClick = { subcategoryExpanded = availableSubcategories.isNotEmpty() }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                                    Text(selectedSubcategoryName, modifier = Modifier.weight(1f)); Icon(Icons.Filled.KeyboardArrowDown, null)
                                }
                            }
                            DropdownMenu(subcategoryExpanded, { subcategoryExpanded = false }) {
                                DropdownMenuItem({ Text("Sin subcategoria") }, {
                                    subcategoryId = 0L; selectedSubcategoryIds = emptySet(); subcategoryExpanded = false
                                })
                                availableSubcategories.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(if (item.id in selectedSubcategoryIds) "✓ ${item.name}" else item.name) },
                                        onClick = {
                                            selectedSubcategoryIds = if (item.id in selectedSubcategoryIds) {
                                                selectedSubcategoryIds - item.id
                                            } else {
                                                selectedSubcategoryIds + item.id
                                            }
                                            subcategoryId = selectedSubcategoryIds.firstOrNull() ?: 0L
                                        },
                                    )
                                }
                            }
                        }
                    }
                    Text("Etiqueta", color = Color(0xFF475569), fontWeight = FontWeight.Medium)
                    Surface(color = Color(0xFFF8FAFC), shape = RoundedCornerShape(10.dp)) { Text("Home - Nuestros Productos", Modifier.fillMaxWidth().padding(16.dp)) }
                    FilterChip(active, { active = !active }, { Text("Estado: ${if (active) "ACTIVO" else "INACTIVO"}") })

                    Text("SEO descripción", fontWeight = FontWeight.Bold)
                    OutlinedTextField(metaTitle, { metaTitle = it }, label = { Text("Meta Titulo Producto *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(metaDescription, { metaDescription = it }, label = { Text("Meta Descripcion Producto *") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    if (compactEditor) {
                        OutlinedButton(onClick = { imagePicker.launch("image/*") }, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                            Icon(Icons.Filled.Image, contentDescription = null); Spacer(Modifier.width(8.dp)); Text("Seleccionar imagen")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f).height(52.dp)) { Text("Cancelar") }
                            Button(onClick = { onSave(draftProduct()) }, enabled = canSaveProduct, modifier = Modifier.weight(1f).height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505))) {
                                Text(if (initial.id == 0L) "Crear producto" else "Guardar")
                            }
                        }
                    }
                }
            }
            if (!compactEditor) Surface(modifier = Modifier.width(360.dp).fillMaxSize(), color = Color.White) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.fillMaxWidth().height(210.dp).background(Color(0xFFF8FAFC)).clickable { imagePicker.launch("image/*") }, contentAlignment = Alignment.Center) {
                        if (imageUrl.isBlank()) Icon(Icons.Filled.Image, contentDescription = "Seleccionar imagen", modifier = Modifier.size(44.dp))
                        else AsyncImage(imageUrl, contentDescription = name, modifier = Modifier.fillMaxSize())
                    }
                    Text(name.ifBlank { "Nombre del producto" }, style = MaterialTheme.typography.titleLarge)
                    Text("S/ %.2f".format(parseDouble(price, 0.0)), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)) { Text("Cancelar") }
                        Button(
                            onClick = {
                                onSave(
                                    initial.copy(
                                        categoryId = categoryId,
                                        subcategoryId = selectedSubcategoryIds.firstOrNull() ?: 0L,
                                        subcategoryIds = selectedSubcategoryIds.toList(),
                                        name = name,
                                        code = code,
                                        barcode = barcode,
                                        imageUrl = imageUrl,
                                        price = parseDouble(price, initial.price),
                                        stock = parseDouble(stock, initial.stock),
                                        costPrice = parseDouble(costPrice, initial.costPrice),
                                        oldPrice = parseDouble(oldPrice, initial.oldPrice),
                                        wholesalePrice = parseDouble(wholesalePrice, initial.wholesalePrice),
                                        wholesaleOldPrice = parseDouble(wholesaleOldPrice, initial.wholesaleOldPrice),
                                        yapePrice = parseDouble(yapePrice, initial.yapePrice),
                                        minimumStock = parseDouble(minimumStock, initial.minimumStock),
                                        description = description,
                                        location = location,
                                        weightKg = parseDouble(weightKg, initial.weightKg),
                                        packageMeasures = packageMeasures,
                                        packageDimension = packageDimension,
                                        promoCutoffTime = promoCutoffTime,
                                        saturdayCutoffTime = saturdayCutoffTime,
                                        offerMaxQuantity = parseDouble(offerMaxQuantity, initial.offerMaxQuantity),
                                        offerMaxQuantityPrice = parseDouble(offerMaxQuantityPrice, initial.offerMaxQuantityPrice),
                                        ratingsEnabled = ratingsEnabled,
                                        adminRating = parseDouble(adminRating, initial.adminRating),
                                        metaTitle = metaTitle,
                                        metaDescription = metaDescription,
                                        salesChannel = salesChannel,
                                        active = active,
                                    ),
                                )
                            },
                            modifier = Modifier.weight(1f),
                            enabled = canSaveProduct,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White),
                        ) { Text(if (initial.id == 0L) "Crear producto" else "Guardar") }
                    }
                }
            }
            }
        }
    }

@Composable
private fun RowScope.SalesChannelChip(
    value: String,
    label: String,
    selectedValue: String,
    onSelect: (String) -> Unit,
) {
    FilterChip(
        selected = selectedValue == value,
        onClick = { onSelect(value) },
        label = { Text(label) },
        modifier = Modifier.weight(1f),
    )
}
