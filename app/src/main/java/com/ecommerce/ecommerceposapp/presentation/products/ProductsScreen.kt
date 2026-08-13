package com.ecommerce.ecommerceposapp.presentation.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductTypeRow
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductConversion
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.copyPickedProductImage
import com.ecommerce.ecommerceposapp.presentation.common.parseDouble
import com.ecommerce.ecommerceposapp.presentation.products.ProductsViewModel
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.ecommerce.ecommerceposapp.presentation.pos.CameraScannerDialog
import com.ecommerce.ecommerceposapp.util.DataWedgeScanner
import com.ecommerce.ecommerceposapp.util.PhysicalScannerInput
import com.ecommerce.ecommerceposapp.util.rememberPhysicalScannerConnected
import kotlinx.coroutines.delay

/**
 * Resuelve la longitud correcta de un código de barra recibido por DataWedge.
 *
 * `DataWedgeScanner.extractBarcode()` restaura el 0 inicial de TODO símbolo UPC-A
 * (12 dígitos) como si fuera un EAN-13 truncado, pero un UPC-A legítimo (p. ej.
 * "027084120134") debe conservar sus 12 dígitos. Se usa el catálogo local como
 * referencia: si solo el EAN-13 de 13 dígitos existe → es EAN-13; en cualquier otro
 * caso (el UPC-A existe, o ninguno existe — producto nuevo) → se conservan los
 * 12 dígitos que DataWedge decodificó del símbolo.
 */
private fun resolveDataWedgeBarcode(code: String, known: List<ProductAdminRow>): String {
    val trimmed = code.trim()
    if (trimmed.length != 13 || !trimmed.startsWith("0")) return trimmed
    val upcaCandidate = trimmed.substring(1)
    val ean13Known = known.any { it.barcode == trimmed || it.code == trimmed }
    val upcaKnown = known.any { it.barcode == upcaCandidate || it.code == upcaCandidate }
    if (ean13Known && !upcaKnown) return trimmed
    return upcaCandidate
}

