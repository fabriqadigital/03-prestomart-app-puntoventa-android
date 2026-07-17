package com.ecommerce.ecommerceposapp.presentation.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm

private val ClientsAccent = Color(0xFFFD0505)
private val ClientsText = Color(0xFF111827)
private val ClientsMuted = Color(0xFF64748B)
private val ClientsDivider = Color(0xFFE2E8F0)

@Composable
fun ClientsCrudScreen(vm: ClientsViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    var editing by remember { mutableStateOf<ClientRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    val filteredClients = state.clients.filter { client ->
        search.isBlank() || listOf(client.displayName(), client.email)
            .any { it.contains(search, ignoreCase = true) }
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        ClientsHeader(onAdd = { showCreate = true })
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        state.message?.let { Text(it, color = ClientsAccent, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(12.dp))
        ClientsTable(
            clients = filteredClients,
            search = search,
            onSearch = { search = it },
            isLoading = state.isLoading,
            onEdit = { editing = it },
            onDelete = { client ->
                pendingConfirm = PendingConfirm(
                    title = "Eliminar cliente",
                    body = "Eliminar a ${client.displayName()}? Dejara de mostrarse en el listado.",
                    confirmButtonText = "Eliminar",
                    onConfirm = { vm.remove(client.id) },
                )
            },
        )
    }

    if (showCreate) {
        ClientEditDialog(
            initial = ClientRow(0, "", "", ""),
            onDismiss = { showCreate = false; vm.clearMessages() },
            onSave = { vm.save(it); showCreate = false },
        )
    }
    editing?.let { client ->
        ClientEditDialog(
            initial = client,
            onDismiss = { editing = null; vm.clearMessages() },
            onSave = { vm.save(it); editing = null },
        )
    }
    ConfirmDestructiveDialog(pendingConfirm) { pendingConfirm = null }
}

@Composable
private fun ClientsHeader(onAdd: () -> Unit) {
    val compact = LocalConfiguration.current.screenWidthDp < 600
    if (compact) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ClientsTitle()
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar")
            }
        }
    } else {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            ClientsTitle()
            Button(
                onClick = onAdd,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar")
            }
        }
    }
}

@Composable
private fun ClientsTitle() {
    Column {
        Text("Clientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Administra los clientes disponibles para tus ventas.", color = ClientsMuted)
    }
}

@Composable
private fun ClientsTable(
    clients: List<ClientRow>,
    search: String,
    onSearch: (String) -> Unit,
    isLoading: Boolean,
    onEdit: (ClientRow) -> Unit,
    onDelete: (ClientRow) -> Unit,
) {
    val compact = LocalConfiguration.current.screenWidthDp < 760
    var pageSize by remember { mutableStateOf(10) }
    var pageSizeExpanded by remember { mutableStateOf(false) }
    var currentPage by remember(clients.size, pageSize, search) { mutableStateOf(0) }
    val totalPages = maxOf(1, (clients.size + pageSize - 1) / pageSize)
    val pageClients = clients.drop(currentPage * pageSize).take(pageSize)

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = ClientsText,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column {
            OutlinedTextField(
                value = search,
                onValueChange = onSearch,
                placeholder = { Text("Buscar por nombre o correo") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.padding(10.dp).then(if (compact) Modifier.fillMaxWidth() else Modifier.widthIn(280.dp, 420.dp)),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
            )
            HorizontalDivider(color = ClientsDivider)
            Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Nombre", modifier = Modifier.weight(1.7f), fontWeight = FontWeight.SemiBold)
                Text("Correo", modifier = Modifier.weight(1.7f), fontWeight = FontWeight.SemiBold)
                Text("Estado", modifier = Modifier.weight(.8f), fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(48.dp))
            }
            HorizontalDivider(color = ClientsDivider)
            Box(Modifier.fillMaxWidth().weight(1f).background(Color.White), contentAlignment = Alignment.Center) {
                when {
                    isLoading -> CircularProgressIndicator(color = ClientsAccent)
                    pageClients.isEmpty() -> Text("No se encontraron clientes", color = ClientsMuted)
                    else -> LazyColumn(Modifier.fillMaxSize()) {
                        items(pageClients, key = { it.id }) { client ->
                            ClientTableRow(client, onEdit, onDelete)
                            HorizontalDivider(color = ClientsDivider)
                        }
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val from = if (clients.isEmpty()) 0 else currentPage * pageSize + 1
                val to = minOf(clients.size, (currentPage + 1) * pageSize)
                if (!compact) Text("Registros por pagina:", color = ClientsMuted)
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = ClientsText) }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                        DropdownMenu(pageSizeExpanded, { pageSizeExpanded = false }) {
                            listOf(10, 20, 50).forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size.toString()) },
                                    onClick = { pageSize = size; pageSizeExpanded = false },
                                )
                            }
                        }
                    }
                }
                Text("$from-$to de ${clients.size}", color = ClientsMuted, modifier = Modifier.weight(1f))
                if (!compact) Text("Pagina ${currentPage + 1} de $totalPages", color = ClientsMuted)
                IconButton(onClick = { currentPage-- }, enabled = currentPage > 0) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
                }
                IconButton(onClick = { currentPage++ }, enabled = currentPage < totalPages - 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
                }
            }
        }
    }
}

