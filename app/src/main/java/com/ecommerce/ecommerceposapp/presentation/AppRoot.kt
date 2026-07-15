package com.ecommerce.ecommerceposapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecommerce.ecommerceposapp.data.repository.AuthRepository
import com.ecommerce.ecommerceposapp.data.repository.CatalogRepository
import com.ecommerce.ecommerceposapp.data.repository.SyncRepository
import com.ecommerce.ecommerceposapp.domain.CartLine
import com.ecommerce.ecommerceposapp.domain.ClientRow
import com.ecommerce.ecommerceposapp.domain.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.ProductItem
import com.ecommerce.ecommerceposapp.domain.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private const val LOGIN = "login"
private const val SYNC = "sync"
private const val POS = "pos"

// Paleta de la app — fondo claro con acento rojo de marca
private val Brand = Color(0xFFfd0505)
private val BrandDark = Color(0xFFa82024)
private val AppBg = Color(0xFFF5F7FA)          // fondo principal — blanco ligeramente gris
private val SurfaceWhite = Color(0xFFFFFFFF)   // superficie blanca (tarjetas)
private val SurfaceAlt = Color(0xFFEEF0F5)     // superficie alternativa más oscura
private val TextPrimary = Color(0xFF111827)    // texto principal
private val TextSecondary = Color(0xFF6B7280)  // texto secundario / muted
private val Divider = Color(0xFFE5E7EB)        // separadores

