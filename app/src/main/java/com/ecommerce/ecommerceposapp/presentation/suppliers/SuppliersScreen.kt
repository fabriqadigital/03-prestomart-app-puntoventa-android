package com.ecommerce.ecommerceposapp.presentation.suppliers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
private val AppBg = Color(0xFFF5F7FA)
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

    Column(
        Modifier
            .fillMaxSize()
            .background(AppBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            "Proveedores",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Administra los proveedores disponibles para tus compras.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { showCreate = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Brand),
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.width(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Agregar", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it; page = 0 },
            placeholder = { Text("Buscar por nombre o RUC") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
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
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                shadowElevation = 2.dp,
            ) {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No hay proveedores registrados", color = TextSecondary)
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                pageItems.forEach { row ->
                    SupplierCard(
                        row = row,
                        onEdit = { editing = row },
                        onDelete = {
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
            /*Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                shadowElevation = 2.dp,
            ) {
                PaginationFooter(
                    page = page,
                    totalPages = totalPages,
                    totalItems = filtered.size,
                    onPrev = { if (page > 0) page-- },
                    onNext = { if (page < totalPages - 1) page++ },
                )
            }*/
        }
    }

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
private fun SupplierCard(row: SupplierRow, onEdit: () -> Unit, onDelete: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DividerColor),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(
                        row.businessName.ifBlank { "-" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (row.codigoProveedor.isNotBlank()) {
                        Text(
                            row.codigoProveedor,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                        )
                    }
                }
                StatusPill(row.estado.ifBlank { "Activo" })
                Box {
                    IconButton(onClick = { menuExpanded = true }, modifier = Modifier.width(36.dp)) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Opciones", tint = TextSecondary)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Editar") }, onClick = { menuExpanded = false; onEdit() })
                        DropdownMenuItem(text = { Text("Desactivar") }, onClick = { menuExpanded = false; onDelete() })
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            InfoLine("RUC", row.ruc)
            InfoLine("Teléfono", row.phone)
            if (row.correo.isNotBlank()) InfoLine("Correo", row.correo)
            if (row.fechaRegistro.isNotBlank()) InfoLine("Registrado", row.fechaRegistro)
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    if (value.isBlank()) return
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.width(72.dp),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
    var estadoMenuExpanded by remember { mutableStateOf(false) }
    var observaciones by remember(initial) { mutableStateOf(initial.observaciones) }
    var banco by remember(initial) { mutableStateOf(initial.banco) }
    var cuenta by remember(initial) { mutableStateOf(initial.cuenta) }
    var cci by remember(initial) { mutableStateOf(initial.cci) }
    var codigoError by remember { mutableStateOf(false) }

    val estados = listOf("Activo", "Inactivo", "Bloqueado")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nuevo proveedor" else "Editar proveedor") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
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
                )
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Razon social") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ruc, onValueChange = { ruc = it }, label = { Text("RUC") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Direccion") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = personaContacto, onValueChange = { personaContacto = it }, label = { Text("Persona de contacto") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cargoContacto, onValueChange = { cargoContacto = it }, label = { Text("Cargo del contacto") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = telefonoContacto, onValueChange = { telefonoContacto = it }, label = { Text("Telefono del contacto") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = correoContacto, onValueChange = { correoContacto = it }, label = { Text("Correo del contacto") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = calificacion,
                    onValueChange = { input -> if (input.isEmpty() || input.toIntOrNull() != null) calificacion = input },
                    label = { Text("Calificacion (1-5)") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Box(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { estadoMenuExpanded = true }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Cambiar estado")
                            }
                        },
                    )
                    DropdownMenu(expanded = estadoMenuExpanded, onDismissRequest = { estadoMenuExpanded = false }) {
                        estados.forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = { estado = opcion; estadoMenuExpanded = false })
                        }
                    }
                }

                OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = banco, onValueChange = { banco = it }, label = { Text("Banco") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cuenta, onValueChange = { cuenta = it }, label = { Text("Cuenta") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cci, onValueChange = { cci = it }, label = { Text("CCI") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (codigo.isBlank()) {
                    codigoError = true
                    return@TextButton
                }
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
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
    )
}