@Composable
private fun ClientTableRow(
    client: ClientRow,
    onEdit: (ClientRow) -> Unit,
    onDelete: (ClientRow) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(Color.White).clickable { onEdit(client) }.padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(client.displayName(), modifier = Modifier.weight(1.7f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(client.email.ifBlank { "-" }, modifier = Modifier.weight(1.7f), color = ClientsMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(if (client.active) "Activo" else "Inactivo", modifier = Modifier.weight(.8f), color = if (client.active) Color(0xFF15803D) else ClientsMuted)
        Box {
            IconButton(onClick = { menuExpanded = true }) { Icon(Icons.Filled.MoreVert, contentDescription = "Opciones") }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(menuExpanded, { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Editar") }, onClick = { menuExpanded = false; onEdit(client) })
                    DropdownMenuItem(text = { Text("Eliminar") }, onClick = { menuExpanded = false; onDelete(client) })
                }
            }
        }
    }
}

private fun ClientRow.displayName(): String {
    val personName = listOf(name.cleanDisplayValue(), lastName.cleanDisplayValue()).filter { it.isNotBlank() }.joinToString(" ")
    return personName.ifBlank { email.cleanDisplayValue() }.ifBlank { "Usuario sin nombre" }
}

private fun String.cleanDisplayValue(): String = trim().takeUnless { it.equals("null", true) } ?: ""

@Composable
private fun ClientEditDialog(initial: ClientRow, onDismiss: () -> Unit, onSave: (ClientRow) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial.name.cleanDisplayValue()) }
    var email by remember(initial) { mutableStateOf(initial.email.cleanDisplayValue()) }
    var password by remember(initial) { mutableStateOf("") }
    var active by remember(initial) { mutableStateOf(initial.active) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Agregar cliente" else "Editar cliente") },
        text = {
            Column(
                Modifier.heightIn(max = 560.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(
                    password,
                    { password = it },
                    label = { Text(if (initial.id == 0L) "Contrasena" else "Nueva contrasena (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (active) "Cliente activo" else "Cliente inactivo", modifier = Modifier.weight(1f))
                    Switch(checked = active, onCheckedChange = { active = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        initial.copy(
                            name = name.trim(), email = email.trim(), active = active, newPassword = password,
                        ),
                    )
                },
                enabled = name.isNotBlank() && email.isNotBlank() && (initial.id > 0L || password.isNotBlank()),
                colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
            ) { Text(if (initial.id == 0L) "Agregar" else "Guardar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } },
        containerColor = Color.White,
        tonalElevation = 0.dp,
    )
}
