package com.ecommerce.ecommerceposapp.presentation.profile

import android.net.Uri
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.data.remote.api.CashierProfileApiDataSource
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Brand = Color(0xFFFD0505)
private val BrandDark = Color(0xFFA82024)
private val TextPrimary = Color(0xFF111827)
private val Muted = Color(0xFF64748B)
private val Border = Color(0xFFE6DADA)

private enum class ProfileMode { View, Edit, Security }
private enum class ProfileSection(val label: String) {
    Personal("Información personal"),
    Work("Datos laborales"),
    Collection("Recaudación"),
}

private data class ProfileCashStats(
    val salesCount: Int = 0,
    val totalCollected: Double = 0.0,
    val currentCashBalance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val cash: Double = 0.0,
    val yape: Double = 0.0,
    val plin: Double = 0.0,
    val card: Double = 0.0,
)

private fun loadProfileCashStats(
    repository: CatalogRepository,
    cashierId: Long,
): Result<ProfileCashStats> = runCatching {
    val openSession = repository.findOpenCashSession(cashierId).getOrThrow()
        ?: return@runCatching ProfileCashStats()
    val summary = repository.cashSummary(openSession.id).getOrThrow()
    ProfileCashStats(
        salesCount = summary.salesCount,
        totalCollected = summary.totalSales,
        currentCashBalance = summary.expectedCash,
        income = summary.income,
        expenses = summary.expenses,
        cash = summary.cashAmount,
        yape = summary.yapeAmount,
        plin = summary.plinAmount,
        card = summary.cardAmount,
    )
}

