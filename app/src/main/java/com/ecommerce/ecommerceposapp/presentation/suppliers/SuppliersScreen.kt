package com.ecommerce.ecommerceposapp.presentation.suppliers

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Paleta consistente con el modulo de productos
private val Brand = Color(0xFFFD0505)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF475569)
private val DividerColor = Color(0xFFE2E8F0)
private val SurfaceMutedLocal = Color(0xFFF1F5F9)
private val BorderDefaultLocal = Color(0xFFCBD5E1)

private fun isValidEmail(value: String): Boolean {
    if (value.isBlank()) return true
    val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return regex.matches(value.trim())
}
@Composable
fun SuppliersCrudScreen(vm: SuppliersViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }

    var editing by remember { mutableStateOf<SupplierRow?>(null) }
    var creatingAdvanced by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedEstado by remember { mutableStateOf<String?>(null) }

    val filteredSuppliers = state.suppliers
        .filter { selectedEstado == null || it.estado.ifBlank { "Activo" } == selectedEstado }
        .filter {
            search.isBlank() ||
                    it.businessName.contains(search, ignoreCase = true) ||
                    it.ruc.contains(search, ignoreCase = true) ||
                    it.codigoProveedor.contains(search, ignoreCase = true) ||
                    it.correo.contains(search, ignoreCase = true)
        }
    val compactScreen = LocalConfiguration.current.screenWidthDp < 600

    if (creatingAdvanced || editing != null) {
        SupplierAdvancedEditorView(
            initial = editing ?: SupplierRow(id = 0, businessName = "", ruc = "", phone = ""),
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
                Text("Proveedores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Administra los proveedores disponibles para tus compras.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }
            Button(
                onClick = { creatingAdvanced = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("+ Nuevo proveedor")
            }
        } else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Proveedores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Administra los proveedores disponibles para tus compras.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
            Button(onClick = { creatingAdvanced = true }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Brand, contentColor = Color.White)) {
                Text("+ Nuevo proveedor")
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(12.dp))
        SuppliersTable(
            suppliers = filteredSuppliers,
            search = search,
            onSearch = { search = it },
            selectedEstado = selectedEstado,
            onEstado = { selectedEstado = it },
            onEdit = { editing = it },
            onDelete = { supplier ->
                pendingConfirm = PendingConfirm(
                    title = "Eliminar proveedor",
                    body = "¿Eliminar definitivamente al proveedor ${supplier.businessName}? Esta acción también lo eliminará de la web.",
                    confirmButtonText = "Eliminar",
                    onConfirm = { vm.remove(supplier.id) },
                )
            },
        )
    }
    ConfirmDestructiveDialog(pendingConfirm) { pendingConfirm = null }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
@Composable
private fun SuppliersTable(
    suppliers: List<SupplierRow>,
    search: String,
    onSearch: (String) -> Unit,
    selectedEstado: String?,
    onEstado: (String?) -> Unit,
    onEdit: (SupplierRow) -> Unit,
    onDelete: (SupplierRow) -> Unit,
) {
    val estados = listOf("Activo", "Inactivo", "Bloqueado")
    val compact = LocalConfiguration.current.screenWidthDp < 760
    var pageSize by remember { mutableStateOf(10) }
    var pageSizeExpanded by remember { mutableStateOf(false) }
    val totalPages = maxOf(1, (suppliers.size + pageSize - 1) / pageSize)
    var currentPage by remember(suppliers.size, pageSize) { mutableStateOf(0) }
    val pageSuppliers = suppliers.drop(currentPage * pageSize).take(pageSize)

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = TextPrimary,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column {
            val searchField: @Composable (Modifier) -> Unit = { fieldModifier ->
                OutlinedTextField(
                    value = search,
                    onValueChange = onSearch,
                    placeholder = { Text("Buscar por nombre o RUC") },
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
                            selected = selectedEstado == null,
                            onClick = { onEstado(null) },
                            label = { Text("Todos") },
                            colors = supplierFilterChipColors(),
                            border = supplierFilterChipBorder(selectedEstado == null),
                        )
                    }
                    items(estados) { estado ->
                        FilterChip(
                            selected = selectedEstado == estado,
                            onClick = { onEstado(estado) },
                            label = { Text(estado) },
                            colors = supplierFilterChipColors(),
                            border = supplierFilterChipBorder(selectedEstado == estado),
                        )
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
                if (!compact) HeaderCell("Código", 0.8f)
                HeaderCell("Razón social", 1.6f)
                if (!compact) HeaderCell("RUC", 1f)
                if (!compact) HeaderCell("Correo", 1.2f)
                if (!compact) HeaderCell("Teléfono", 0.9f)
                HeaderCell("Estado", 0.8f)
                if (!compact) HeaderCell("Calificación", 1.2f)
                if (!compact) HeaderCell("Banco", 0.9f)
                Spacer(Modifier.width(48.dp))
            }
            HorizontalDivider(color = DividerColor)
            LazyColumn(Modifier.fillMaxWidth().weight(1f).background(Color.White)) {
                items(pageSuppliers, key = { it.id }) { supplier ->
                    SupplierTableRow(supplier = supplier, compact = compact, onEdit = onEdit, onDelete = onDelete)
                    HorizontalDivider(color = DividerColor)
                }
            }
            val paginationInfo: @Composable () -> Unit = {
                val from = if (suppliers.isEmpty()) 0 else currentPage * pageSize + 1
                val to = minOf(suppliers.size, (currentPage + 1) * pageSize)
                Text(if (compact) "Filas:" else "Registros por pagina:", color = TextSecondary)
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = TextPrimary) }
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
                Text("$from-$to de ${suppliers.size}", color = TextSecondary)
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
                        Text("Pagina ${currentPage + 1} de $totalPages", color = TextSecondary)
                        paginationButtons()
                    }
                }
            } else {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    paginationInfo()
                    Spacer(Modifier.weight(1f))
                    Text("Pagina ${currentPage + 1} de $totalPages", color = TextSecondary)
                    paginationButtons()
                }
            }
        }
    }
}