@Composable
fun PosAppRoot(navController: NavHostController = rememberNavController()) {
    var currentUser by remember { mutableStateOf<UserSession?>(null) }
    val auth: AuthRepository = koinInject()
    val syncRepo: SyncRepository = koinInject()

    var bootstrapped by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (bootstrapped) return@LaunchedEffect
        bootstrapped = true
        val saved = auth.getSession() ?: return@LaunchedEffect
        currentUser = saved
        if (syncRepo.hasInitialSync(saved.id)) {
            navController.navigate(POS) {
                popUpTo(LOGIN) { inclusive = true }
            }
        } else {
            navController.navigate(SYNC) {
                popUpTo(LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            val vm: LoginViewModel = koinViewModel()
            val state by vm.uiState.collectAsState()
            LaunchedEffect(Unit) { vm.refreshOfflineAvailability() }
            LoginScreen(
                state = state,
                onEmailChange = vm::setEmail,
                onPasswordChange = vm::setPassword,
                onModeChange = vm::setOnlineMode,
                onSubmit = vm::login,
            )
            LaunchedEffect(state.user) {
                val user = state.user ?: return@LaunchedEffect
                currentUser = user
                navController.navigate(SYNC)
            }
        }
        composable(SYNC) {
            val vm: SyncViewModel = koinViewModel()
            val state by vm.uiState.collectAsState()
            val syncScope = rememberCoroutineScope()
            val user = currentUser ?: auth.getSession()?.also { currentUser = it }
            if (user == null) {
                navController.navigate(LOGIN) {
                    popUpTo(SYNC) { inclusive = true }
                }
                return@composable
            }
            val requiresSync = vm.needsSync(user.id)
            LaunchedEffect(user.id) {
                vm.loadModules()
                if (vm.needsSync(user.id)) vm.sync(user)
            }
            SyncScreen(
                user = user,
                state = state,
                requiresSync = requiresSync,
                onSync = { syncScope.launch { vm.sync(user) } },
                onToggleModule = vm::toggleModule,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(POS) {
                            popUpTo(SYNC) { inclusive = true }
                        }
                    }
                },
                onContinue = {
                    navController.navigate(POS) {
                        popUpTo(SYNC) { inclusive = true }
                    }
                },
            )
        }
        composable(POS) {
            val session = auth.getSession()
            if (session == null) {
                navController.navigate(LOGIN) {
                    popUpTo(POS) { inclusive = true }
                }
                return@composable
            }
            val maestroVm: MaestroViewModel = koinViewModel()
            val posVm: PosViewModel = koinViewModel()
            LaunchedEffect(Unit) { posVm.load() }
            PosScreen(
                session = session,
                maestroVm = maestroVm,
                posVm = posVm,
                onLogout = {
                    auth.logout()
                    currentUser = null
                    navController.navigate(LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onGoSync = {
                    currentUser = session
                    navController.navigate(SYNC) {
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppBg, SurfaceAlt))),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))

            // Ícono de marca
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "MiniMarket POS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                "Sistema de punto de venta",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
            Spacer(Modifier.height(32.dp))

            // Tarjeta del formulario
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = SurfaceWhite,
                shadowElevation = 6.dp,
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                    )
                    Spacer(Modifier.height(16.dp))

                    if (!state.canChooseOffline) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF8E1))
                                .padding(12.dp),
                        ) {
                            Text(
                                "Primera vez: inicie sesión en línea y espere la sincronización. Luego podrá usar modo sin conexión.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF92400E),
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    // Selector de modo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val offlineSel = !state.onlineMode && state.canChooseOffline
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (offlineSel) SurfaceAlt else Color(0xFFF9FAFB))
                                .clickable(enabled = state.canChooseOffline) { onModeChange(false) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                if (!state.onlineMode) "✓ Offline" else "Offline",
                                color = if (offlineSel) TextPrimary else TextSecondary,
                                fontWeight = if (offlineSel) FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (state.onlineMode) Color(0xFFEFF6FF) else Color(0xFFF9FAFB))
                                .clickable { onModeChange(true) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                if (state.onlineMode) "✓ Online" else "Online",
                                color = if (state.onlineMode) Color(0xFF1E40AF) else TextSecondary,
                                fontWeight = if (state.onlineMode) FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    ) {
                        Text(
                            "Iniciar sesión",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                        )
                    }
                    state.error?.let { err ->
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFEBEB))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                        ) {
                            Text(err, color = Brand, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                "© 2025 MiniMarket POS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9CA3AF),
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SyncScreen(
    user: UserSession,
    state: SyncUiState,
    requiresSync: Boolean,
    onSync: () -> Unit,
    onToggleModule: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    fun formatLastSync(millis: Long): String {
        if (millis <= 0L) return "Nunca"
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE")).format(Date(millis))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppBg, SurfaceAlt))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                OutlinedButton(onClick = onBack) { Text("← Volver") }
            }
            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Hola, ${user.name}",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (requiresSync) "Primera sincronización requerida para modo offline." else "Sincronización inicial ya completada.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
            Spacer(Modifier.height(24.dp))

            if (requiresSync) {
                Button(
                    onClick = onSync,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Sincronizar catálogo", fontWeight = FontWeight.Bold, color = Color.White) }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onSync) { Text("Re-sincronizar") }
                    Button(
                        onClick = onContinue,
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Entrar al POS", fontWeight = FontWeight.Bold, color = Color.White) }
                }
            }

            Spacer(Modifier.height(24.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                shadowElevation = 4.dp,
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Módulos de sincronización",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                    Spacer(Modifier.height(12.dp))
                    state.modules.forEach { m ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(m.label, fontWeight = FontWeight.Medium, color = TextPrimary)
                                Text(
                                    "Última: ${formatLastSync(m.lastSyncAt)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                )
                            }
                            FilterChip(
                                selected = state.selectedModules.contains(m.key),
                                onClick = { onToggleModule(m.key) },
                                label = { Text(if (state.selectedModules.contains(m.key)) "Seleccionado" else "Seleccionar") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Brand,
                                    selectedLabelColor = Color.White,
                                ),
                            )
                        }
                    }
                }
            }

            if (state.syncing || state.message.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(state.message, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
            if (state.completed) {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Continuar al POS", fontWeight = FontWeight.Bold, color = Color.White) }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

private val drawerMenuItems = listOf(
    "Punto de venta",
    "Historial de ventas",
    "Productos",
    "Categorías",
    "Clientes",
    "Proveedores",
    "Usuarios",
    "Mi perfil",
    "Sincronizar catálogo",
    "Cerrar sesión",
)

@Composable
private fun PosNavigationDrawerContent(
    selectedLabel: String,
    greetingName: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "MiniMarket POS",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    "Hola, $greetingName",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .height(1.dp)
                .background(Divider),
        )

        Spacer(Modifier.height(4.dp))

        drawerMenuItems.forEach { label ->
            val selectable = label != "Cerrar sesión" && label != "Sincronizar catálogo"
            val selected = selectable && label == selectedLabel
            NavigationDrawerItem(
                label = {
                    Text(
                        label,
                        color = if (selected) Brand else TextPrimary,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                selected = selected,
                onClick = { onItemClick(label) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFFFFEBEB),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = Brand,
                    unselectedTextColor = TextPrimary,
                ),
            )
        }

        Spacer(Modifier.weight(1f))
        Text(
            "v1.0 · MiniMarket POS",
            color = Color(0xFF9CA3AF),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PosScreen(
    session: UserSession,
    maestroVm: MaestroViewModel,
    posVm: PosViewModel,
    onLogout: () -> Unit,
    onGoSync: () -> Unit,
) {
    val catalog: CatalogRepository = koinInject()
    val state by posVm.uiState.collectAsState()
    val maestroState by maestroVm.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedModule by remember { mutableStateOf("Punto de venta") }
    val widthDp = LocalConfiguration.current.screenWidthDp
    val responsiveTwoPanels = widthDp >= 900

    val topBarTitle = selectedModule

    LaunchedEffect(selectedModule) {
        if (selectedModule == "Punto de venta" || selectedModule == "Historial de ventas") {
            posVm.load()
            maestroVm.loadAll()
        }
    }

    val posContent: @Composable (PaddingValues) -> Unit = { padding ->
        when (selectedModule) {
            "Punto de venta" -> {
                if (responsiveTwoPanels) {
                    Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                        CatalogPane(
                            Modifier.weight(1f).fillMaxHeight(),
                            state,
                            posVm::setSearch,
                            posVm::setCategory,
                            onAddToCart = posVm::addToCart,
                        )
                        CartPane(
                            Modifier.width(360.dp),
                            state,
                            maestroState,
                            catalog,
                            posVm::increase,
                            posVm::decrease,
                            onPay = { p, idC -> posVm.pay(p, idC) },
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                        CatalogPane(
                            Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                            state,
                            posVm::setSearch,
                            posVm::setCategory,
                            onAddToCart = posVm::addToCart,
                        )
                        CartPane(
                            Modifier.fillMaxWidth().height(320.dp),
                            state,
                            maestroState,
                            catalog,
                            posVm::increase,
                            posVm::decrease,
                            onPay = { p, idC -> posVm.pay(p, idC) },
                        )
                    }
                }
            }
            "Historial de ventas" -> Box(Modifier.fillMaxSize().padding(padding)) { SalesHistoryScreen(catalog, maestroState.clients) }
            "Productos" -> Box(Modifier.fillMaxSize().padding(padding)) { ProductsCrudScreen(maestroVm) }
            "Categorías" -> Box(Modifier.fillMaxSize().padding(padding)) { CategoriesCrudScreen(maestroVm) }
            "Clientes" -> Box(Modifier.fillMaxSize().padding(padding)) { ClientsCrudScreen(maestroVm) }
            "Proveedores" -> Box(Modifier.fillMaxSize().padding(padding)) { SuppliersCrudScreen(maestroVm) }
            "Usuarios" -> Box(Modifier.fillMaxSize().padding(padding)) { UsersCrudScreen(maestroVm, session) }
            "Mi perfil" -> Box(Modifier.fillMaxSize().padding(padding)) { ProfileScreen(session, onLogout) }
            else -> Box(Modifier.fillMaxSize().padding(padding)) { Text("Seleccione una opción del menú.") }
        }
    }

    val scaffold: @Composable () -> Unit = {
        Scaffold(
            containerColor = AppBg,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            topBarTitle,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = if (drawerState.isOpen) Icons.Filled.Close else Icons.Filled.Menu,
                                contentDescription = if (drawerState.isOpen) "Cerrar menú" else "Abrir menú",
                                tint = TextPrimary,
                            )
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 12.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (session.offlineSession) Color(0xFFFFEBEB) else Color(0xFFDCFCE7),
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                            ) {
                                Text(
                                    if (session.offlineSession) "OFFLINE" else "ONLINE",
                                    color = if (session.offlineSession) Brand else Color(0xFF16A34A),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SurfaceWhite,
                        titleContentColor = TextPrimary,
                        navigationIconContentColor = TextPrimary,
                    ),
                )
            },
        ) { padding -> posContent(padding) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceWhite,
                drawerTonalElevation = 0.dp,
            ) {
                PosNavigationDrawerContent(
                    selectedLabel = selectedModule,
                    greetingName = session.name,
                    onItemClick = { label ->
                        when (label) {
                            "Cerrar sesión" -> {
                                scope.launch { drawerState.close() }
                                onLogout()
                            }
                            "Sincronizar catálogo" -> {
                                scope.launch { drawerState.close() }
                                onGoSync()
                            }
                            else -> {
                                selectedModule = label
                                scope.launch { drawerState.close() }
                            }
                        }
                    },
                )
            }
        },
    ) {
        scaffold()
    }
}

@Composable
private fun ProductSaleCard(
    product: ProductItem,
    onAddToCart: (ProductItem) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.05f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center,
            ) {
                if (product.imageUrl.isBlank()) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = null,
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(48.dp),
                    )
                } else {
                    var coilFailed by rememberSaveable(product.imageUrl) { mutableStateOf(false) }
                    if (coilFailed) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(48.dp),
                        )
                    } else {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onError = { coilFailed = true },
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = TextPrimary,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "S/ ${"%.2f".format(product.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Brand,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Stock: ${product.stock.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand),
            ) {
                Icon(
                    Icons.Filled.AddShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White,
                )
                Spacer(Modifier.width(6.dp))
                Text("Agregar", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }
    }
}

@Composable
private fun CatalogPane(
    modifier: Modifier,
    state: PosUiState,
    onSearch: (String) -> Unit,
    onCategory: (Long?) -> Unit,
    onAddToCart: (ProductItem) -> Unit,
) {
    val products = state.products.filter {
        (state.selectedCategoryId == null || it.categoryId == state.selectedCategoryId) &&
            (state.search.isBlank() || it.name.contains(state.search, ignoreCase = true) ||
                (it.code.isNotBlank() && it.code.contains(state.search, ignoreCase = true)))
    }
    val gridMinWidth = when {
        LocalConfiguration.current.screenWidthDp >= 900 -> 200.dp
        LocalConfiguration.current.screenWidthDp >= 600 -> 176.dp
        else -> 156.dp
    }
    Column(modifier = modifier.fillMaxSize().background(AppBg).padding(12.dp)) {
        OutlinedTextField(
            value = state.search,
            onValueChange = onSearch,
            label = { Text("Buscar producto...") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
            item {
                FilterChip(
                    selected = state.selectedCategoryId == null,
                    onClick = { onCategory(null) },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Brand,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
            items(state.categories) { category ->
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = state.selectedCategoryId == category.id,
                    onClick = { onCategory(category.id) },
                    label = { Text(category.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Brand,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = gridMinWidth),
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(products, key = { it.id }) { product ->
                ProductSaleCard(
                    product = product,
                    onAddToCart = onAddToCart,
                )
            }
        }
    }
}

private enum class PosPaymentMethod { Efectivo, Tarjeta, Yape, Plin }

private fun appendMontoRecibido(current: String, key: String): String {
    if (key == "⌫") return if (current.isEmpty()) "" else current.dropLast(1)
    if (key == ".") {
        if (current.contains('.')) return current
        return if (current.isEmpty()) "0." else current + "."
    }
    if (!key.all { it.isDigit() }) return current
    val next = current + key
    val parts = next.split('.')
    if (parts.size > 1 && parts[1].length > 2) return current
    return next
}

private fun mapPosPaymentMethodToTipoPago(m: PosPaymentMethod): String = when (m) {
    PosPaymentMethod.Efectivo -> "EFE"
    PosPaymentMethod.Tarjeta -> "TAR"
    PosPaymentMethod.Yape -> "YAP"
    PosPaymentMethod.Plin -> "PLN"
}

@Composable
private fun CobrarVentaDialog(
    total: Double,
    onDismiss: () -> Unit,
    onCobroExitoso: (CompletedSaleReceipt) -> Unit,
    onPay: suspend (SalePaymentInfo) -> Result<CompletedSaleReceipt>,
) {
    val headerBg = BrandDark
    val bodyBg = SurfaceWhite
    val keypadBg = SurfaceAlt
    val methodUnselected = Color(0xFFF0F2F5)
    val methodSelected = Brand
    val labelMuted = TextSecondary

    var method by remember { mutableStateOf(PosPaymentMethod.Efectivo) }
    var receivedText by remember { mutableStateOf("") }
    var processing by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(method, total) {
        receivedText = when (method) {
            PosPaymentMethod.Efectivo -> ""
            else -> String.format(Locale.US, "%.2f", total)
        }
    }

    val received = receivedText.toDoubleOrNull() ?: 0.0
    val vuelto = (received - total).coerceAtLeast(0.0)
    val canConfirm = total > 0 && !processing && received + 1e-6 >= total

    Dialog(onDismissRequest = { if (!processing) onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(20.dp),
                color = bodyBg,
                shadowElevation = 12.dp,
            ) {
                Column(Modifier.fillMaxSize()) {
                    // Cabecera roja
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Payment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Cobrar venta",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        IconButton(onClick = { if (!processing) onDismiss() }, enabled = !processing) {
                            Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    ) {
                        Text("TOTAL A COBRAR", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(
                                "S/ ${String.format(Locale.US, "%.2f", total)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("MÉTODO DE PAGO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val methods = listOf(
                                Triple(PosPaymentMethod.Efectivo, "Efectivo", Icons.Filled.AttachMoney),
                                Triple(PosPaymentMethod.Tarjeta, "Tarjeta", Icons.Filled.CreditCard),
                                Triple(PosPaymentMethod.Yape, "Yape", Icons.Filled.Smartphone),
                                Triple(PosPaymentMethod.Plin, "Plin", Icons.Filled.Smartphone),
                            )
                            methods.forEach { (m, label, icon) ->
                                val sel = method == m
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (sel) methodSelected else methodUnselected)
                                        .clickable(enabled = !processing) {
                                            method = m
                                            errorText = ""
                                        }
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = if (sel) Color.White else TextSecondary,
                                        modifier = Modifier.size(26.dp),
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        label,
                                        color = if (sel) Color.White else TextSecondary,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("MONTO RECIBIDO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceAlt, RoundedCornerShape(10.dp))
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                text = if (receivedText.isEmpty()) "0" else receivedText,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("VUELTO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceAlt, RoundedCornerShape(10.dp))
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                "S/ ${String.format(Locale.US, "%.2f", vuelto)}",
                                color = Color(0xFF16A34A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                            )
                        }
                        Spacer(Modifier.height(18.dp))
                        Text("TECLADO NUMÉRICO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        val keys = listOf(
                            listOf("7", "8", "9"),
                            listOf("4", "5", "6"),
                            listOf("1", "2", "3"),
                            listOf(".", "0", "⌫"),
                        )
                        keys.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                row.forEach { k ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(keypadBg)
                                            .clickable(enabled = !processing && method == PosPaymentMethod.Efectivo) {
                                                receivedText = appendMontoRecibido(receivedText, k)
                                                errorText = ""
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(k, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        if (errorText.isNotBlank()) {
                            Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Button(
                            onClick = {
                                if (!canConfirm) return@Button
                                scope.launch {
                                    processing = true
                                    errorText = ""
                                    val payment = SalePaymentInfo(
                                        tipoPago = mapPosPaymentMethodToTipoPago(method),
                                        montoRecibido = received,
                                        vuelto = vuelto,
                                    )
                                    val r = onPay(payment)
                                    processing = false
                                    r.fold(
                                        onSuccess = {
                                            onCobroExitoso(it)
                                            onDismiss()
                                        },
                                        onFailure = { errorText = it.message ?: "No se pudo registrar la venta." },
                                    )
                                }
                            },
                            enabled = canConfirm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(
                                "COBRAR  S/ ${String.format(Locale.US, "%.2f", total)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        TextButton(
                            onClick = { if (!processing) onDismiss() },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Cancelar", color = labelMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartPane(
    modifier: Modifier,
    state: PosUiState,
    maestroState: MaestroUiState,
    catalog: CatalogRepository,
    onIncrease: (CartLine) -> Unit,
    onDecrease: (CartLine) -> Unit,
    onPay: suspend (SalePaymentInfo, Long) -> Result<CompletedSaleReceipt>,
) {
    var message by remember { mutableStateOf("") }
    var showCobrarVenta by remember { mutableStateOf(false) }
    var selectedCliente by remember { mutableStateOf<ClientRow?>(null) }
    var showClientePicker by remember { mutableStateOf(false) }
    var pendingReceipt by remember { mutableStateOf<CompletedSaleReceipt?>(null) }
    var showEmitir by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    var comprobanteEmitido by remember { mutableStateOf<ComprobanteEmitidoResult?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.background(SurfaceWhite).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Brand,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Carrito (${state.cart.sumOf { it.quantity }})",
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Cliente: ${selectedCliente?.name ?: "—"}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            TextButton(onClick = { showClientePicker = true }) {
                Text("Cambiar", color = Brand)
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(state.cart) { line ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppBg)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            line.productName,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            "S/ ${"%.2f".format(line.unitPrice)}",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onDecrease(line) },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(Icons.Filled.Remove, contentDescription = "Menos", tint = Brand)
                        }
                        Text(
                            "${line.quantity}",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        IconButton(
                            onClick = { onIncrease(line) },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Más", tint = Brand)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(AppBg)
                .padding(12.dp),
        ) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text("S/ ${"%.2f".format(state.subtotal)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("IGV (18%)", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text("S/ ${"%.2f".format(state.igv)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Divider),
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TOTAL", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "S/ ${"%.2f".format(state.total)}",
                        fontWeight = FontWeight.Bold,
                        color = Brand,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                if (state.cart.isEmpty()) {
                    message = "Agregue productos al carrito."
                    return@Button
                }
                message = ""
                showCobrarVenta = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Brand),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(Icons.Filled.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("PAGAR", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
        }
        if (message.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                color = if (message.contains("Agregue")) Brand else Color(0xFF16A34A),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

    if (showCobrarVenta) {
        CobrarVentaDialog(
            total = state.total,
            onDismiss = { showCobrarVenta = false },
            onCobroExitoso = { receipt ->
                message = "Venta registrada."
                pendingReceipt = receipt
                showEmitir = true
            },
            onPay = { payment -> onPay(payment, selectedCliente?.id ?: 0L) },
        )
    }

    if (showClientePicker) {
        AlertDialog(
            onDismissRequest = { showClientePicker = false },
            title = { Text("Cliente en esta venta") },
            text = {
                Column(
                    Modifier
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        "Sin cliente (no identificado)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCliente = null
                                showClientePicker = false
                            }
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    maestroState.clients.filter { it.active }.forEach { c ->
                        Text(
                            "${c.name} — ${c.document.ifBlank { "—" }}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCliente = c
                                    showClientePicker = false
                                }
                                .padding(vertical = 8.dp),
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClientePicker = false }) { Text("Cerrar") }
            },
        )
    }

    if (showEmitir && pendingReceipt != null) {
        EmitirComprobanteDialog(
            onDismiss = {
                showEmitir = false
                pendingReceipt = null
            },
            onElegir = pick@{ tipo ->
                val pr = pendingReceipt ?: return@pick
                scope.launch {
                    val r = withContext(Dispatchers.IO) {
                        catalog.emitComprobanteForVenta(pr.ventaId, tipo, selectedCliente?.id ?: pr.idCliente)
                    }
                    r.fold(
                        onSuccess = {
                            comprobanteEmitido = it
                            showEmitir = false
                            showPreview = true
                        },
                        onFailure = { e -> message = e.message ?: "Error al emitir comprobante." },
                    )
                }
            },
        )
    }

    if (showPreview && pendingReceipt != null && comprobanteEmitido != null) {
        val pr = pendingReceipt!!
        val em = comprobanteEmitido!!
        val cid = (selectedCliente?.id ?: pr.idCliente).coerceAtLeast(0L)
        val cdisp = if (cid > 0L) catalog.getClienteDisplay(cid) else null
        VistaPreviaReciboDialog(
            receipt = pr,
            emitido = em,
            clienteNombre = cdisp?.first,
            clienteDoc = cdisp?.second,
            idClienteVenta = cid,
            clients = maestroState.clients,
            catalog = catalog,
            onDismiss = {
                showPreview = false
                pendingReceipt = null
                comprobanteEmitido = null
                selectedCliente = null
            },
        )
    }
}
