package com.ecommerce.ecommerceposapp.presentation.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val QuickClientAccent = Color(0xFFFD0505)
private val QuickClientMuted = Color(0xFF64748B)

private enum class ClientFormMode(val label: String) {
    BASICO("Básico"),
    AVANZADO("Avanzado"),
}

/** Coloca dos campos lado a lado cuando hay espacio suficiente; en pantallas angostas los apila. */
@Composable
private fun FieldPair(
    compact: Boolean,
    first: @Composable () -> Unit,
    second: (@Composable () -> Unit)? = null,
) {
    if (compact || second == null) {
        first()
        second?.invoke()
    } else {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.weight(1f)) { first() }
            Box(Modifier.weight(1f)) { second() }
        }
    }
}

/**
 * Panel lateral (desliza desde la derecha) para el alta rápida de cliente desde el carrito.
 * Ofrece un modo "Básico" (campos esenciales) y "Avanzado" (todos los campos del CRUD de clientes).
 */
@Composable
fun QuickAddClientDialog(
    isSaving: Boolean = false,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onSave: (ClientRow) -> Unit,
) {
    val compact = LocalConfiguration.current.screenWidthDp < 560
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    fun close() {
        visible = false
        scope.launch { delay(220); onDismiss() }
    }

    var mode by remember { mutableStateOf(ClientFormMode.BASICO) }

    var personType by remember { mutableStateOf("Natural") }
    var documentType by remember { mutableStateOf("DNI") }
    var document by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var maritalStatus by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }
    var webAccess by remember { mutableStateOf(false) }

    val canSave = name.isNotBlank() && document.isNotBlank() && (!webAccess || email.isNotBlank())

    Dialog(
        onDismissRequest = { close() },
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
    ) {
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200)),
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0x8A000000))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { close() },
                )
            }
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(animationSpec = tween(260), initialOffsetX = { it }),
                exit = slideOutHorizontally(animationSpec = tween(220), targetOffsetX = { it }),
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(if (compact) 0.96f else 0.42f)
                        .widthIn(min = 320.dp, max = 460.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                    shadowElevation = 8.dp,
                ) {
                    Column(Modifier.fillMaxSize().padding(20.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Nuevo cliente", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                            IconButton(onClick = { close() }) {
                                Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        TabRow(selectedTabIndex = mode.ordinal) {
                            ClientFormMode.entries.forEach { option ->
                                Tab(
                                    selected = mode == option,
                                    onClick = { mode = option },
                                    text = { Text(option.label) },
                                )
                            }
                        }
                        Column(
                            Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            FieldPair(
                                compact,
                                {
                                    SelectField("Tipo de persona", personType, listOf("Natural", "Juridica")) {
                                        personType = it
                                        documentType = documentTypeForPersonChange(it, documentType)
                                        document = ""
                                    }
                                },
                                {
                                    SelectField("Tipo de documento", documentType, documentTypeOptionsFor(personType), enabled = personType != "Juridica") { documentType = it; document = "" }
                                },
                            )
                            FieldPair(
                                compact || mode == ClientFormMode.BASICO,
                                {
                                    ClientTextField(
                                        "Nro. de documento", document,
                                        { document = filterDocumentInput(documentType, it) },
                                        if (documentType == "CE") KeyboardType.Text else KeyboardType.Number,
                                        isError = document.isNotBlank() && !isDocumentValid(documentType, document),
                                        supportingText = documentHelperText(documentType),
                                    )
                                },
                                { ClientTextField("Telefono", phone, { phone = it.filter(Char::isDigit).take(15) }, KeyboardType.Phone) },
                            )
                            FieldPair(
                                compact || mode == ClientFormMode.BASICO,
                                { ClientTextField("Nombre", name, { name = it }) },
                                { ClientTextField("Apellido", lastName, { lastName = it }) },
                            )
                            if (personType == "Juridica") ClientTextField("Razon social", businessName, { businessName = it })

                            if (mode == ClientFormMode.AVANZADO) {
                                FieldPair(
                                    compact,
                                    { ClientTextField("Direccion", address, { address = it }) },
                                    { ClientTextField("Alias", alias, { alias = it }) },
                                )
                                FieldPair(
                                    compact,
                                    { SelectField("Genero", gender, listOf("", "Masculino", "Femenino", "Otro")) { gender = it } },
                                    { SelectField("Estado civil", maritalStatus, listOf("", "Soltero", "Casado", "Conviviente", "Divorciado", "Viudo")) { maritalStatus = it } },
                                )
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
                                    Text("Acceso al sistema (web / app)", Modifier.weight(1f))
                                    Switch(webAccess, { webAccess = it })
                                }
                                if (webAccess) {
                                    Text(
                                        "Se enviara un correo para que el cliente cree su propia contrasena.",
                                        color = QuickClientMuted,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            }

                            errorMessage?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(onClick = { close() }, enabled = !isSaving) { Text("Cancelar") }
                            Spacer(Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    onSave(
                                        ClientRow(
                                            id = 0L,
                                            name = name.trim(),
                                            document = document.trim(),
                                            phone = phone.trim(),
                                            active = active,
                                            lastName = lastName.trim(),
                                            email = email.trim(),
                                            address = address.trim(),
                                            businessName = businessName.trim(),
                                            personType = personType,
                                            documentType = documentType,
                                            alias = alias.trim(),
                                            gender = gender,
                                            maritalStatus = maritalStatus,
                                            observations = observations.trim(),
                                            webAccess = webAccess,
                                        ),
                                    )
                                },
                                enabled = canSave && !isSaving,
                                colors = ButtonDefaults.buttonColors(containerColor = QuickClientAccent, contentColor = Color.White),
                            ) {
                                if (isSaving) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}