@Composable
private fun supplierFilterChipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Brand,
    selectedLabelColor = Color.White,
    containerColor = SurfaceMutedLocal,
    labelColor = TextSecondary,
)

@Composable
private fun supplierFilterChipBorder(selected: Boolean) = FilterChipDefaults.filterChipBorder(
    enabled = true,
    selected = selected,
    selectedBorderColor = Brand,
    borderColor = BorderDefaultLocal,
    selectedBorderWidth = 1.dp,
    borderWidth = 1.dp,
)

@Composable
private fun SupplierTableRow(
    supplier: SupplierRow,
    compact: Boolean,
    onEdit: (SupplierRow) -> Unit,
    onDelete: (SupplierRow) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(Color.White).clickable { onEdit(supplier) }.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!compact) Text(supplier.codigoProveedor.ifBlank { "-" }, modifier = Modifier.weight(0.8f), color = TextPrimary)
        Column(modifier = Modifier.weight(1.6f)) {
            Text(supplier.businessName.ifBlank { "—" }, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
            if (compact) Text(supplier.codigoProveedor.ifBlank { "-" }, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        }
        if (!compact) Text(
            supplier.ruc.ifBlank { "-" },
            modifier = Modifier.weight(1f),
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (!compact) Text(supplier.correo.ifBlank { "-" }, modifier = Modifier.weight(1.4f), color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!compact) Text(supplier.phone.ifBlank { "-" }, modifier = Modifier.weight(1f), color = TextPrimary)
        Box(modifier = Modifier.weight(0.9f)) {
            SupplierStatusPill(supplier.estado.ifBlank { "Activo" })
        }
        if (!compact) Text(
            if (supplier.calificacion > 0) supplier.calificacion.toString() else "-",
            modifier = Modifier.weight(0.9f),
            color = TextPrimary,
        )
        if (!compact) Text(supplier.banco.ifBlank { "-" }, modifier = Modifier.weight(0.9f), color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Opciones")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        menuExpanded = false
                        onEdit(supplier)
                    },
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        menuExpanded = false
                        onDelete(supplier)
                    },
                )
            }
        }
    }
}

