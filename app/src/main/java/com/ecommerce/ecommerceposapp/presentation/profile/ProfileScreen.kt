package com.ecommerce.ecommerceposapp.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.data.remote.api.CashierProfileApiDataSource
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Brand = Color(0xFFFD0505)

@Composable
fun ProfileScreen(session: UserSession, onLogout: () -> Unit, onNotice: (String) -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var resolvedSession by remember(session) { mutableStateOf(session) }
    var editing by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    var name by remember(session) { mutableStateOf(session.name) }
    var lastName by remember(session) { mutableStateOf(session.lastName) }
    var email by remember(session) { mutableStateOf(session.email) }
    var document by remember(session) { mutableStateOf(session.document) }
    var phone by remember(session) { mutableStateOf(session.phone) }
    var address by remember(session) { mutableStateOf(session.address) }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    LaunchedEffect(session.cashierId) {
        if (session.offlineSession) return@LaunchedEffect
        withContext(Dispatchers.IO) { CashierProfileApiDataSource(context).fetch(session) }
            .onSuccess { fresh ->
                resolvedSession = fresh; name = fresh.name; lastName = fresh.lastName; email = fresh.email
                document = fresh.document; phone = fresh.phone; address = fresh.address
            }
            .onFailure { onNotice(it.message ?: "No se pudo cargar la ficha completa del cajero.") }
    }
    val photoKey = "profile_photo_${session.id}"
    var photo by remember(session.id) { mutableStateOf(context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE).getString(photoKey, "").orEmpty()) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        runCatching { savePhoto(context, session.id, uri) }.onSuccess {
            photo = it; context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE).edit().putString(photoKey, it).apply(); onNotice("Foto actualizada.")
        }.onFailure { onNotice("No se pudo guardar la foto.") }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column { Text("Mi perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Información de tu cuenta de cajero", color = Color(0xFF64748B)) }
            if (!editing) Button(onClick = { editing = true }, colors = ButtonDefaults.buttonColors(Brand), shape = RoundedCornerShape(10.dp)) { Icon(Icons.Filled.Edit, null); Spacer(Modifier.width(6.dp)); Text("Editar") }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(Modifier.size(112.dp), shape = CircleShape, color = Color(0xFFFFE4E6)) {
                    if (photo.isNotBlank()) AsyncImage(photo, "Foto", Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                    else Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.Person, null, Modifier.size(60.dp), tint = Brand) }
                }
                if (editing) IconButton(onClick = { picker.launch("image/*") }, modifier = Modifier.size(40.dp), colors = IconButtonDefaults.iconButtonColors(containerColor = Brand, contentColor = Color.White)) { Icon(Icons.Filled.CameraAlt, "Cambiar foto") }
            }
            Column { Text("$name $lastName".trim(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(resolvedSession.role, color = Color(0xFF64748B)); Text(resolvedSession.defaultCashRegisterName.ifBlank { "Sin caja asignada" }, color = Brand) }
        }
        HorizontalDivider(color = Color(0xFFE2E8F0))
        if (!editing) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ReadRow("Nombres", name); ReadRow("Apellidos", lastName); ReadRow("Correo electrónico", email); ReadRow("Tipo de documento", resolvedSession.documentType); ReadRow("Número de documento", document); ReadRow("Número de celular", phone); ReadRow("Dirección", address); ReadRow("Sucursal", resolvedSession.branchName); ReadRow("Caja asignada", resolvedSession.defaultCashRegisterName); ReadRow("Rol", resolvedSession.role)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Información personal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(name, { name = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(lastName, { lastName = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(email, { email = it }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { OutlinedTextField(document, { document = it }, label = { Text("Documento") }, modifier = Modifier.weight(1f), singleLine = true); OutlinedTextField(phone, { phone = it }, label = { Text("Celular") }, modifier = Modifier.weight(1f), singleLine = true) }
                OutlinedTextField(address, { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                ReadRow("Sucursal", resolvedSession.branchName); ReadRow("Caja asignada", resolvedSession.defaultCashRegisterName)
                Text("Cambiar contraseña", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(password, { password = it }, label = { Text("Nueva contraseña (opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(confirmation, { confirmation = it }, label = { Text("Confirmar contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { editing = false; name = resolvedSession.name; lastName = resolvedSession.lastName; email = resolvedSession.email; document = resolvedSession.document; phone = resolvedSession.phone; address = resolvedSession.address }) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (password.isNotBlank() && password != confirmation) { onNotice("Las contraseñas no coinciden."); return@Button }
                        scope.launch { saving = true; val result = withContext(Dispatchers.IO) { CashierProfileApiDataSource(context).update(resolvedSession, name, lastName, email, document, phone, address, password) }; saving = false; result.onSuccess { editing = false; password = ""; confirmation = ""; onNotice("Perfil actualizado correctamente.") }.onFailure { onNotice(it.message ?: "No se pudo actualizar el perfil.") } }
                    }, enabled = !saving && name.isNotBlank() && email.isNotBlank(), colors = ButtonDefaults.buttonColors(Brand)) { if (saving) CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp) else Text("Guardar cambios") }
                }
            }
        }
    }
}

@Composable private fun ReadRow(label: String, value: String) { Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(label, color = Color(0xFF64748B)); Text(value.ifBlank { "No registrado" }, fontWeight = FontWeight.SemiBold) }; HorizontalDivider(color = Color(0xFFF1F5F9)) }
private fun savePhoto(context: Context, userId: Long, uri: Uri): String { val dir = File(context.filesDir, "profile_photos").apply { mkdirs() }; val target = File(dir, "cashier_$userId.jpg"); context.contentResolver.openInputStream(uri)?.use { i -> target.outputStream().use(i::copyTo) } ?: error("Imagen inválida"); return "file://${target.absolutePath}" }