@Composable
fun ProfileScreen(
    session: UserSession,
    catalogRepository: CatalogRepository,
    onSessionUpdated: (UserSession) -> Unit,
    onNotice: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val profileApi = remember(context) { CashierProfileApiDataSource(context.applicationContext) }
    fun hasValidatedInternet(): Boolean {
        val connectivity = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivity.activeNetwork ?: return false
        val capabilities = connectivity.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    val scope = rememberCoroutineScope()
    var resolvedSession by remember(session) { mutableStateOf(session) }
    var mode by remember { mutableStateOf(ProfileMode.View) }
    var saving by remember { mutableStateOf(false) }
    var name by remember(session) { mutableStateOf(session.name.cleanDisplay()) }
    var lastName by remember(session) { mutableStateOf(session.lastName.cleanDisplay()) }
    var email by remember(session) { mutableStateOf(session.email.cleanDisplay()) }
    var document by remember(session) { mutableStateOf(session.document.cleanDisplay()) }
    var phone by remember(session) { mutableStateOf(session.phone.cleanDisplay()) }
    var address by remember(session) { mutableStateOf(session.address.cleanDisplay()) }
    var currentPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var cashStats by remember { mutableStateOf(ProfileCashStats()) }

    fun resetForm(source: UserSession = resolvedSession) {
        name = source.name.cleanDisplay()
        lastName = source.lastName.cleanDisplay()
        email = source.email.cleanDisplay()
        document = source.document.cleanDisplay()
        phone = source.phone.cleanDisplay()
        address = source.address.cleanDisplay()
        currentPassword = ""
        password = ""
        confirmation = ""
        pendingPhotoUri = null
    }

    LaunchedEffect(session.cashierId) {
        if (session.offlineSession) return@LaunchedEffect
        if (!hasValidatedInternet()) return@LaunchedEffect
        withContext(Dispatchers.IO) { profileApi.fetch(session) }
            .onSuccess { fresh ->
                resolvedSession = fresh
                resetForm(fresh)
                onSessionUpdated(fresh)
            }
            .onFailure { onNotice(it.message ?: "No se pudo cargar la ficha completa del cajero.") }
    }

    LaunchedEffect(resolvedSession.cashierId, resolvedSession.offlineSession) {
        if (resolvedSession.cashierId <= 0L) return@LaunchedEffect
        while (isActive) {
            val stats = withContext(Dispatchers.IO) {
                loadProfileCashStats(catalogRepository, resolvedSession.cashierId)
            }
            stats.onSuccess { cashStats = it }
            delay(60_000)
        }
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pendingPhotoUri = uri
    }

    val persistedAvatar = remember(resolvedSession.avatar, resolvedSession.avatarBase64) {
        resolvedSession.avatarModel()
    }

    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
        val compact = maxWidth < 600.dp
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(if (compact) 12.dp else 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)

        ProfileHeader(
            session = resolvedSession,
            name = name,
            lastName = lastName,
            avatar = pendingPhotoUri ?: persistedAvatar,
            photoEditable = mode == ProfileMode.Edit,
            onPickPhoto = { picker.launch("image/*") },
            onEdit = { mode = ProfileMode.Edit },
            onSecurity = { mode = ProfileMode.Security },
        )

        when (mode) {
            ProfileMode.View -> ProfileReadOnly(name, lastName, email, document, phone, address, resolvedSession, cashStats)
            ProfileMode.Edit -> ProfileEditForm(
                name = name,
                lastName = lastName,
                email = email,
                document = document,
                phone = phone,
                address = address,
                session = resolvedSession,
                saving = saving,
                onName = { name = it },
                onLastName = { lastName = it },
                onEmail = { email = it },
                onDocument = { document = it },
                onPhone = { phone = it },
                onAddress = { address = it },
                onCancel = {
                    mode = ProfileMode.View
                    resetForm()
                },
                onSave = {
                    if (!hasValidatedInternet()) {
                        onNotice("Se necesita Internet para actualizar el perfil.")
                        return@ProfileEditForm
                    }
                    scope.launch {
                        saving = true
                        val result = withContext(Dispatchers.IO) {
                            profileApi.update(
                                resolvedSession,
                                name,
                                lastName,
                                email,
                                document,
                                phone,
                                address,
                                currentPassword = "",
                                password = "",
                                passwordConfirmation = "",
                                imageUri = pendingPhotoUri,
                            )
                        }
                        saving = false
                        result.onSuccess { fresh ->
                            resolvedSession = fresh
                            resetForm(fresh)
                            onSessionUpdated(fresh)
                            mode = ProfileMode.View
                            onNotice("Perfil actualizado correctamente.")
                        }.onFailure { onNotice(it.message ?: "No se pudo actualizar el perfil.") }
                    }
                },
            )
            ProfileMode.Security -> SecurityForm(
                currentPassword = currentPassword,
                password = password,
                confirmation = confirmation,
                saving = saving,
                onCurrentPassword = { currentPassword = it },
                onPassword = { password = it },
                onConfirmation = { confirmation = it },
                onCancel = {
                    mode = ProfileMode.View
                    resetForm()
                },
                onSave = {
                    if (!hasValidatedInternet()) {
                        onNotice("Se necesita Internet para cambiar la contraseña.")
                        return@SecurityForm
                    }
                    if (currentPassword.isBlank() || password.isBlank()) {
                        onNotice("Ingrese la contrasena actual y la nueva contrasena.")
                        return@SecurityForm
                    }
                    if (password != confirmation) {
                        onNotice("Las contrasenas no coinciden.")
                        return@SecurityForm
                    }
                    scope.launch {
                        saving = true
                        val result = withContext(Dispatchers.IO) {
                            profileApi.update(
                                resolvedSession,
                                name,
                                lastName,
                                email,
                                document,
                                phone,
                                address,
                                currentPassword = currentPassword,
                                password = password,
                                passwordConfirmation = confirmation,
                            )
                        }
                        saving = false
                        result.onSuccess { fresh ->
                            resolvedSession = fresh
                            resetForm(fresh)
                            onSessionUpdated(fresh)
                            mode = ProfileMode.View
                            onNotice("Contrasena actualizada correctamente.")
                        }.onFailure { onNotice(it.message ?: "No se pudo actualizar la contrasena.") }
                    }
                },
            )
        }
    }
    }
}