@Composable
private fun SupplierStatusPill(status: String) {
    val (bg, fg) = when (status.lowercase()) {
        "activo" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "bloqueado" -> Color(0xFFFFEBEB) to Brand
        else -> Color(0xFFF3F4F6) to TextSecondary
    }
    Surface(shape = RoundedCornerShape(20.dp), color = bg) {
        Text(
            status,
            color = fg,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun SupplierAdvancedEditorView(
    initial: SupplierRow,
    onBack: () -> Unit,
    onSave: (SupplierRow) -> Unit,
) {
    var codigo by remember(initial) { mutableStateOf(initial.codigoProveedor) }
    var name by remember(initial) { mutableStateOf(initial.businessName) }
    var ruc by remember(initial) { mutableStateOf(initial.ruc) }
    var correo by remember(initial) { mutableStateOf(initial.correo) }
    var phone by remember(initial) { mutableStateOf(initial.phone) }
    var direccion by remember(initial) { mutableStateOf(initial.direccion) }
    var personaContacto by remember(initial) { mutableStateOf(initial.personaContacto) }
    var cargoContacto by remember(initial) { mutableStateOf(initial.cargoContacto) }
    var telefonoContacto by remember(initial) { mutableStateOf(initial.telefonoContacto) }
    var correoContacto by remember(initial) { mutableStateOf(initial.correoContacto) }
    var calificacion by remember(initial) { mutableStateOf(initial.calificacion) }
    var estado by remember(initial) { mutableStateOf(initial.estado.ifBlank { "Activo" }) }
    var observaciones by remember(initial) { mutableStateOf(initial.observaciones) }
    var banco by remember(initial) { mutableStateOf(initial.banco) }
    var cuenta by remember(initial) { mutableStateOf(initial.cuenta) }
    var cci by remember(initial) { mutableStateOf(initial.cci) }
    var correoError by remember { mutableStateOf(false) }
    var correoContactoError by remember { mutableStateOf(false) }
    // Fecha de registro: si el proveedor es nuevo (viene vacía), se autocompleta con la fecha actual
    val fechaRegistro = remember(initial) {
        initial.fechaRegistro.ifBlank {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }
    }
    var codigoError by remember { mutableStateOf(false) }
    var rucError by remember { mutableStateOf(false) }

    val compactEditor = LocalConfiguration.current.screenWidthDp < 900

    val canSaveSupplier = codigo.isNotBlank() &&
            name.isNotBlank() &&
            ruc.length == 11

    val draftSupplier = {
        SupplierRow(
            id = initial.id,
            codigoProveedor = codigo.trim(),
            businessName = name.trim(),
            ruc = ruc,
            correo = correo.trim(),
            phone = phone.trim(),
            direccion = direccion.trim(),
            personaContacto = personaContacto.trim(),
            cargoContacto = cargoContacto.trim(),
            telefonoContacto = telefonoContacto.trim(),
            correoContacto = correoContacto.trim(),
            calificacion = calificacion,
            estado = estado,
            fechaRegistro = fechaRegistro,
            observaciones = observaciones,
            banco = banco.trim(),
            cuenta = cuenta.trim(),
            cci = cci.trim(),
            active = estado == "Activo",
        )
    }

    val trySave: () -> Unit = {
        var hasError = false
        if (codigo.isBlank()) { codigoError = true; hasError = true }
        if (ruc.length != 11) { rucError = true; hasError = true }
        if (!isValidEmail(correo)) { correoError = true; hasError = true }
        if (!isValidEmail(correoContacto)) { correoContactoError = true; hasError = true }
        if (!hasError) onSave(draftSupplier())
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("< Volver") }
            Text(
                if (initial.id == 0L) "Nuevo proveedor" else "Editar proveedor",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
            ) {
                Column(Modifier.padding(18.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text("Información del proveedor", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 2, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = codigo,
                            onValueChange = { codigo = it; codigoError = false },
                            label = { Text("Código Proveedor *") },
                            isError = codigoError,
                            supportingText = { if (codigoError) Text("El código es obligatorio", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Razón social *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = ruc,
                            onValueChange = { input ->
                                val digits = input.filter { it.isDigit() }
                                if (digits.length <= 11) { ruc = digits; rucError = false }
                            },
                            label = { Text("RUC *") },
                            isError = rucError,
                            supportingText = { if (rucError) Text("El RUC debe tener 11 dígitos", color = MaterialTheme.colorScheme.error) },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it; correoError = false },
                            label = { Text("Correo") },
                            isError = correoError,
                            supportingText = {
                                if (correoError) Text("Ingresa un correo válido, ej: nombre@dominio.com", color = MaterialTheme.colorScheme.error)
                            },
                            modifier = Modifier.weight(1f).onFocusChanged {
                                if (!it.isFocused && correo.isNotBlank() && !isValidEmail(correo)) correoError = true
                            },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { input -> phone = input.filter { it.isDigit() } },
                            label = { Text("Teléfono") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.weight(1f), singleLine = true)
                    }

                    Text("Información de contacto", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 2, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(personaContacto, { personaContacto = it }, label = { Text("Persona de contacto") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(cargoContacto, { cargoContacto = it }, label = { Text("Cargo de contacto") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(
                            value = telefonoContacto,
                            onValueChange = { input -> telefonoContacto = input.filter { it.isDigit() } },
                            label = { Text("Teléfono de contacto") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = correoContacto,
                            onValueChange = { correoContacto = it; correoContactoError = false },
                            label = { Text("Correo de contacto") },
                            isError = correoContactoError,
                            supportingText = {
                                if (correoContactoError) Text("Ingresa un correo válido, ej: nombre@dominio.com", color = MaterialTheme.colorScheme.error)
                            },
                            modifier = Modifier.weight(1f).onFocusChanged {
                                if (!it.isFocused && correo.isNotBlank() && !isValidEmail(correo)) correoError = true
                            },
                            singleLine = true,
                        )
                    }

                    Text("Calificación y estado", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 3, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("Calificación", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(4.dp))
                            StarRating(value = calificacion, onChange = { calificacion = it })
                        }
                        EstadoDropdown(
                            value = estado,
                            onChange = { estado = it },
                            modifier = Modifier.weight(1f),
                        )
                        OutlinedTextField(
                            value = fechaRegistro,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fecha de registro") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Text("Información bancaria", fontWeight = FontWeight.Bold)
                    FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = if (compactEditor) 1 else 3, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(banco, { banco = it }, label = { Text("Banco") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(
                            value = cuenta,
                            onValueChange = { input -> cuenta = input.filter { it.isDigit() } },
                            label = { Text("Cuenta") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = cci,
                            onValueChange = { input -> cci = input.filter { it.isDigit() } },
                            label = { Text("CCI") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                    }

                    Text("Observaciones", fontWeight = FontWeight.Bold)
                    OutlinedTextField(observaciones, { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(Modifier.width(12.dp))

                        Button(
                            onClick = trySave,
                            enabled = canSaveSupplier,
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Brand,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                if (initial.id == 0L)
                                    "Crear proveedor"
                                else
                                    "Guardar"
                            )
                        }
                    }
                }
            }
            if (!compactEditor) Surface(modifier = Modifier.width(360.dp).fillMaxSize(), color = Color.White) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(name.ifBlank { "Razón social del proveedor" }, style = MaterialTheme.typography.titleLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(ruc.ifBlank { "Sin RUC" }, style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                    SupplierStatusPill(estado)
                    Spacer(Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)) { Text("Cancelar") }
                        Button(
                            onClick = trySave,
                            modifier = Modifier.weight(1f),
                            enabled = canSaveSupplier,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand, contentColor = Color.White),
                        ) { Text(if (initial.id == 0L) "Crear proveedor" else "Guardar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun StarRating(value: Int, onChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (star in 1..5) {
            IconButton(onClick = { onChange(if (value == star) 0 else star) }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (star <= value) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "Calificación $star",
                    tint = if (star <= value) Color(0xFFF59E0B) else BorderDefaultLocal,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstadoDropdown(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val opciones = listOf("Activo", "Inactivo", "Bloqueado")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Estado") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onChange(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}