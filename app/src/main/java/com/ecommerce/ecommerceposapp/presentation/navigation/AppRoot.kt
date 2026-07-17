package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarData
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
import androidx.compose.ui.res.painterResource
import com.ecommerce.ecommerceposapp.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import java.io.File
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.presentation.auth.LoginUiState
import com.ecommerce.ecommerceposapp.presentation.auth.LoginViewModel
import com.ecommerce.ecommerceposapp.presentation.categories.CategoriesCrudScreen
import com.ecommerce.ecommerceposapp.presentation.categories.CategoriesViewModel
import com.ecommerce.ecommerceposapp.presentation.clients.ClientsCrudScreen
import com.ecommerce.ecommerceposapp.presentation.clients.ClientsViewModel
import com.ecommerce.ecommerceposapp.presentation.products.ProductsCrudScreen
import com.ecommerce.ecommerceposapp.presentation.products.ProductEditDialog
import com.ecommerce.ecommerceposapp.presentation.products.ProductsViewModel
import com.ecommerce.ecommerceposapp.presentation.profile.ProfileScreen
import com.ecommerce.ecommerceposapp.presentation.suppliers.SuppliersCrudScreen
import com.ecommerce.ecommerceposapp.presentation.suppliers.SuppliersViewModel
import com.ecommerce.ecommerceposapp.presentation.users.UsersCrudScreen
import com.ecommerce.ecommerceposapp.presentation.users.UsersViewModel
import com.ecommerce.ecommerceposapp.presentation.pos.PosViewModel
import com.ecommerce.ecommerceposapp.presentation.sales.SalesHistoryScreen
import com.ecommerce.ecommerceposapp.presentation.sync.SyncViewModel
import kotlinx.coroutines.launch
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
private fun FloatingRightNotice(data: SnackbarData) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Card(
            modifier = Modifier.widthIn(min = 300.dp, max = 440.dp),
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF111827)),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(10.dp),
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4ADE80))
                Text(data.visuals.message, color = Color.White, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
                IconButton(onClick = data::dismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar notificación", tint = Color(0xFFCBD5E1))
                }
            }
        }
    }
}

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
            ModernLoginScreen(
                state = state,
                onEmailChange = vm::setEmail,
                onPasswordChange = vm::setPassword,
                onOfflineModeChange = vm::setOfflineMode,
                onSubmit = vm::login,
            )
            LaunchedEffect(state.user) {
                val user = state.user ?: return@LaunchedEffect
                currentUser = user
                navController.navigate(if (user.offlineSession) POS else SYNC) {
                    popUpTo(LOGIN) { inclusive = true }
                }
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
            val categoriesVm: CategoriesViewModel = koinViewModel()
            val clientsVm: ClientsViewModel = koinViewModel()
            val productsVm: ProductsViewModel = koinViewModel()
            val suppliersVm: SuppliersViewModel = koinViewModel()
            val usersVm: UsersViewModel = koinViewModel()
            val posVm: PosViewModel = koinViewModel()
            LaunchedEffect(Unit) { posVm.load() }
            PosScreen(
                session = session,
                categoriesVm = categoriesVm,
                clientsVm = clientsVm,
                productsVm = productsVm,
                suppliersVm = suppliersVm,
                usersVm = usersVm,
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
private fun ModernLoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onOfflineModeChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
        val wide = maxWidth >= 760.dp
        val compactHeight = maxHeight < 650.dp
        Row(Modifier.fillMaxSize()) {
            if (wide) {
                Box(Modifier.weight(1.05f).fillMaxHeight()) {
                    Image(
                        painter = painterResource(R.drawable.post),
                        contentDescription = "Cajera en punto de venta",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, BrandDark.copy(alpha = 0.22f)))))
                }
            }
            Box(
                Modifier.weight(1f).fillMaxHeight().padding(horizontal = if (wide) 52.dp else 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    Modifier.fillMaxWidth().widthIn(max = 430.dp).verticalScroll(rememberScrollState())
                        .padding(vertical = if (compactHeight) 16.dp else 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                if (!wide) {
                    Image(
                        painterResource(R.drawable.post),
                        "Punto de venta PrestoMart",
                        Modifier.fillMaxWidth().height(if (compactHeight) 120.dp else 180.dp).clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.height(if (compactHeight) 14.dp else 22.dp))
                }
                Text("Modo de conectividad", fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Filled.Cloud, null, tint = if (!state.offlineMode) Color(0xFF16A34A) else TextSecondary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp)); Text("Online", color = if (!state.offlineMode) Color(0xFF16A34A) else TextSecondary)
                    androidx.compose.material3.Switch(checked = state.offlineMode, onCheckedChange = onOfflineModeChange, enabled = state.offlineAvailable, modifier = Modifier.padding(horizontal = 8.dp))
                    Icon(Icons.Filled.CloudOff, null, tint = if (state.offlineMode) Brand else TextSecondary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp)); Text("Offline", color = if (state.offlineMode) Brand else TextSecondary)
                }
                if (!state.offlineAvailable) Text("El modo offline se habilita después del primer acceso online.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(Modifier.height(if (compactHeight) 18.dp else 28.dp))
                Text("PrestoMart POS", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10213F))
                Text(if (state.offlineMode) "Accede sin conexión con tus credenciales guardadas." else "Bienvenido. Ingresa con tu cuenta de cajero.", color = TextSecondary)
                Spacer(Modifier.height(if (compactHeight) 18.dp else 28.dp))
                OutlinedTextField(state.email, onEmailChange, placeholder = { Text("Correo electrónico") }, leadingIcon = { Icon(Icons.Filled.Email, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(10.dp))
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp),
                )
                state.error?.let { Spacer(Modifier.height(10.dp)); Text(it, color = Brand, modifier = Modifier.fillMaxWidth()) }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onSubmit, enabled = !state.busy && state.email.isNotBlank() && state.password.isNotBlank(), modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(Brand)) {
                    if (state.busy) { CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp); Spacer(Modifier.width(8.dp)) }
                    Text(if (state.offlineMode) "Ingresar sin conexión" else "Iniciar sesión", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(if (compactHeight) 24.dp else 54.dp))
                Text("© 2026 PrestoMart POS", color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onOfflineModeChange: (Boolean) -> Unit,
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
                "PrestoMart POS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                "Tu caja lista para vender",
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("Bienvenido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Ingresa con tu cuenta de cajero", color = TextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = if (state.offlineAvailable) Color(0xFFFFF1F2) else Color(0xFFF1F5F9)) {
                            Text(
                                if (state.offlineAvailable) "Offline disponible" else "Acceso seguro",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                color = if (state.offlineAvailable) BrandDark else TextSecondary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Spacer(Modifier.height(22.dp))
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

                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = !state.busy && state.email.isNotBlank() && state.password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    ) {
                        if (state.busy) {
                            CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(Modifier.width(10.dp))
                        }
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

private val drawerMenuItems = listOf(
    "Punto de venta",
    "Historial de ventas",
    "Productos",
    "Categorías",
    "Clientes",
    "Proveedores",
    "Usuarios",
    "Sincronizar catálogo",
    "Cerrar sesión",
)

@Composable
private fun PosNavigationDrawerContent(
    selectedLabel: String,
    session: UserSession,
    cashRegisterName: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val photoPath = context.getSharedPreferences("profile_preferences", android.content.Context.MODE_PRIVATE)
        .getString("profile_photo_${session.id}", "").orEmpty()
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
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onItemClick("Mi perfil") }
                .padding(horizontal = 8.dp, vertical = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0xFFFFE4E6)),
                contentAlignment = Alignment.Center,
            ) {
                if (photoPath.isNotBlank() && File(photoPath.removePrefix("file://")).exists()) {
                    AsyncImage(model = photoPath, contentDescription = "Foto de ${session.name}", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Text(session.name.trim().firstOrNull()?.uppercase() ?: "C", color = Brand, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Hola, ${session.name.ifBlank { "cajero" }}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    cashRegisterName.ifBlank { session.defaultCashRegisterName }.ifBlank { "Sin caja asignada" },
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text("Ver mi perfil", color = Brand, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
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
                    selectedContainerColor = Color(0xFFF3F4F6),
                    unselectedContainerColor = Color.Transparent,
                    selectedTextColor = Brand,
                    unselectedTextColor = TextPrimary,
                ),
            )
        }

        Spacer(Modifier.weight(1f))
        Text(
            "v1.0 · PrestoMart POS",
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
    categoriesVm: CategoriesViewModel,
    clientsVm: ClientsViewModel,
    productsVm: ProductsViewModel,
    suppliersVm: SuppliersViewModel,
    usersVm: UsersViewModel,
    posVm: PosViewModel,
    onLogout: () -> Unit,
    onGoSync: () -> Unit,
) {
    val catalog: CatalogRepository = koinInject()
    val state by posVm.uiState.collectAsState()
    val clientsState by clientsVm.uiState.collectAsState()
    val productsState by productsVm.uiState.collectAsState()
    val categoriesState by categoriesVm.uiState.collectAsState()
    val suppliersState by suppliersVm.uiState.collectAsState()
    val usersState by usersVm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedModule by remember { mutableStateOf("Punto de venta") }
    var showQuickProductDialog by remember { mutableStateOf(false) }
    var openAdvancedProductForm by remember { mutableStateOf(false) }
    val widthDp = LocalConfiguration.current.screenWidthDp
    val responsiveTwoPanels = widthDp >= 900

    LaunchedEffect(session.cashierId) {
        posVm.loadCashSession(session.cashierId)
    }

    val topBarTitle = selectedModule

    LaunchedEffect(productsState.message, productsState.error) {
        val notice = productsState.error ?: productsState.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            productsVm.clearMessages()
        }
    }

    LaunchedEffect(categoriesState.message, categoriesState.error) {
        val notice = categoriesState.error ?: categoriesState.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            categoriesVm.clearMessages()
        }
    }

    LaunchedEffect(clientsState.message, clientsState.error) {
        val notice = clientsState.error ?: clientsState.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            clientsVm.clearMessages()
        }
    }

    LaunchedEffect(suppliersState.message, suppliersState.error) {
        val notice = suppliersState.error ?: suppliersState.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            suppliersVm.clearMessages()
        }
    }

    LaunchedEffect(usersState.message, usersState.error) {
        val notice = usersState.error ?: usersState.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            usersVm.clearMessages()
        }
    }

    LaunchedEffect(selectedModule) {
        when (selectedModule) {
            "Punto de venta", "Historial de ventas" -> {
                posVm.refreshCatalog()
                clientsVm.load()
            }
            "Productos" -> {
                posVm.refreshCatalog()
                productsVm.load()
            }
            "Categorías" -> {
                posVm.refreshCatalog()
                categoriesVm.loadAll()
            }
        }
    }

    val posContent: @Composable (PaddingValues) -> Unit = { padding ->
        when (selectedModule) {
            "Punto de venta" -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    if (responsiveTwoPanels) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            CatalogPane(
                                Modifier.weight(1f).fillMaxHeight(),
                                state,
                                posVm::setSearch,
                                posVm::setCategory,
                                posVm::setSubcategory,
                                onAddToCart = posVm::addToCart,
                                onToggleFeatured = posVm::toggleFeatured,
                                onNewProduct = { showQuickProductDialog = true },
                            )
                            CartPane(
                                Modifier.width(360.dp),
                                state,
                                "${session.name} ${session.lastName}".trim(),
                                clientsState.clients,
                                catalog,
                                posVm::increase,
                                posVm::decrease,
                                onPay = { p, idC -> posVm.pay(p, idC) },
                                onNewClient = { selectedModule = "Clientes" },
                            )
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            CatalogPane(
                                Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                                state,
                                posVm::setSearch,
                                posVm::setCategory,
                                posVm::setSubcategory,
                                onAddToCart = posVm::addToCart,
                                onToggleFeatured = posVm::toggleFeatured,
                                onNewProduct = { showQuickProductDialog = true },
                            )
                            CartPane(
                                Modifier.fillMaxWidth().height(320.dp),
                                state,
                                "${session.name} ${session.lastName}".trim(),
                                clientsState.clients,
                                catalog,
                                posVm::increase,
                                posVm::decrease,
                                onPay = { p, idC -> posVm.pay(p, idC) },
                                onNewClient = { selectedModule = "Clientes" },
                            )
                        }
                    }
                    CashSessionIndicator(
                        state = state,
                        onClose = {
                            scope.launch { posVm.loadCashSummary() }
                            requestCashClose()
                        },
                        modifier = Modifier.align(Alignment.TopEnd),
                    )
                }
            }
            "Historial de ventas" -> Box(Modifier.fillMaxSize().padding(padding)) { SalesHistoryScreen(catalog, clientsState.clients) }
            "Productos" -> Box(Modifier.fillMaxSize().padding(padding)) {
                ProductsCrudScreen(
                    vm = productsVm,
                    openCreateAdvanced = openAdvancedProductForm,
                    onCreateAdvancedConsumed = { openAdvancedProductForm = false },
                )
            }
            "Categorías" -> Box(Modifier.fillMaxSize().padding(padding)) { CategoriesCrudScreen(categoriesVm) }
            "Clientes" -> Box(Modifier.fillMaxSize().padding(padding)) { ClientsCrudScreen(clientsVm) }
            "Proveedores" -> Box(Modifier.fillMaxSize().padding(padding)) { SuppliersCrudScreen(suppliersVm) }
            "Usuarios" -> Box(Modifier.fillMaxSize().padding(padding)) { UsersCrudScreen(usersVm, session) }
            "Mi perfil" -> Box(Modifier.fillMaxSize().padding(padding)) {
                ProfileScreen(
                    session = session,
                    onLogout = onLogout,
                ) { notice -> scope.launch { snackbarHostState.showSnackbar(notice) } }
            }
            else -> Box(Modifier.fillMaxSize().padding(padding)) { Text("Seleccione una opción del menú.") }
        }
    }

    val scaffold: @Composable () -> Unit = {
        Scaffold(
            containerColor = AppBg,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.fillMaxWidth().padding(end = 18.dp, bottom = 18.dp),
                    snackbar = { data -> FloatingRightNotice(data) },
                )
            },
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
                                        if (session.offlineSession) Color(0xFFF3F4F6) else Color(0xFFDCFCE7),
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
                    session = session,
                    cashRegisterName = state.cashSession?.cashRegisterName.orEmpty(),
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

    if (showQuickProductDialog) {
        ProductEditDialog(
            initial = ProductAdminRow(
                id = 0L,
                categoryId = productsState.categories.firstOrNull { it.active }?.id ?: 0L,
                subcategoryId = 0L,
                name = "",
                code = "",
                imageUrl = "",
                price = 0.0,
                stock = 0.0,
                active = true,
            ),
            categories = productsState.categories,
            subcategories = productsState.subcategories,
            onDismiss = {
                showQuickProductDialog = false
                productsVm.clearMessages()
            },
            onSave = {
                showQuickProductDialog = false
                productsVm.save(it) {
                    scope.launch {
                        posVm.refreshCatalog()
                    }
                }
            },
            quickMode = true,
            initialAdvanced = false,
            onAdvancedRequest = {
                showQuickProductDialog = false
                selectedModule = "Productos"
                openAdvancedProductForm = true
            },
        )
    }
    CashFlowHost(session, state, posVm)
}