@Composable
fun ProductsCrudScreen(
    vm: ProductsViewModel,
    openCreateAdvanced: Boolean = false,
    initialCategoryId: Long? = null,
    initialSubcategoryId: Long? = null,
    onCreateAdvancedConsumed: () -> Unit = {},
) {
    val state by vm.uiState.collectAsState()
    var editing by remember { mutableStateOf<ProductAdminRow?>(null) }
    var creatingAdvanced by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedSubcategoryId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(search, selectedCategoryId, selectedSubcategoryId) {
        delay(350)
        vm.load(page = 1, search = search, categoryId = selectedCategoryId, subcategoryId = selectedSubcategoryId)
    }

    LaunchedEffect(openCreateAdvanced) {
        if (openCreateAdvanced) {
            creatingAdvanced = true
            onCreateAdvancedConsumed()
        }
    }


    val context = LocalContext.current
    var dataWedgeSequence by remember { mutableStateOf(0) }
    var dataWedgeCode by remember { mutableStateOf("") }
    var lastDataWedgeCode by remember { mutableStateOf("") }
    var lastDataWedgeTime by remember { mutableStateOf(0L) }
    var editorDataWedgeScan by remember { mutableStateOf<DataWedgeScanner.DataWedgeScan?>(null) }
    var editorPhysicalScan by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {
        val filter = IntentFilter(DataWedgeScanner.SCAN_ACTION).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                Log.d(
                    "DataWedge",
                    "Productos: broadcast recibido action=${intent?.action} extras=${intent?.extras?.keySet()}",
                )
                val code = DataWedgeScanner.extractBarcode(intent)
                if (code == null) {
                    Log.d("DataWedge", "Productos: broadcast ignorado (no es SCAN o falta data_string).")
                    return
                }
                Log.d("DataWedge", "Productos: Barcode recibido desde DataWedge: $code")
                Log.d("BarcodeDebug", "DataWedge (Productos) recibido: [$code] (length=${code.length})")
                val now = SystemClock.elapsedRealtime()
                if (code == lastDataWedgeCode && now - lastDataWedgeTime < DataWedgeScanner.DATAWEDGE_DEDUP_MS) {
                    Log.d("DataWedge", "Productos: escaneo duplicado ignorado (ventana 2s): $code")
                    return
                }
                lastDataWedgeCode = code
                lastDataWedgeTime = now
                dataWedgeCode = code
                dataWedgeSequence += 1
            }
        }
        Log.d("DataWedge", "Productos: registrando receiver para acción=${DataWedgeScanner.SCAN_ACTION}")
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        onDispose {
            context.unregisterReceiver(receiver)
            Log.d("DataWedge", "Productos: receiver desregistrado")
        }
    }

    val externalScan = if (dataWedgeSequence > 0) {
        DataWedgeScanner.DataWedgeScan(dataWedgeSequence, dataWedgeCode)
    } else {
        null
    }

    LaunchedEffect(externalScan) {
        val scan = externalScan ?: return@LaunchedEffect
        // DataWedge restaura el 0 inicial de todo UPC-A (12 dígitos); se resuelve con el
        // catálogo para que el campo Código Barra guarde el código tal cual (12 o 13).
        val finalCode = resolveDataWedgeBarcode(scan.code, state.products)
        if (finalCode != scan.code) {
            Log.d("BarcodeDebug", "Productos: DataWedge UPC-A resuelto con catálogo: [${scan.code}] → [$finalCode]")
        }
        if (creatingAdvanced || editing != null) {
            Log.d("DataWedge", "Productos: escaneo enrutado al formulario: $finalCode (seq=${scan.sequence})")
            Log.d("BarcodeDebug", "Productos: escaneo al formulario: [$finalCode] (length=${finalCode.length})")
            editorDataWedgeScan = scan.copy(code = finalCode)
        } else {
            Log.d("DataWedge", "Productos: escaneo al buscador: $finalCode (seq=${scan.sequence})")
            Log.d("BarcodeDebug", "Productos: escaneo al buscador: [$finalCode] (length=${finalCode.length})")
            search = finalCode
        }
    }

    LaunchedEffect(Unit) {
        PhysicalScannerInput.scans.collect { rawCode ->
            val knownCodes = state.products.flatMap { listOfNotNull(it.barcode, it.code) }
            val normalized = PhysicalScannerInput.normalizeBarcode(rawCode, knownCodes)
            Log.d("PhysicalScanner", "SCANNER PROCESSED: $normalized")
            Log.d("PhysicalScanner", "SCANNER MODE: PHYSICAL")
            if (creatingAdvanced || editing != null) {
                Log.d("PhysicalScanner", "TARGET: ${if (creatingAdvanced) "CREATE" else "EDIT"}")
                editorPhysicalScan = normalized
            } else {
                Log.d("PhysicalScanner", "TARGET: FILTER")
                search = normalized
            }
        }
    }

    val activeCategories = state.categories.filter { it.active }
    val visibleSubcategories = state.subcategories.filter {
        it.active && it.categoryId == selectedCategoryId
    }
    val compactScreen = LocalConfiguration.current.screenWidthDp < 600

    if (creatingAdvanced || editing != null) {
        ProductAdvancedEditorView(
            initial = editing ?: ProductAdminRow(
                id = 0,
                categoryId = initialCategoryId?.takeIf { id -> activeCategories.any { it.id == id } }
                    ?: selectedCategoryId
                    ?: activeCategories.firstOrNull()?.id
                    ?: 0L,
                subcategoryId = initialSubcategoryId?.takeIf { id ->
                    state.subcategories.any { it.id == id && it.categoryId == initialCategoryId }
                } ?: selectedSubcategoryId ?: 0L,
                name = "",
                code = "",
                imageUrl = "",
                price = 0.0,
                stock = 0.0,
                active = true,
            ),
            categories = state.categories,
            subcategories = state.subcategories,
            productTypes = state.productTypes,
            products = state.products,
            externalScan = editorDataWedgeScan,
            physicalScan = editorPhysicalScan,
            onBack = {
                creatingAdvanced = false
                editing = null
                editorDataWedgeScan = null
                editorPhysicalScan = null
                vm.clearMessages()
            },
            onSave = {
                vm.save(it)
                creatingAdvanced = false
                editing = null
                editorDataWedgeScan = null
                editorPhysicalScan = null
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
            products = state.products,
            total = state.total,
            currentPage = state.page,
            pageSize = state.perPage,
            onPage = { vm.load(page = it) },
            onPageSize = { vm.load(page = 1, perPage = it) },
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
    total: Int,
    currentPage: Int,
    pageSize: Int,
    onPage: (Int) -> Unit,
    onPageSize: (Int) -> Unit,
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
    var pageSizeExpanded by remember { mutableStateOf(false) }
    val totalPages = maxOf(1, (total + pageSize - 1) / pageSize)
    val searchFocusRequester = remember { FocusRequester() }
    var showSearchCameraScanner by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val physicalScannerConnected by rememberPhysicalScannerConnected(context)

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
                    trailingIcon = if (search.isNotBlank()) {
                        {
                            IconButton(onClick = { onSearch("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Limpiar búsqueda", tint = TextSecondary)
                            }
                        }
                    } else null,
                    modifier = fieldModifier,
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
            }

            val scanSearchButton: @Composable (Modifier) -> Unit = { btnMod ->
                Surface(
                    modifier        = btnMod,
                    shape           = RoundedCornerShape(10.dp),
                    color           = Color.White,
                    border          = androidx.compose.foundation.BorderStroke(1.dp, BorderDefault),
                ) {
                    IconButton(
                        onClick = {
                            if (!physicalScannerConnected) showSearchCameraScanner = true
                        },
                        modifier = Modifier.size(50.dp),
                    ) {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "Filtrar por código escaneado", tint = TextSecondary)
                    }
                }
            }

            if (showSearchCameraScanner) {
                CameraScannerDialog(
                    onBarcodeDetected = { code ->
                        onSearch(code)
                        showSearchCameraScanner = false
                    },
                    onDismiss = { showSearchCameraScanner = false },
                )
            }
            val filterList: @Composable (Modifier) -> Unit = { listModifier ->
                LazyRow(modifier = listModifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryId == null,
                            onClick = { onCategory(null) },
                            label = { Text("Todos") },
                            colors = productFilterChipColors(),
                            border = productFilterChipBorder(selectedCategoryId == null),
                        )
                    }
                    items(categories, key = { it.id }) { category ->
                        FilterChip(
                            selected = selectedCategoryId == category.id,
                            onClick = { onCategory(category.id) },
                            label = { Text(category.name) },
                            colors = productFilterChipColors(),
                            border = productFilterChipBorder(selectedCategoryId == category.id),
                        )
                    }
                    if (subcategories.isNotEmpty()) {
                        item {
                            FilterChip(
                                selected = selectedSubcategoryId == null,
                                onClick = { onSubcategory(null) },
                                label = { Text("Todas") },
                                colors = productFilterChipColors(),
                                border = productFilterChipBorder(selectedSubcategoryId == null),
                            )
                        }
                        items(subcategories, key = { it.id }) { subcategory ->
                            FilterChip(
                                selected = selectedSubcategoryId == subcategory.id,
                                onClick = { onSubcategory(subcategory.id) },
                                label = { Text(subcategory.name) },
                                colors = productFilterChipColors(),
                                border = productFilterChipBorder(selectedSubcategoryId == subcategory.id),
                            )
                        }
                    }
                }
            }
            if (compact) {
                Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        searchField(Modifier.weight(1f).focusRequester(searchFocusRequester))
                        scanSearchButton(Modifier)
                    }
                    filterList(Modifier.fillMaxWidth())
                }
            } else {
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    searchField(Modifier.widthIn(min = 260.dp, max = 360.dp).focusRequester(searchFocusRequester))
                    scanSearchButton(Modifier)
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
                items(products, key = { it.id }) { product ->
                    ProductTableRow(product = product, compact = compact, onEdit = onEdit, onDelete = onDelete)
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                }
            }
            val paginationInfo: @Composable () -> Unit = {
                val from = if (products.isEmpty()) 0 else (currentPage - 1) * pageSize + 1
                val to = minOf(total, currentPage * pageSize)
                Text(if (compact) "Filas:" else "Registros por pagina:", color = Color(0xFF475569))
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = Color(0xFF111827)) }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                        DropdownMenu(expanded = pageSizeExpanded, onDismissRequest = { pageSizeExpanded = false }) {
                            listOf(20, 50, 100).forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size.toString()) },
                                    onClick = { onPageSize(size); pageSizeExpanded = false },
                                )
                            }
                        }
                    }
                }
                Text("$from-$to de $total", color = Color(0xFF475569))
            }
            val paginationButtons: @Composable () -> Unit = {
                IconButton(onClick = { onPage(currentPage - 1) }, enabled = currentPage > 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
                }
                IconButton(onClick = { onPage(currentPage + 1) }, enabled = currentPage < totalPages) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
                }
            }
            if (compact) {
                Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { paginationInfo() }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                        Text("Pagina $currentPage de $totalPages", color = Color(0xFF475569))
                        paginationButtons()
                    }
                }
            } else {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    paginationInfo()
                    Spacer(Modifier.weight(1f))
                    Text("Pagina $currentPage de $totalPages", color = Color(0xFF475569))
                    paginationButtons()
                }
            }
        }
    }
}

