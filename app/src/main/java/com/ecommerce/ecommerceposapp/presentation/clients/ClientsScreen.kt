package com.ecommerce.ecommerceposapp.presentation.clients

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import kotlinx.coroutines.delay

private val ClientsAccent = Color(0xFFFD0505)
private val ClientsText = Color(0xFF111827)
private val ClientsMuted = Color(0xFF64748B)
private val ClientsDivider = Color(0xFFE2E8F0)

@Composable
fun ClientsCrudScreen(vm: ClientsViewModel) {
    val state by vm.uiState.collectAsState()
    var editing by remember { mutableStateOf<ClientRow?>(null) }
    var creating by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    val compact = LocalConfiguration.current.screenWidthDp < 760
    val smallScreen = LocalConfiguration.current.screenWidthDp < 480

    LaunchedEffect(search) {
        delay(350)
        vm.load(page = 1, perPage = state.perPage, search = search)
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(if (smallScreen) 8.dp else if (compact) 12.dp else 16.dp)) {
        ClientsHeader(compact, smallScreen) { creating = true }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        state.message?.let { Text(it, color = ClientsAccent, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Buscar por nombre, documento o correo") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = if (compact) Modifier.fillMaxWidth() else Modifier.widthIn(320.dp, 480.dp),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
        )
        Spacer(Modifier.height(10.dp))
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ClientsAccent) }
            state.clients.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No se encontraron clientes", color = ClientsMuted) }
            else -> ClientTable(
                clients = state.clients,
                total = state.total,
                pageSize = state.perPage,
                currentPage = state.page - 1,
                compact = compact,
                onPageSize = { vm.load(page = 1, perPage = it, search = search) },
                onPageChange = { vm.load(page = it + 1, perPage = state.perPage, search = search) },
                onEdit = { editing = it },
                onDelete = { client -> pendingConfirm = deleteConfirmation(client, vm) },
            )
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
private fun ClientsHeader(compact: Boolean, smallScreen: Boolean, onAdd: () -> Unit) {
    val title: @Composable () -> Unit = {
        Column {
            Text("Clientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Clientes del punto de venta.", color = ClientsMuted)
        }
    }
    val button: @Composable () -> Unit = {
        Button(
            onClick = onAdd,
            modifier = if (compact && !smallScreen) Modifier.fillMaxWidth() else Modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ClientsAccent, contentColor = Color.White),
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (smallScreen) "Nuevo" else "Nuevo cliente")
        }
    }
    if (smallScreen) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Clientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        button()
    } else if (compact) Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { title(); button() }
    else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { title(); button() }
}

@Composable
private fun ClientTable(
    clients: List<ClientRow>,
    total: Int,
    pageSize: Int,
    currentPage: Int,
    compact: Boolean,
    onPageSize: (Int) -> Unit,
    onPageChange: (Int) -> Unit,
    onEdit: (ClientRow) -> Unit,
    onDelete: (ClientRow) -> Unit,
) {
    val totalPages = maxOf(1, (total + pageSize - 1) / pageSize)
    Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(8.dp), color = Color.White, shadowElevation = 1.dp) {
        Column {
            Row(Modifier.fillMaxWidth().background(Color(0xFFF8FAFC)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text("Nombre", Modifier.weight(if (compact) 1.5f else 1.8f), fontWeight = FontWeight.SemiBold)
                Text("Documento", Modifier.weight(1.1f), fontWeight = FontWeight.SemiBold)
                if (!compact) Text("Correo", Modifier.weight(1.7f), fontWeight = FontWeight.SemiBold)
                if (!compact) Text("Estado", Modifier.weight(.7f), fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(if (compact) 44.dp else 56.dp))
            }
            HorizontalDivider(color = ClientsDivider)
            LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                items(clients, key = { it.id }) { client ->
                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(client.displayName(), Modifier.weight(if (compact) 1.5f else 1.8f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${client.documentType} ${client.document}", Modifier.weight(1.1f), color = ClientsMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (!compact) Text(client.email.ifBlank { "-" }, Modifier.weight(1.7f), color = ClientsMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (!compact) Text(if (client.active) "Activo" else "Inactivo", Modifier.weight(.7f), color = if (client.active) Color(0xFF15803D) else ClientsMuted)
                        ClientRowMenu(client, onEdit, onDelete)
                    }
                    HorizontalDivider(color = ClientsDivider)
                }
            }
            ClientsPaginationBar(
                total = total,
                pageSize = pageSize,
                currentPage = currentPage,
                totalPages = totalPages,
                onPageSize = onPageSize,
                onPageChange = onPageChange,
                compact = compact,
            )
        }
    }
}

@Composable
private fun ClientRowMenu(client: ClientRow, onEdit: (ClientRow) -> Unit, onDelete: (ClientRow) -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "Opciones")
        }
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = { menuExpanded = false; onEdit(client) },
            )
            DropdownMenuItem(
                text = { Text("Eliminar") },
                onClick = { menuExpanded = false; onDelete(client) },
            )
        }
    }
}

