package com.ecommerce.ecommerceposapp.presentation.suppliers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.window.Dialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm

// Paleta consistente con el resto de la app
private val Brand = Color(0xFFFD0505)
private val AppBg = Color(0xFFFFFFFF)
private val SurfaceWhite = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val DividerColor = Color(0xFFE5E7EB)
private const val PAGE_SIZE = 10

@Composable
fun SuppliersCrudScreen(vm: SuppliersViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }

    var editing by remember { mutableStateOf<SupplierRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var page by remember { mutableStateOf(0) }

    val filtered = remember(state.suppliers, search) {
        if (search.isBlank()) {
            state.suppliers
        } else {
            state.suppliers.filter {
                it.businessName.contains(search, ignoreCase = true) ||
                        it.ruc.contains(search, ignoreCase = true) ||
                        it.codigoProveedor.contains(search, ignoreCase = true) ||
                        it.correo.contains(search, ignoreCase = true)
            }
        }
    }
    LaunchedEffect(filtered.size) {
        val maxPage = if (filtered.isEmpty()) 0 else (filtered.size - 1) / PAGE_SIZE
        if (page > maxPage) page = 0
    }
    val totalPages = if (filtered.isEmpty()) 1 else ((filtered.size - 1) / PAGE_SIZE) + 1
    val pageItems = filtered.drop(page * PAGE_SIZE).take(PAGE_SIZE)

    // Contenedor responsive: en tablet se centra y limita a 600dp
    Box(
        modifier = Modifier.fillMaxSize().background(AppBg),
        contentAlignment = Alignment.TopCenter,
    ) {
    Column(
        Modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Encabezado + botón en la misma fila ──────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Proveedores",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Administra los proveedores disponibles para tus compras.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = { showCreate = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Agregar", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it; page = 0 },
            placeholder = { Text("Buscar por nombre o RUC", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
        )
        Spacer(Modifier.height(16.dp))

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }
        state.message?.let {
            Text(it, color = Color(0xFF16A34A))
            Spacer(Modifier.height(8.dp))
        }

        if (pageItems.isEmpty() && !state.isLoading) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                tonalElevation = 0.dp,
            ) {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No hay proveedores registrados", color = TextSecondary)
                }
            }
        } else {
            SupplierTable(
                items = pageItems,
                onEdit = { row -> editing = row },
                onDelete = { row ->
                    pendingConfirm = PendingConfirm(
                        title = "Desactivar proveedor",
                        body = "¿Desactivar al proveedor ${row.businessName}? Dejará de mostrarse como activo.",
                        confirmButtonText = "Desactivar",
                        onConfirm = { vm.remove(row.id) },
                    )
                },
            )
        }
    }
    } // Box responsive

    if (showCreate) {
        SupplierEditDialog(
            SupplierRow(id = 0, businessName = "", ruc = "", phone = ""),
            onDismiss = { showCreate = false; vm.clearMessages() },
            onSave = { vm.save(it); showCreate = false },
        )
    }
    editing?.let { row ->
        SupplierEditDialog(row, onDismiss = { editing = null; vm.clearMessages() }, onSave = { vm.save(it); editing = null })
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun SupplierTable(
    items: List<SupplierRow>,
    onEdit: (SupplierRow) -> Unit,
    onDelete: (SupplierRow) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── Cabecera ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Proveedor / RUC", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(2.5f))
                Text("Contacto", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.8f))
                Text("Estado", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f))
                Spacer(Modifier.width(32.dp))
            }

            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            // ── Filas ─────────────────────────────────────────────────────────
            items.forEachIndexed { index, row ->
                SupplierTableRow(row = row, onEdit = { onEdit(row) }, onDelete = { onDelete(row) })
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = DividerColor,
                        thickness = 0.5.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun SupplierTableRow(row: SupplierRow, onEdit: () -> Unit, onDelete: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Col 1: nombre + código + RUC
        Column(
            modifier = Modifier.weight(2.5f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                row.businessName.ifBlank { "—" },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (row.codigoProveedor.isNotBlank()) {
                Text(
                    row.codigoProveedor,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                )
            }
            if (row.ruc.isNotBlank()) {
                Text(
                    row.ruc,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                )
            }
        }

        // Col 2: teléfono + correo
        Column(
            modifier = Modifier.weight(1.8f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                row.phone.ifBlank { "—" },
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (row.correo.isNotBlank()) {
                Text(
                    row.correo,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        // Col 3: badge de estado
        Box(modifier = Modifier.weight(1.2f)) {
            StatusPill(row.estado.ifBlank { "Activo" })
        }

        // Menú de opciones
        Box {
            IconButton(onClick = { menuExpanded = true }, modifier = Modifier.width(32.dp)) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Opciones", tint = TextSecondary, modifier = Modifier.size(18.dp))
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(text = { Text("Editar") }, onClick = { menuExpanded = false; onEdit() })
                DropdownMenuItem(text = { Text("Desactivar") }, onClick = { menuExpanded = false; onDelete() })
            }
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val (bg, fg) = when (status.lowercase()) {
        "activo" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "bloqueado" -> Color(0xFFFFEBEB) to Brand
        else -> Color(0xFFF3F4F6) to TextSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(status, color = fg, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PaginationFooter(
    page: Int,
    totalPages: Int,
    totalItems: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(PAGE_SIZE.toString(), color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        val start = if (totalItems == 0) 0 else page * PAGE_SIZE + 1
        val end = minOf((page + 1) * PAGE_SIZE, totalItems)
        Text("$start-$end de $totalItems", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Row {
            IconButton(onClick = onPrev, enabled = page > 0) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Anterior",
                    tint = if (page > 0) TextPrimary else Color(0xFFD1D5DB),
                )
            }
            IconButton(onClick = onNext, enabled = page < totalPages - 1) {
                Icon(
                    Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Siguiente",
                    tint = if (page < totalPages - 1) TextPrimary else Color(0xFFD1D5DB),
                )
            }
        }
    }
}

@Composable
private fun SupplierEditDialog(initial: SupplierRow, onDismiss: () -> Unit, onSave: (SupplierRow) -> Unit) {
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
    var calificacion by remember(initial) { mutableStateOf(initial.calificacion.toString()) }
    var estado by remember(initial) { mutableStateOf(initial.estado.ifBlank { "Activo" }) }
    var observaciones by remember(initial) { mutableStateOf(initial.observaciones) }
    var banco by remember(initial) { mutableStateOf(initial.banco) }
    var cuenta by remember(initial) { mutableStateOf(initial.cuenta) }
    var cci by remember(initial) { mutableStateOf(initial.cci) }
    var codigoError by remember { mutableStateOf(false) }
    // Solo se activa cuando el usuario intenta guardar con RUC incompleto
    var rucError by remember { mutableStateOf(false) }

    val estados = listOf("Activo", "Inactivo", "Bloqueado")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                // Título
                Text(
                    if (initial.id == 0L) "Nuevo proveedor" else "Editar proveedor",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(16.dp))

                // Contenido scrollable
                Column(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 460.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = codigo,
                        onValueChange = { codigo = it; codigoError = false },
                        label = { Text("Codigo proveedor *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = codigoError,
                        supportingText = {
                            if (codigoError) Text("El código es obligatorio", color = MaterialTheme.colorScheme.error)
                        },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Razon social") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    // RUC: solo dígitos, máx 11, error solo si está incompleto al guardar
                    OutlinedTextField(
                        value = ruc,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }
                            if (digits.length <= 11) {
                                ruc = digits
                                rucError = false
                            }
                        },
                        label = { Text("RUC") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = rucError,
                        supportingText = if (rucError) {
                            { Text("El RUC debe tener exactamente 11 dígitos", color = MaterialTheme.colorScheme.error) }
                        } else null,
                        singleLine = true,
                    )
                    OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Direccion") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = personaContacto, onValueChange = { personaContacto = it }, label = { Text("Persona de contacto") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = cargoContacto, onValueChange = { cargoContacto = it }, label = { Text("Cargo del contacto") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = telefonoContacto, onValueChange = { telefonoContacto = it }, label = { Text("Telefono del contacto") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = correoContacto, onValueChange = { correoContacto = it }, label = { Text("Correo del contacto") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(
                        value = calificacion,
                        onValueChange = { input -> if (input.isEmpty() || input.toIntOrNull() != null) calificacion = input },
                        label = { Text("Calificacion (1-5)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    // ComboBox Estado
                    SelectField("Estado", estado, estados) { estado = it }

                    OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = banco, onValueChange = { banco = it }, label = { Text("Banco") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = cuenta, onValueChange = { cuenta = it }, label = { Text("Cuenta") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = cci, onValueChange = { cci = it }, label = { Text("CCI") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                }

                Spacer(Modifier.height(20.dp))

                // Botones
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                ) {
                    TextButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Cancelar", color = Brand, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            var hasError = false
                            if (codigo.isBlank()) { codigoError = true; hasError = true }
                            if (ruc.isNotBlank() && ruc.length != 11) { rucError = true; hasError = true }
                            if (hasError) return@Button
                            onSave(
                                SupplierRow(
                                    id = initial.id,
                                    codigoProveedor = codigo,
                                    businessName = name,
                                    ruc = ruc,
                                    correo = correo,
                                    phone = phone,
                                    direccion = direccion,
                                    personaContacto = personaContacto,
                                    cargoContacto = cargoContacto,
                                    telefonoContacto = telefonoContacto,
                                    correoContacto = correoContacto,
                                    calificacion = calificacion.toIntOrNull() ?: 0,
                                    estado = estado,
                                    fechaRegistro = initial.fechaRegistro,
                                    observaciones = observaciones,
                                    banco = banco,
                                    cuenta = cuenta,
                                    cci = cci,
                                    active = estado == "Activo",
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Guardar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectField(label: String, value: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        DropdownMenu(expanded, { expanded = false }, modifier = Modifier.widthIn(min = 220.dp)) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.ifBlank { "No especificado" }) },
                    onClick = { onSelected(option); expanded = false },
                )
            }
        }
    }
}