@Composable
private fun productFilterChipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = BrandRed,
    selectedLabelColor = Color.White,
    containerColor = SurfaceMuted,
    labelColor = TextSecondary,
)

@Composable
private fun productFilterChipBorder(selected: Boolean) = FilterChipDefaults.filterChipBorder(
    enabled = true,
    selected = selected,
    selectedBorderColor = BrandRed,
    borderColor = BorderDefault,
    selectedBorderWidth = 1.dp,
    borderWidth = 1.dp,
)

private fun ProductAdminRow.productNumberText(value: Double): String =
    if (id == 0L && value == 0.0) "" else value.toString()

private fun editableConversionNumber(value: Double): String =
    if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()

private fun String.editableDoubleOrNull(): Double? = replace(',', '.').toDoubleOrNull()

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
    productTypes: List<ProductTypeRow>,
    products: List<ProductAdminRow>,
    externalScan: DataWedgeScanner.DataWedgeScan? = null,
    physicalScan: String? = null,
    onBack: () -> Unit,
    onSave: (ProductAdminRow) -> Unit,
) {
    val context = LocalContext.current
    var name by remember(initial) { mutableStateOf(initial.name) }
    var code by remember(initial) { mutableStateOf(initial.code) }
    var barcode by remember(initial) { mutableStateOf(initial.barcode) }
    var barcodeValidationMessage by remember(initial) { mutableStateOf<String?>(null) }
    var imageUrl by remember(initial) { mutableStateOf(initial.imageUrl) }
    var categoryId by remember(initial) { mutableStateOf(initial.categoryId) }
    var subcategoryId by remember(initial) { mutableStateOf(initial.subcategoryId) }
    var selectedSubcategoryIds by remember(initial) {
        mutableStateOf(initial.subcategoryIds.ifEmpty { listOfNotNull(initial.subcategoryId.takeIf { it != 0L }) }.toSet())
    }
    var categoryExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }
    var productTypeId by remember(initial) { mutableStateOf(initial.productTypeId) }
    var productTypeExpanded by remember { mutableStateOf(false) }
    var price by remember(initial) { mutableStateOf(initial.productNumberText(initial.price)) }
    var stock by remember(initial) { mutableStateOf(initial.productNumberText(initial.stock)) }
    var costPrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.costPrice)) }
    var oldPrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.oldPrice)) }
    var wholesalePrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.wholesalePrice)) }
    var wholesaleOldPrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.wholesaleOldPrice)) }
    var yapePrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.yapePrice)) }
    var minimumStock by remember(initial) { mutableStateOf(initial.productNumberText(initial.minimumStock)) }
    var description by remember(initial) { mutableStateOf(initial.description) }
    var location by remember(initial) { mutableStateOf(initial.location) }
    var weightKg by remember(initial) { mutableStateOf(initial.productNumberText(initial.weightKg)) }
    var packageMeasures by remember(initial) { mutableStateOf(initial.packageMeasures) }
    var packageDimension by remember(initial) { mutableStateOf(initial.packageDimension) }
    var promoCutoffTime by remember(initial) { mutableStateOf(initial.promoCutoffTime) }
    var saturdayCutoffTime by remember(initial) { mutableStateOf(initial.saturdayCutoffTime) }
    var offerMaxQuantity by remember(initial) { mutableStateOf(initial.productNumberText(initial.offerMaxQuantity)) }
    var offerMaxQuantityPrice by remember(initial) { mutableStateOf(initial.productNumberText(initial.offerMaxQuantityPrice)) }
    var ratingsEnabled by remember(initial) { mutableStateOf(initial.ratingsEnabled) }
    var adminRating by remember(initial) { mutableStateOf(initial.productNumberText(initial.adminRating)) }
    var metaTitle by remember(initial) { mutableStateOf(initial.metaTitle) }
    var metaDescription by remember(initial) { mutableStateOf(initial.metaDescription) }
    var salesChannel by remember(initial) { mutableStateOf(initial.salesChannel.ifBlank { "ambos" }) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    var conversions by remember(initial) { mutableStateOf(initial.conversions) }
    var conversionStockTexts by remember(initial) {
        mutableStateOf(initial.conversions.map { editableConversionNumber(it.stockFactor) })
    }
    var conversionPriceTexts by remember(initial) {
        mutableStateOf(initial.conversions.map { editableConversionNumber(it.finalPrice) })
    }
    val activeCategories = categories.filter { it.active }
    val availableSubcategories = subcategories.filter { it.active && it.categoryId == categoryId }
    val selectedCategoryName = activeCategories.firstOrNull { it.id == categoryId }?.name ?: "Seleccionar"
    val selectedSubcategoryName = availableSubcategories.filter { it.id in selectedSubcategoryIds }
        .joinToString { it.name }.ifBlank { "Sin subcategoria" }
    val selectedProductTypeName = productTypes.firstOrNull { it.id == productTypeId }?.name ?: "Sin etiqueta"
    val duplicateBarcodeProduct = products.firstOrNull { product ->
        barcode.isNotBlank() &&
            product.id != initial.id &&
            product.barcode.trim().equals(barcode.trim(), ignoreCase = true)
    }
    val barcodeError = barcodeValidationMessage
        ?: duplicateBarcodeProduct?.let { "Este producto ya se encuentra en el sistema: ${it.name}." }
    val conversionNames = conversions.map { it.name.trim().lowercase() }
    val conversionNumbersValid = conversionStockTexts.size == conversions.size &&
        conversionPriceTexts.size == conversions.size && conversions.indices.all { index ->
            val stockValue = conversionStockTexts[index].editableDoubleOrNull()
            val priceValue = conversionPriceTexts[index].editableDoubleOrNull()
            stockValue != null && stockValue in 0.0..999999.9999 &&
                priceValue != null && priceValue in 0.01..9999999999.99
        }
    val conversionsValid = conversions.size <= 30 && conversionNumbersValid && conversions.all {
        it.name.trim().isNotBlank() && it.name.trim().length <= 80 && it.code.trim().length <= 50
    } && conversionNames.distinct().size == conversionNames.size
    val canSaveProduct = name.isNotBlank() && categoryId != 0L && parseDouble(price, 0.0) > 0.0 &&
        parseDouble(stock, -1.0) >= 0.0 && duplicateBarcodeProduct == null && conversionsValid
    val compactEditor = LocalConfiguration.current.screenWidthDp < 900
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imageUrl = copyPickedProductImage(context, uri)
    }
    val barcodeFocusRequester = remember { FocusRequester() }
    var showBarcodeCameraScanner by remember { mutableStateOf(false) }

    val physicalScannerConnected by rememberPhysicalScannerConnected(context)
    LaunchedEffect(externalScan) {
        val scan = externalScan ?: return@LaunchedEffect
        Log.d("DataWedge", "Productos: escaneo al campo Código Barra: ${scan.code} (seq=${scan.sequence})")
        Log.d("BarcodeDebug", "Productos: formulario campo Código Barra: [${scan.code}] (length=${scan.code.length})")
        barcode = scan.code
        barcodeValidationMessage = products.firstOrNull { product ->
            product.id != initial.id &&
                product.barcode.trim().equals(scan.code.trim(), ignoreCase = true)
        }?.let { product ->
            "Este producto ya se encuentra en el sistema: ${product.name}."
        }
        barcodeFocusRequester.requestFocus()
    }
    LaunchedEffect(physicalScan) {
        val code = physicalScan ?: return@LaunchedEffect
        Log.d("PhysicalScanner", "Productos: lector físico → campo Código Barra: [$code] (length=${code.length})")
        barcode = code.trim()
        barcodeValidationMessage = null
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
            productTypeId = productTypeId, metaTitle = metaTitle, metaDescription = metaDescription,
            salesChannel = salesChannel, active = active,
            conversions = conversions.mapIndexed { index, conversion ->
                conversion.copy(
                    stockFactor = conversionStockTexts.getOrNull(index)?.editableDoubleOrNull()
                        ?: conversion.stockFactor,
                    finalPrice = conversionPriceTexts.getOrNull(index)?.editableDoubleOrNull()
                        ?: conversion.finalPrice,
                )
            },
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
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedTextField(
                                value = barcode,
                                onValueChange = {
                                    barcode = it
                                    barcodeValidationMessage = null
                                },
                                label = { Text("Código Barra") },
                                modifier = Modifier.weight(1f).focusRequester(barcodeFocusRequester),
                                singleLine = true,
                                isError = barcodeError != null,
                                supportingText = barcodeError?.let { message -> { Text(message) } },
                            )
                            Surface(
                                shape  = RoundedCornerShape(10.dp),
                                color  = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDefault),
                            ) {
                                IconButton(
                                    onClick = {
                                        if (!physicalScannerConnected) showBarcodeCameraScanner = true
                                    },
                                    modifier = Modifier.size(54.dp),
                                ) {
                                    Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear código de barra", tint = TextSecondary)
                                }
                            }
                        }
                    }
                    if (showBarcodeCameraScanner) {
                        CameraScannerDialog(
                            onBarcodeDetected = { code ->
                                barcode = code
                                barcodeValidationMessage = products.firstOrNull { product ->
                                    product.id != initial.id &&
                                        product.barcode.trim().equals(code.trim(), ignoreCase = true)
                                }?.let { product ->
                                    "Este producto ya se encuentra en el sistema: ${product.name}."
                                }
                                showBarcodeCameraScanner = false
                            },
                            onDismiss = { showBarcodeCameraScanner = false },
                        )
                    }
                    OutlinedTextField(name, { name = it }, label = { Text("Nombre *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    ProductCheckboxField(
                        checked = ratingsEnabled,
                        label = "Permitir calificación por usuarios",
                        onCheckedChange = { ratingsEnabled = it },
                    )
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

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Conversiones", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                if (conversions.size < 30) {
                                    val initialPrice = parseDouble(price, 0.0)
                                    conversions = conversions + ProductConversion(
                                        id = 0L, name = "", code = "", stockFactor = 1.0, finalPrice = initialPrice,
                                    )
                                    conversionStockTexts = conversionStockTexts + "1"
                                    conversionPriceTexts = conversionPriceTexts + editableConversionNumber(initialPrice)
                                }
                            },
                            enabled = conversions.size < 30,
                        ) { Text("+ Agregar conversión") }
                    }
                    Text(
                        "Opcional. Cada conversión usa su propio precio y descuenta el stock según su factor.",
                        color = TextSecondary,
                    )
                    conversions.forEachIndexed { index, conversion ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White,
                        ) {
                            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text("Conversión ${index + 1}", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                        conversions = conversions.filterIndexed { i, _ -> i != index }
                                        conversionStockTexts = conversionStockTexts.filterIndexed { i, _ -> i != index }
                                        conversionPriceTexts = conversionPriceTexts.filterIndexed { i, _ -> i != index }
                                    }) {
                                        Icon(Icons.Filled.Close, contentDescription = "Eliminar conversión")
                                    }
                                }
                                FlowRow(
                                    Modifier.fillMaxWidth(),
                                    maxItemsInEachRow = if (compactEditor) 1 else 4,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    OutlinedTextField(conversion.name, { value ->
                                        conversions = conversions.toMutableList().also { it[index] = conversion.copy(name = value) }
                                    }, label = { Text("Nombre *") }, modifier = Modifier.weight(1f), singleLine = true)
                                    OutlinedTextField(conversion.code, { value ->
                                        conversions = conversions.toMutableList().also { it[index] = conversion.copy(code = value) }
                                    }, label = { Text("Código") }, modifier = Modifier.weight(1f), singleLine = true)
                                    OutlinedTextField(conversionStockTexts.getOrElse(index) { "" }, { value ->
                                        conversionStockTexts = conversionStockTexts.toMutableList().also { it[index] = value }
                                    }, label = { Text("Stock conversión *") }, modifier = Modifier.weight(1f), singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                                    OutlinedTextField(conversionPriceTexts.getOrElse(index) { "" }, { value ->
                                        conversionPriceTexts = conversionPriceTexts.toMutableList().also { it[index] = value }
                                    }, label = { Text("Precio final *") }, prefix = { Text("S/ ") }, modifier = Modifier.weight(1f), singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Checkbox(
                                            checked = conversion.active,
                                            onCheckedChange = { checked ->
                                                conversions = conversions.toMutableList().also {
                                                    it[index] = conversion.copy(active = checked)
                                                }
                                            },
                                        )
                                        Text("Activa")
                                    }
                                }
                            }
                        }
                    }
                    if (!conversionsValid) Text(
                        "Revisa las conversiones: nombres únicos, stock no negativo y precio final mayor a 0.",
                        color = MaterialTheme.colorScheme.error,
                    )

                    Text("Inventario y Categorías", fontWeight = FontWeight.Bold)
                    OutlinedTextField(stock, { stock = it }, label = { Text("Stock *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 2, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ProductSelectField(
                            label = "Categoría", value = selectedCategoryName, expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = it }, required = true,
                            options = activeCategories.map { ProductSelectOption(it.id, it.name) },
                            onSelect = { categoryId = it; subcategoryId = 0L; selectedSubcategoryIds = emptySet() },
                            modifier = Modifier.weight(1f),
                        )
                        ProductMultiSelectField(
                            label = "Subcategorías", value = selectedSubcategoryName, expanded = subcategoryExpanded,
                            onExpandedChange = { subcategoryExpanded = it },
                            options = availableSubcategories.map { ProductSelectOption(it.id, it.name) },
                            selectedIds = selectedSubcategoryIds,
                            onToggle = { id ->
                                selectedSubcategoryIds = if (id in selectedSubcategoryIds) selectedSubcategoryIds - id else selectedSubcategoryIds + id
                                subcategoryId = selectedSubcategoryIds.firstOrNull() ?: 0L
                            },
                            onClear = { subcategoryId = 0L; selectedSubcategoryIds = emptySet() },
                            enabled = categoryId != 0L && availableSubcategories.isNotEmpty(),
                            modifier = Modifier.weight(1f),
                        )
                    }
                    ProductSelectField(
                        label = "Etiqueta", value = selectedProductTypeName, expanded = productTypeExpanded,
                        onExpandedChange = { productTypeExpanded = it },
                        options = productTypes.map { ProductSelectOption(it.id, it.name) },
                        onSelect = { productTypeId = it }, clearLabel = "Sin etiqueta",
                        enabled = productTypes.isNotEmpty(), modifier = Modifier.fillMaxWidth(),
                    )
                    ProductCheckboxField(
                        checked = active,
                        label = "Producto activo",
                        onCheckedChange = { active = it },
                    )

                    Text("SEO descripción", fontWeight = FontWeight.Bold)
                    OutlinedTextField(metaTitle, { metaTitle = it }, label = { Text("Meta título del producto (opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(metaDescription, { metaDescription = it }, label = { Text("Meta descripción del producto (opcional)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
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
                    Box(
                        Modifier.fillMaxWidth().height(210.dp).background(Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center,
                    ) {
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
                                        productTypeId = productTypeId,
                                        metaTitle = metaTitle,
                                        metaDescription = metaDescription,
                                        salesChannel = salesChannel,
                                        active = active,
                                        conversions = conversions,
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
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedContainerColor = Color.White,
            labelColor = Color(0xFF111827),
            selectedLabelColor = Color(0xFFFD0505),
        ),
        border = androidx.compose.material3.FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedValue == value,
            borderColor = Color(0xFFCBD5E1),
            selectedBorderColor = Color(0xFFFD0505),
        ),
    )
}