@Composable
private fun ClientsPaginationBar(
    total: Int,
    pageSize: Int,
    currentPage: Int,
    totalPages: Int,
    onPageSize: (Int) -> Unit,
    onPageChange: (Int) -> Unit,
    compact: Boolean,
) {
    var pageSizeExpanded by remember { mutableStateOf(false) }
    val from = if (total == 0) 0 else currentPage * pageSize + 1
    val to = minOf(total, (currentPage + 1) * pageSize)
    val info: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Filas:", color = ClientsMuted)
            Box {
                TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = ClientsText) }
                DropdownMenu(expanded = pageSizeExpanded, onDismissRequest = { pageSizeExpanded = false }) {
                    listOf(20, 50, 100).forEach { size ->
                        DropdownMenuItem(text = { Text(size.toString()) }, onClick = { onPageSize(size); pageSizeExpanded = false })
                    }
                }
            }
            Text("$from-$to de $total", color = ClientsMuted)
        }
    }
    val buttons: @Composable () -> Unit = {
        IconButton(onClick = { onPageChange(currentPage - 1) }, enabled = currentPage > 0) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
        }
        IconButton(onClick = { onPageChange(currentPage + 1) }, enabled = currentPage < totalPages - 1) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
        }
    }
    if (compact) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            info()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${currentPage + 1}/$totalPages", color = ClientsMuted)
                buttons()
            }
        }
    } else {
        Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            info()
            Spacer(Modifier.weight(1f))
            Text("Pagina ${currentPage + 1} de $totalPages", color = ClientsMuted)
            buttons()
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

private fun filterDocumentInput(documentType: String, raw: String): String = when (documentType) {
    "DNI" -> raw.filter(Char::isDigit).take(8)
    "RUC" -> raw.filter(Char::isDigit).take(11)
    "CE" -> raw.filter(Char::isLetterOrDigit).take(16)
    else -> raw.filter(Char::isLetterOrDigit)
}

private fun isDocumentValid(documentType: String, document: String): Boolean = when (documentType) {
    "DNI" -> document.length == 8
    "RUC" -> document.length == 11
    "CE" -> document.isNotBlank() && document.length <= 16
    else -> document.isNotBlank()
}

private fun documentHelperText(documentType: String): String = when (documentType) {
    "DNI" -> "Debe tener 8 digitos"
    "RUC" -> "Debe tener 11 digitos"
    "CE" -> "Letras y numeros, maximo 16 caracteres"
    else -> ""
}

private fun isEmailValid(email: String): Boolean = email.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email).matches()

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
        ClientTextField(
            "Nro. de documento", document,
            { document = filterDocumentInput(documentType, it) },
            if (documentType == "CE") KeyboardType.Text else KeyboardType.Number,
            isError = document.isNotBlank() && !isDocumentValid(documentType, document),
            supportingText = documentHelperText(documentType),
        )
        ClientTextField("Nombre", name, { name = it })
        ClientTextField("Apellido", lastName, { lastName = it })
        if (personType == "Juridica") ClientTextField("Razon social", businessName, { businessName = it })
        ClientTextField("Telefono", phone, { phone = it.filter(Char::isDigit).take(15) }, KeyboardType.Phone)
        ClientTextField("Direccion", address, { address = it })
        ClientTextField("Alias", alias, { alias = it })
        SelectField("Genero", gender, listOf("", "Masculino", "Femenino", "Otro")) { gender = it }
        SelectField("Estado civil", maritalStatus, listOf("", "Soltero", "Casado", "Conviviente", "Divorciado", "Viudo")) { maritalStatus = it }
        // ClientTextField("Descuento (%)", discount, { value -> if (value.matches(Regex("^\\d{0,3}([.]\\d{0,2})?$"))) discount = value }, KeyboardType.Decimal)
        ClientTextField(
            "Correo", email, { email = it }, KeyboardType.Email,
            isError = !isEmailValid(email),
            supportingText = if (!isEmailValid(email)) "Formato de correo invalido" else null,
        )
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
                            ClientTextField(
                                "Nro. de documento", document,
                                { document = filterDocumentInput(documentType, it) },
                                if (documentType == "CE") KeyboardType.Text else KeyboardType.Number,
                                isError = document.isNotBlank() && !isDocumentValid(documentType, document),
                                supportingText = documentHelperText(documentType),
                            )
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
                            // ClientTextField("Descuento (%)", discount, { value -> if (value.matches(Regex("^\\d{0,3}([.]\\d{0,2})?$"))) discount = value }, KeyboardType.Decimal)
                            ClientTextField(
                                "Correo", email, { email = it }, KeyboardType.Email,
                                isError = !isEmailValid(email),
                                supportingText = if (!isEmailValid(email)) "Formato de correo invalido" else null,
                            )
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
private fun ClientTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    OutlinedTextField(
        value,
        onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
    )
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
            trailingIcon = { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { expanded = true },
        )
        DropdownMenu(expanded, { expanded = false }, modifier = Modifier.widthIn(min = 220.dp)) {
            options.forEach { option -> DropdownMenuItem(text = { Text(option.ifBlank { "No especificado" }) }, onClick = { onSelected(option); expanded = false }) }
        }
    }
}
