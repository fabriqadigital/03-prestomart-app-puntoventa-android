package com.ecommerce.ecommerceposapp.presentation.clients

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm

private val ClientsAccent = com.ecommerce.ecommerceposapp.ui.theme.BrandRed
private val ClientsText = com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
private val ClientsMuted = com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
private val ClientsDivider = com.ecommerce.ecommerceposapp.ui.theme.BorderDefault

@Composable
fun ClientsCrudScreen(vm: ClientsViewModel) {
    val state by vm.uiState.collectAsState()
    var editing by remember { mutableStateOf<ClientRow?>(null) }
    var creating by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    val compact = LocalConfiguration.current.screenWidthDp < 760

    LaunchedEffect(Unit) { vm.load() }
    val clients = state.clients.filter { client ->
        search.isBlank() || listOf(client.displayName(), client.email, client.document, client.phone)
            .any { it.contains(search, ignoreCase = true) }
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(if (compact) 12.dp else 16.dp)) {
        ClientsHeader(compact) { creating = true }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        state.message?.let { Text(it, color = ClientsAccent, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Buscar por nombre, documento o correo") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = com.ecommerce.ecommerceposapp.ui.theme.TextSecondary) },
            modifier = if (compact) Modifier.fillMaxWidth() else Modifier.widthIn(320.dp, 480.dp),
            singleLine = true,
            shape = RoundedCornerShape(com.ecommerce.ecommerceposapp.ui.theme.Radius.lg),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = com.ecommerce.ecommerceposapp.ui.theme.BrandRed,
                unfocusedBorderColor = com.ecommerce.ecommerceposapp.ui.theme.BorderDefault,
                cursorColor          = com.ecommerce.ecommerceposapp.ui.theme.BrandRed,
                focusedLabelColor    = com.ecommerce.ecommerceposapp.ui.theme.BrandRed,
                focusedContainerColor   = com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite,
                unfocusedContainerColor = com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite,
            ),
        )
        Spacer(Modifier.height(10.dp))
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ClientsAccent) }
            clients.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No se encontraron clientes", color = ClientsMuted) }
            compact -> ClientCards(clients, { editing = it }) { client -> pendingConfirm = deleteConfirmation(client, vm) }
            else -> ClientTable(clients, { editing = it }) { client -> pendingConfirm = deleteConfirmation(client, vm) }
        }
    }

    if (creating) {
        ClientEditDialog(
            initial = ClientRow(0L, "", "", ""),
            onDismiss = { creating = false; vm.clearMessages() },
            onSave = { vm.save(it); creating = false },
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
private fun ClientsHeader(compact: Boolean, onAdd: () -> Unit) {
    val title: @Composable () -> Unit = {
        Column {
            Text("Clientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Clientes del punto de venta y cuentas web vinculadas.", color = ClientsMuted)
        }
    }
    val button: @Composable () -> Unit = {
        Button(
            onClick = onAdd,
            modifier = if (compact) Modifier.fillMaxWidth() else Modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Nuevo cliente")
        }
    }
    if (compact) Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { title(); button() }
    else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { title(); button() }
}

@Composable
private fun ClientCards(clients: List<ClientRow>, onEdit: (ClientRow) -> Unit, onDelete: (ClientRow) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(clients, key = { it.id }) { client ->
            Surface(shape = RoundedCornerShape(10.dp), color = Color.White, shadowElevation = 1.dp) {
                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(client.displayName(), color = ClientsText, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${client.documentType}: ${client.document}", color = ClientsMuted)
                        Text(if (client.webAccess) "Cuenta web vinculada" else "Solo cliente POS", color = if (client.webAccess) Color(0xFF15803D) else ClientsMuted)
                    }
                    IconButton(onClick = { onEdit(client) }) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
                    IconButton(onClick = { onDelete(client) }) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                }
            }
        }
    }
}

