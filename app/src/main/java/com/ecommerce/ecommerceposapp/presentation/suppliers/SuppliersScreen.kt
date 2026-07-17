package com.ecommerce.ecommerceposapp.presentation.suppliers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.CrudEditDeleteIcons
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.ToolbarAddIconButton

private val COMPACT_BREAKPOINT = 600.dp

@Composable
fun SuppliersCrudScreen(vm: SuppliersViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    var editing by remember { mutableStateOf<SupplierRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista de proveedores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nuevo proveedor")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))

        BoxWithConstraints(Modifier.fillMaxWidth().weight(1f)) {
            val isCompact = maxWidth < COMPACT_BREAKPOINT
            val onEdit: (SupplierRow) -> Unit = { editing = it }
            val onDelete: (SupplierRow) -> Unit = { row ->
                pendingConfirm = PendingConfirm(
                    title = "Desactivar proveedor",
                    body = "Desactivar al proveedor ${row.businessName}? Dejara de mostrarse como activo.",
                    confirmButtonText = "Desactivar",
                    onConfirm = { vm.remove(row.id) },
                )
            }

            if (isCompact) {
                SuppliersCardList(state.suppliers, state.isLoading, onEdit, onDelete)
            } else {
                SuppliersTable(state.suppliers, state.isLoading, onEdit, onDelete)
            }
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

// ---------- Layout compacto (celular): tarjetas ----------

@Composable
private fun SuppliersCardList(
    suppliers: List<SupplierRow>,
    isLoading: Boolean,
    onEdit: (SupplierRow) -> Unit,
    onDelete: (SupplierRow) -> Unit,
) {
    if (suppliers.isEmpty() && !isLoading) {
        EmptyState()
        return
    }
    LazyColumn(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(suppliers, key = { it.id }) { row ->
            SupplierCard(row, onEdit = { onEdit(row) }, onDelete = { onDelete(row) })
        }
    }
}

@Composable
private fun SupplierCard(row: SupplierRow, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(row.businessName.ifBlank { "-" }, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    if (row.codigoProveedor.isNotBlank()) {
                        Text("Código: ${row.codigoProveedor}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                CrudEditDeleteIcons(onEdit = onEdit, onDelete = onDelete, deleteContentDescription = "Desactivar proveedor")
            }
            Spacer(Modifier.height(6.dp))
            InfoLine("RUC", row.ruc)
            InfoLine("Teléfono", row.phone)
            if (row.correo.isNotBlank()) InfoLine("Correo", row.correo)
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(row.estado.ifBlank { "Activo" }, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                Text("Calif. ${row.calificacion}", style = MaterialTheme.typography.bodySmall)
                if (row.fechaRegistro.isNotBlank()) Text(row.fechaRegistro, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    if (value.isBlank()) return
    Text("$label: $value", style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

// ---------- Layout ancho (tablet): tabla ----------

@Composable
private fun SuppliersTable(
    suppliers: List<SupplierRow>,
    isLoading: Boolean,
    onEdit: (SupplierRow) -> Unit,
    onDelete: (SupplierRow) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        SupplierTableHeader()
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        if (suppliers.isEmpty() && !isLoading) {
            EmptyState()
        }
        LazyColumn(Modifier.fillMaxWidth()) {
            items(suppliers, key = { it.id }) { row ->
                SupplierTableRow(row = row, onEdit = { onEdit(row) }, onDelete = { onDelete(row) })
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
        Text("No hay proveedores registrados", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SupplierTableHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderCell("Código", 0.8f)
        HeaderCell("Razón social", 1.8f)
        HeaderCell("RUC", 1.1f)
        HeaderCell("Correo", 1.8f)
        HeaderCell("Teléfono", 1.1f)
        HeaderCell("Estado", 1.0f)
        HeaderCell("Calif.", 0.7f, TextAlign.Center)
        HeaderCell("Banco", 1.0f)
        HeaderCell("Fecha", 1.0f)
        HeaderCell("Acciones", 0.9f, TextAlign.End)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HeaderCell(
    text: String,
    weight: Float,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        text,
        modifier = Modifier.weight(weight).padding(horizontal = 4.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = align,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun SupplierTableRow(row: SupplierRow, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Cell(row.codigoProveedor, 0.8f)
        Cell(row.businessName, 1.8f, FontWeight.Medium)
        Cell(row.ruc, 1.1f)
        Cell(row.correo, 1.8f)
        Cell(row.phone, 1.1f)
        Cell(row.estado.ifBlank { "Activo" }, 1.0f)
        Cell(row.calificacion.toString(), 0.7f, align = TextAlign.Center)
        Cell(row.banco, 1.0f)
        Cell(row.fechaRegistro, 1.0f)
        Row(Modifier.weight(0.9f), horizontalArrangement = Arrangement.End) {
            CrudEditDeleteIcons(onEdit = onEdit, onDelete = onDelete, deleteContentDescription = "Desactivar proveedor")
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.Cell(
    text: String,
    weight: Float,
    fontWeight: FontWeight = FontWeight.Normal,
    align: TextAlign = TextAlign.Start,
) {
    Text(
        text.ifBlank { "-" },
        modifier = Modifier.weight(weight).padding(horizontal = 4.dp),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = fontWeight,
        textAlign = align,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
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
                OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Codigo proveedor") }, modifier = Modifier.fillMaxWidth())
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
            IconButton(onClick = {
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
            }) { Icon(Icons.Filled.Check, contentDescription = "Guardar") }
        },
        dismissButton = { IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") } },
    )
}