@Composable
private fun ProfileHeader(
    session: UserSession,
    name: String,
    lastName: String,
    avatar: Any?,
    photoEditable: Boolean,
    onPickPhoto: () -> Unit,
    onEdit: () -> Unit,
    onSecurity: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
        shadowElevation = 1.dp,
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val compact = maxWidth < 600.dp
            if (compact) {
                Column(
                    Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    ProfileAvatar(name, avatar, photoEditable, onPickPhoto, 88.dp)
                    ProfileIdentity(session, name, lastName, Alignment.CenterHorizontally)
                    ProfileHeaderActions(onEdit, onSecurity, Modifier.fillMaxWidth(), horizontal = true)
                }
            } else {
                Row(
                    Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    ProfileAvatar(name, avatar, photoEditable, onPickPhoto, 96.dp)
                    ProfileIdentity(session, name, lastName, Alignment.Start, Modifier.weight(1f))
                    ProfileHeaderActions(onEdit, onSecurity)
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(name: String, avatar: Any?, editable: Boolean, onPickPhoto: () -> Unit, size: androidx.compose.ui.unit.Dp) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Surface(Modifier.size(size), shape = CircleShape, color = Color(0xFFFFE4E6)) {
            Box(contentAlignment = Alignment.Center) {
                Text(name.firstInitial(), color = Brand, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                when (avatar) {
                    is ImageBitmap -> Image(
                        bitmap = avatar,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    null -> Unit
                    else -> AsyncImage(
                        model = avatar,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
        if (editable) {
            IconButton(
                onClick = onPickPhoto,
                modifier = Modifier.size(34.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Brand, contentColor = Color.White),
            ) { Icon(Icons.Filled.CameraAlt, "Cambiar foto", Modifier.size(18.dp)) }
        }
    }
}

@Composable
private fun ProfileIdentity(
    session: UserSession,
    name: String,
    lastName: String,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = alignment, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            "$name $lastName".trim().ifBlank { "-" },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            StatusChip(session.role.cleanDisplay().ifBlank { "-" }, Color(0xFFEFF6FF), Color(0xFF475569))
            StatusChip(session.defaultCashRegisterName.cleanDisplay().ifBlank { "-" }, Color(0xFFFFE4E6), BrandDark)
        }
    }
}

@Composable
private fun ProfileHeaderActions(
    onEdit: () -> Unit,
    onSecurity: () -> Unit,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
) {
    val editButton: @Composable (Modifier) -> Unit = { buttonModifier ->
        Button(onClick = onEdit, modifier = buttonModifier, colors = ButtonDefaults.buttonColors(Brand), shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Filled.Edit, null, Modifier.size(17.dp))
            Spacer(Modifier.width(6.dp))
            Text("Editar perfil", maxLines = 1)
        }
    }
    val securityButton: @Composable (Modifier) -> Unit = { buttonModifier ->
        OutlinedButton(onClick = onSecurity, modifier = buttonModifier, shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Border)) {
            Icon(Icons.Filled.Lock, null, Modifier.size(17.dp), tint = Muted)
            Spacer(Modifier.width(6.dp))
            Text("Seguridad", color = Muted, maxLines = 1)
        }
    }
    if (horizontal) {
        Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            editButton(Modifier.weight(1f))
            securityButton(Modifier.weight(1f))
        }
    } else {
        Column(modifier, horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            editButton(Modifier)
            securityButton(Modifier)
        }
    }
}

@Composable
private fun ProfileReadOnly(name: String, lastName: String, email: String, document: String, phone: String, address: String, session: UserSession, stats: ProfileCashStats) {
    var selectedSection by rememberSaveable { mutableStateOf(ProfileSection.Personal) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileSectionTabs(selectedSection) { selectedSection = it }
        when (selectedSection) {
            ProfileSection.Personal -> InfoCard("Información personal", Icons.Filled.Person, Modifier.fillMaxWidth()) {
                InfoGrid(
                    listOf(
                        "Nombres" to name,
                        "Apellidos" to lastName,
                        "Correo electrónico" to email,
                        "Tipo de documento" to session.documentType,
                        "Número de documento" to document,
                        "Número de celular" to phone,
                        "Dirección residencial" to address,
                    ),
                )
            }
            ProfileSection.Work -> InfoCard("Datos laborales", Icons.Filled.BusinessCenter, Modifier.fillMaxWidth()) {
                InfoGrid(
                    listOf(
                        "Rol en el sistema" to session.role,
                        "Caja asignada" to session.defaultCashRegisterName,
                        "Sucursal" to session.branchName,
                    ),
                )
            }
            ProfileSection.Collection -> {
                StatsRow(stats)
                PaymentBreakdown(stats)
            }
        }
    }
}

@Composable
private fun ProfileSectionTabs(
    selected: ProfileSection,
    onSelected: (ProfileSection) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFFBFB),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            ProfileSection.entries.forEach { section ->
                val active = selected == section
                Surface(
                    modifier = Modifier.clickable { onSelected(section) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (active) Brand else Color.Transparent,
                ) {
                    Text(
                        text = section.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                        color = if (active) Color.White else Muted,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileEditForm(
    name: String,
    lastName: String,
    email: String,
    document: String,
    phone: String,
    address: String,
    session: UserSession,
    saving: Boolean,
    onName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onEmail: (String) -> Unit,
    onDocument: (String) -> Unit,
    onPhone: (String) -> Unit,
    onAddress: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    InfoCard("Editar informacion", Icons.Filled.Edit, Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(name, onName, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(lastName, onLastName, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(email, onEmail, label = { Text("Correo electronico") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < 480.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(document, onDocument, label = { Text("Documento") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(phone, onPhone, label = { Text("Celular") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(document, onDocument, label = { Text("Documento") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(phone, onPhone, label = { Text("Celular") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                }
            }
            OutlinedTextField(address, onAddress, label = { Text("Direccion") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            InfoLine("Sucursal", session.branchName, Icons.Filled.Store)
            InfoLine("Caja asignada", session.defaultCashRegisterName, Icons.Filled.Badge)
            ActionRow(saving, "Guardar cambios", onCancel, onSave)
        }
    }
}

@Composable
private fun SecurityForm(
    currentPassword: String,
    password: String,
    confirmation: String,
    saving: Boolean,
    onCurrentPassword: (String) -> Unit,
    onPassword: (String) -> Unit,
    onConfirmation: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    var currentVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmationVisible by remember { mutableStateOf(false) }
    InfoCard("Seguridad", Icons.Filled.Lock, Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PasswordField(currentPassword, onCurrentPassword, "Contrasena actual", currentVisible) { currentVisible = !currentVisible }
            PasswordField(password, onPassword, "Nueva contrasena", passwordVisible) { passwordVisible = !passwordVisible }
            PasswordField(confirmation, onConfirmation, "Confirmar nueva contrasena", confirmationVisible) { confirmationVisible = !confirmationVisible }
            Text("Al guardar, el backend recibira la senal para enviar la confirmacion al correo del cajero.", color = Muted, style = MaterialTheme.typography.bodySmall)
            ActionRow(saving, "Guardar seguridad", onCancel, onSave)
        }
    }
}

@Composable
private fun PasswordField(value: String, onValueChange: (String) -> Unit, label: String, visible: Boolean, onToggleVisible: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisible) {
                Icon(
                    if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Ocultar contrasena" else "Mostrar contrasena",
                )
            }
        },
    )
}

@Composable
private fun StatsRow(stats: ProfileCashStats) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val wide = maxWidth >= 760.dp
        val cards = listOf(
            Triple(Icons.Filled.Badge, stats.salesCount.toString(), "Ventas hasta cierre"),
            Triple(Icons.Filled.Store, "S/ ${String.format(Locale.US, "%.2f", stats.totalCollected)}", "Total recaudado"),
            Triple(Icons.Filled.AccountBalanceWallet, "S/ ${String.format(Locale.US, "%.2f", stats.currentCashBalance)}", "Saldo actual de caja"),
            Triple(Icons.AutoMirrored.Filled.TrendingUp, "S/ ${String.format(Locale.US, "%.2f", stats.income)}", "Ingresos de caja"),
            Triple(Icons.AutoMirrored.Filled.TrendingDown, "S/ ${String.format(Locale.US, "%.2f", stats.expenses)}", "Salidas de caja"),
        )
        if (wide) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                cards.chunked(3).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { (icon, value, label) -> StatCard(icon, value, label, Modifier.weight(1f)) }
                    }
                }
            }
        } else if (maxWidth < 420.dp) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                cards.forEach { (icon, value, label) -> StatCard(icon, value, label, Modifier.fillMaxWidth()) }
            }
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                cards.chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { (icon, value, label) -> StatCard(icon, value, label, Modifier.weight(1f)) }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private data class PaymentTab(val label: String, val amount: Double)

@Composable
private fun PaymentBreakdown(stats: ProfileCashStats) {
    val tabs = listOf(
        PaymentTab("Efectivo", stats.cash),
        PaymentTab("Yape", stats.yape),
        PaymentTab("Plin", stats.plin),
        PaymentTab("Tarjeta", stats.card),
    )
    var selected by remember { mutableStateOf(0) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Recaudación por método de pago", color = TextPrimary, fontWeight = FontWeight.Bold)
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tabs.forEachIndexed { index, tab ->
                    val active = selected == index
                    Surface(
                        modifier = Modifier.clickable { selected = index },
                        shape = RoundedCornerShape(8.dp),
                        color = if (active) Brand else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (active) Brand else Border),
                    ) {
                        Text(
                            tab.label,
                            color = if (active) Color.White else TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                        )
                    }
                }
            }
            Text(
                "S/ ${String.format(Locale.US, "%.2f", tabs[selected].amount)}",
                color = Brand,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                if (tabs[selected].label == "Efectivo") "Importe físico considerado para la entrega de caja."
                else "Pago digital recibido directamente por la empresa; no se entrega como efectivo.",
                color = Muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun StatCard(icon: ImageVector, value: String, label: String, modifier: Modifier) {
    Surface(
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, null, tint = Brand, modifier = Modifier.size(20.dp))
            Column {
                Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(label, color = Muted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, icon: ImageVector, modifier: Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Border),
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().background(Color(0xFFFFFBFB)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Brand, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(title, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun InfoGrid(rows: List<Pair<String, String>>) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = if (maxWidth < 480.dp) 1 else 2
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            rows.chunked(columns).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { (label, value) ->
                        InfoText(label, value, Modifier.weight(1f))
                    }
                    if (columns == 2 && pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun InfoText(label: String, value: String, modifier: Modifier) {
    Column(modifier.heightIn(min = 48.dp)) {
        Text(label.uppercase(), color = Muted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(value.cleanDisplay().ifBlank { "-" }, color = TextPrimary, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun InfoLine(label: String, value: String, icon: ImageVector) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(icon, null, tint = Muted, modifier = Modifier.size(18.dp))
        Column(Modifier.weight(1f)) {
            Text(label, color = Muted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(value.cleanDisplay().ifBlank { "-" }, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ActionRow(saving: Boolean, primaryText: String, onCancel: () -> Unit, onSave: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = onCancel, enabled = !saving) { Text("Cancelar", color = Muted) }
        Spacer(Modifier.width(8.dp))
        Button(onClick = onSave, enabled = !saving, colors = ButtonDefaults.buttonColors(Brand), shape = RoundedCornerShape(8.dp)) {
            if (saving) CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp) else Text(primaryText)
        }
    }
}

@Composable
private fun StatusChip(text: String, bg: Color, fg: Color) {
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Text(text.cleanDisplay().ifBlank { "-" }, color = fg, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}

private fun String.cleanDisplay(): String {
    val value = trim()
    return value.takeUnless { it.isBlank() || it.equals("null", ignoreCase = true) }.orEmpty()
}

private fun String.firstInitial(): String = cleanDisplay().firstOrNull()?.uppercase() ?: "C"

private fun UserSession.avatarModel(): Any? {
    val encoded = avatarBase64.cleanDisplay().ifBlank {
        avatar.cleanDisplay().takeIf { it.startsWith("data:image/") }?.substringAfter("base64,").orEmpty()
    }
    if (encoded.isNotBlank()) {
        return runCatching {
            val cleanBase64 = encoded.substringAfter("base64,").replace("\n", "").replace("\r", "")
            val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        }.getOrNull()
    }
    val value = avatar.cleanDisplay()
    if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("content://")) return value
    return null
}