@Composable
private fun ClientTable(clients: List<ClientRow>, onEdit: (ClientRow) -> Unit, onDelete: (ClientRow) -> Unit) {
    Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(8.dp), color = Color.White, shadowElevation = 1.dp) {
        Column {
            Row(Modifier.fillMaxWidth().background(Color(0xFFF8FAFC)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Nombre", Modifier.weight(1.8f), fontWeight = FontWeight.SemiBold)
                Text("Documento", Modifier.weight(1.1f), fontWeight = FontWeight.SemiBold)
                Text("Correo", Modifier.weight(1.7f), fontWeight = FontWeight.SemiBold)
                Text("Acceso", Modifier.weight(.8f), fontWeight = FontWeight.SemiBold)
                Text("Estado", Modifier.weight(.7f), fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(96.dp))
            }
            HorizontalDivider(color = ClientsDivider)
            LazyColumn(Modifier.fillMaxSize()) {
                items(clients, key = { it.id }) { client ->
                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(client.displayName(), Modifier.weight(1.8f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${client.documentType} ${client.document}", Modifier.weight(1.1f), color = ClientsMuted)
                        Text(client.email.ifBlank { "-" }, Modifier.weight(1.7f), color = ClientsMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(if (client.webAccess) "Web" else "POS", Modifier.weight(.8f), color = if (client.webAccess) Color(0xFF15803D) else ClientsMuted)
                        Text(if (client.active) "Activo" else "Inactivo", Modifier.weight(.7f), color = if (client.active) Color(0xFF15803D) else ClientsMuted)
                        IconButton(onClick = { onEdit(client) }) { Icon(Icons.Filled.Edit, contentDescription = "Editar") }
                        IconButton(onClick = { onDelete(client) }) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                    }
                    HorizontalDivider(color = ClientsDivider)
                }
            }
        }
    }
}

private fun deleteConfirmation(client: ClientRow, vm: ClientsViewModel) = PendingConfirm(
    title = "Eliminar cliente",
    body = "Se inactivara la ficha POS de ${client.displayName()}. La cuenta web vinculada no se eliminara.",
    confirmButtonText = "Eliminar",
    onConfirm = { vm.remove(client.id) },
)

private fun ClientRow.displayName(): String {
    val fullName = listOf(name.cleanValue(), lastName.cleanValue()).filter(String::isNotBlank).joinToString(" ")
    return fullName.ifBlank { businessName.cleanValue() }.ifBlank { alias.cleanValue() }.ifBlank { email.cleanValue() }.ifBlank { "Cliente sin nombre" }
}

private fun String.cleanValue(): String = trim().takeUnless { it.equals("null", true) } ?: ""

@Composable
private fun ClientEditDialog(initial: ClientRow, onDismiss: () -> Unit, onSave: (ClientRow) -> Unit) {
    val compact = LocalConfiguration.current.screenWidthDp < 700
    var personType by remember(initial) { mutableStateOf(initial.personType.ifBlank { "Natural" }) }
    var documentType by remember(initial) { mutableStateOf(initial.documentType.ifBlank { "DNI" }) }
    var document by remember(initial) { mutableStateOf(initial.document.cleanValue()) }
    var name by remember(initial) { mutableStateOf(initial.name.cleanValue()) }
    var lastName by remember(initial) { mutableStateOf(initial.lastName.cleanValue()) }
    var businessName by remember(initial) { mutableStateOf(initial.businessName.cleanValue()) }
    var phone by remember(initial) { mutableStateOf(initial.phone.cleanValue()) }
    var address by remember(initial) { mutableStateOf(initial.address.cleanValue()) }
    var alias by remember(initial) { mutableStateOf(initial.alias.cleanValue()) }
    var email by remember(initial) { mutableStateOf(initial.email.cleanValue()) }
    var gender by remember(initial) { mutableStateOf(initial.gender.cleanValue()) }
    var maritalStatus by remember(initial) { mutableStateOf(initial.maritalStatus.cleanValue()) }
    var discount by remember(initial) { mutableStateOf(initial.discountPercentage.toString().removeSuffix(".0")) }
    var observations by remember(initial) { mutableStateOf(initial.observations.cleanValue()) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    var webAccess by remember(initial) { mutableStateOf(initial.webAccess) }

    val fields: @Composable () -> Unit = {
        SelectField("Tipo de persona", personType, listOf("Natural", "Juridica")) { personType = it }
        SelectField("Tipo de documento", documentType, listOf("DNI", "RUC", "CE")) { documentType = it; document = "" }
        ClientTextField("Nro. de documento", document, { document = it.filter(Char::isLetterOrDigit) }, KeyboardType.Text)
        ClientTextField("Nombre", name, { name = it })
        ClientTextField("Apellido", lastName, { lastName = it })
        if (personType == "Juridica") ClientTextField("Razon social", businessName, { businessName = it })
        ClientTextField("Telefono", phone, { phone = it.filter(Char::isDigit).take(15) }, KeyboardType.Phone)
        ClientTextField("Direccion", address, { address = it })
        ClientTextField("Alias", alias, { alias = it })
        SelectField("Genero", gender, listOf("", "Masculino", "Femenino", "Otro")) { gender = it }
        SelectField("Estado civil", maritalStatus, listOf("", "Soltero", "Casado", "Conviviente", "Divorciado", "Viudo")) { maritalStatus = it }
        ClientTextField("Descuento (%)", discount, { value -> if (value.matches(Regex("^\\d{0,3}([.]\\d{0,2})?$"))) discount = value }, KeyboardType.Decimal)
        ClientTextField("Correo", email, { email = it }, KeyboardType.Email)
        OutlinedTextField(observations, { observations = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Estado del cliente: ${if (active) "Activado" else "Inactivo"}", Modifier.weight(1f))
            Switch(active, { active = it })
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Acceso al sistema (web / app)")
                if (initial.userId > 0L) Text("Cuenta web vinculada", color = Color(0xFF15803D), style = MaterialTheme.typography.bodySmall)
            }
            Switch(webAccess, { webAccess = it }, enabled = initial.userId == 0L)
        }
        if (webAccess && initial.userId == 0L) {
            Text("Se enviara un correo para que el cliente cree su propia contrasena. Si la cuenta ya existe, se conservara su contrasena actual.", color = ClientsMuted, style = MaterialTheme.typography.bodySmall)
        }
    }

    val canSave = name.isNotBlank() && document.isNotBlank() && (!webAccess || email.isNotBlank())
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(if (compact) .98f else .96f).widthIn(max = 1180.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text(if (initial.id == 0L) "Nuevo cliente" else "Editar cliente") },
        text = {
            Column(Modifier.heightIn(max = if (compact) 640.dp else 800.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (compact) fields() else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SelectField("Tipo de persona", personType, listOf("Natural", "Juridica")) { personType = it }
                            SelectField("Tipo de documento", documentType, listOf("DNI", "RUC", "CE")) { documentType = it; document = "" }
                            ClientTextField("Nro. de documento", document, { document = it.filter(Char::isLetterOrDigit) })
                            ClientTextField("Nombre", name, { name = it })
                            ClientTextField("Apellido", lastName, { lastName = it })
                            if (personType == "Juridica") ClientTextField("Razon social", businessName, { businessName = it })
                            ClientTextField("Direccion", address, { address = it })
                            ClientTextField("Alias", alias, { alias = it })
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ClientTextField("Telefono", phone, { phone = it.filter(Char::isDigit).take(15) }, KeyboardType.Phone)
                            SelectField("Genero", gender, listOf("", "Masculino", "Femenino", "Otro")) { gender = it }
                            SelectField("Estado civil", maritalStatus, listOf("", "Soltero", "Casado", "Conviviente", "Divorciado", "Viudo")) { maritalStatus = it }
                            ClientTextField("Descuento (%)", discount, { value -> if (value.matches(Regex("^\\d{0,3}([.]\\d{0,2})?$"))) discount = value }, KeyboardType.Decimal)
                            ClientTextField("Correo", email, { email = it }, KeyboardType.Email)
                            OutlinedTextField(observations, { observations = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("Cliente ${if (active) "activo" else "inactivo"}", Modifier.weight(1f)); Switch(active, { active = it }) }
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(if (initial.userId > 0L) "Cuenta web vinculada" else "Activar acceso web / app", Modifier.weight(1f)); Switch(webAccess, { webAccess = it }, enabled = initial.userId == 0L) }
                            if (webAccess && initial.userId == 0L) Text("Se enviara una invitacion por correo para crear la contrasena.", color = ClientsMuted, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(initial.copy(
                        name = name.trim(), lastName = lastName.trim(), document = document.trim(), phone = phone.trim(),
                        email = email.trim(), address = address.trim(), businessName = businessName.trim(), alias = alias.trim(),
                        personType = personType, documentType = documentType, gender = gender, maritalStatus = maritalStatus,
                        discountPercentage = discount.toDoubleOrNull()?.coerceIn(0.0, 100.0) ?: 0.0,
                        observations = observations.trim(), active = active, webAccess = webAccess, newPassword = "",
                    ))
                },
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
            ) { Text("Guardar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } },
        containerColor = Color.White,
        tonalElevation = 0.dp,
    )
}

@Composable
private fun ClientTextField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(value, onValueChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboardType))
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
            trailingIcon = { IconButton(onClick = { expanded = true }) { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        DropdownMenu(expanded, { expanded = false }, modifier = Modifier.widthIn(min = 220.dp)) {
            options.forEach { option -> DropdownMenuItem(text = { Text(option.ifBlank { "No especificado" }) }, onClick = { onSelected(option); expanded = false }) }
        }
    }
}
