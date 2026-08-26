package com.ecommerce.ecommerceposapp.presentation.navigation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.SystemClock
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.BackHandler
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.ecommerce.ecommerceposapp.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.sp
import java.util.Locale
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.domain.sync.SyncPlan
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
import com.ecommerce.ecommerceposapp.util.DataWedgeScanner
import com.ecommerce.ecommerceposapp.util.PosIdleMonitor
import com.ecommerce.ecommerceposapp.presentation.sales.SalesHistoryScreen
import com.ecommerce.ecommerceposapp.presentation.sync.SyncViewModel
import com.ecommerce.ecommerceposapp.presentation.cash.CashModuleScreen
import com.ecommerce.ecommerceposapp.presentation.cash.CashModuleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private const val LOGIN = "login"
private const val SYNC = "sync"
private const val POS = "pos"

// Aliases locales — usan los tokens del DesignSystem
private val Brand         = com.ecommerce.ecommerceposapp.ui.theme.BrandRed
private val BrandDark     = com.ecommerce.ecommerceposapp.ui.theme.BrandRedDark
private val AppBg         = com.ecommerce.ecommerceposapp.ui.theme.AppBackground
private val SurfaceWhite  = com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
private val SurfaceAlt    = com.ecommerce.ecommerceposapp.ui.theme.SurfaceSubtle
private val TextPrimary   = com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
private val TextSecondary = com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
private val Divider       = com.ecommerce.ecommerceposapp.ui.theme.BorderDefault

@Composable
private fun FloatingRightNotice(data: SnackbarData) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        androidx.compose.material3.Card(
            modifier = Modifier.widthIn(min = 300.dp, max = 440.dp),
            shape = RoundedCornerShape(com.ecommerce.ecommerceposapp.ui.theme.Radius.lg),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = com.ecommerce.ecommerceposapp.ui.theme.DarkSurface
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess)
                Text(
                    data.visuals.message,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodySmall,
                )
                IconButton(onClick = data::dismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color(0xFFCBD5E1))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  PANTALLA DE CARGA INICIAL (SPLASH)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AppLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp),
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "PrestoMart POS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                )
                Text(
                    "Cargando tu sesión…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }
            CircularProgressIndicator(
                color = Brand,
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  PANTALLA DE CARGA DEL POS (catálogo + caja)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PosLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CircularProgressIndicator(color = Brand, modifier = Modifier.size(40.dp), strokeWidth = 3.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Preparando el punto de venta",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
                Text(
                    "Cargando catálogo y configuración de caja…",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
        }
    }
}

@Composable
fun PosAppRoot(navController: NavHostController = rememberNavController()) {
    var currentUser by remember { mutableStateOf<UserSession?>(null) }
    val auth: AuthRepository = koinInject()
    val syncRepo: SyncRepository = koinInject()

  
    var startDestination by remember { mutableStateOf<String?>(null) }

   
    LaunchedEffect(Unit) {
        val saved = auth.getSession()
        val target = if (saved != null && (saved.offlineSession || auth.hasStoredToken())) {
            currentUser = saved
            val synced = withContext(Dispatchers.IO) {
                syncRepo.hasInitialSync(saved.id)
            }
            if (synced) POS else SYNC
        } else {
            LOGIN
        }
        startDestination = target
    }

   
    if (startDestination == null) {
        Box(Modifier.fillMaxSize().background(Color.White))
        return
    }

    NavHost(navController = navController, startDestination = startDestination!!) {
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
                vm.loadModules(requiresSync)
                if (requiresSync) vm.sync(user)
            }
            SyncScreen(
                user = user,
                state = state,
                requiresSync = requiresSync,
                onSync = { syncScope.launch { vm.sync(user) } },
                onToggleModule = vm::toggleModule,
                onSelectAll = vm::selectAllModules,
                onClearSelection = vm::clearSelection,
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
            val session = currentUser ?: auth.getSession()?.also { currentUser = it }
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
            val cashModuleVm: CashModuleViewModel = koinViewModel()
            val posState by posVm.uiState.collectAsState()
            // Carga local inmediata: catálogo desde Realm + caja desde caché (< 200 ms).
            // El splash solo cubre ese tiempo mínimo — sin red, sin demoras.
            LaunchedEffect(Unit) { posVm.loadImmediate(session.cashierId) }
            if (posState.initialLoading) {
                PosLoadingScreen()
                return@composable
            }
            PosScreen(
                session = session,
                categoriesVm = categoriesVm,
                clientsVm = clientsVm,
                productsVm = productsVm,
                suppliersVm = suppliersVm,
                usersVm = usersVm,
                posVm = posVm,
                onSessionUpdated = { currentUser = it },
                cashModuleVm = cashModuleVm,
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
                Spacer(Modifier.height(if (compactHeight) 4.dp else 8.dp))
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

private data class DrawerMenuEntry(val label: String, val icon: ImageVector)

private val drawerMenuItems = listOf(
    DrawerMenuEntry("Punto de venta", Icons.Filled.PointOfSale),
    DrawerMenuEntry("Historial de ventas", Icons.Filled.ReceiptLong),
    DrawerMenuEntry("Caja", Icons.Filled.AccountBalanceWallet),
    DrawerMenuEntry("Productos", Icons.Filled.Inventory2),
    DrawerMenuEntry("Categorías", Icons.Filled.Category),
    DrawerMenuEntry("Clientes", Icons.Filled.People),
    DrawerMenuEntry("Proveedores", Icons.Filled.LocalShipping),
)

@Composable
private fun PosNavigationDrawerContent(
    selectedLabel: String,
    session: UserSession,
    isOnline: Boolean,
    pendingSyncCount: Long,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val avatarModel = remember(session.avatar, session.avatarBase64) { session.avatarModel() }
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 16.dp),
    ) {
        Text("PRESTOMART", color = Brand, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
        Text("Punto de venta", color = Color(0xFF94A3B8), style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .clickable { onItemClick("Mi perfil") }
                .padding(horizontal = 12.dp, vertical = 14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0xFFFFE4E6)),
                contentAlignment = Alignment.Center,
            ) {
                if (avatarModel != null) {
                    if (avatarModel is ImageBitmap) {
                        Image(bitmap = avatarModel, contentDescription = "Foto de ${session.name}", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        AsyncImage(model = avatarModel, contentDescription = "Foto de ${session.name}", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    }
                } else {
                    Text(session.name.trim().firstOrNull()?.uppercase() ?: "C", color = Brand, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "${session.name} ${session.lastName}".trim().ifBlank { "Cajero" },
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text("NAVEGACIÓN", color = Color(0xFF94A3B8), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
            drawerMenuItems.forEach { item ->
                val selected = item.label == selectedLabel
                NavigationDrawerItem(
                    icon = {
                        Box(
                            Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(if (selected) Color.White else Color.Transparent),
                            contentAlignment = Alignment.Center,
                        ) { Icon(item.icon, contentDescription = null, modifier = Modifier.size(20.dp)) }
                    },
                    label = { Text(item.label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) },
                    selected = selected,
                    onClick = { onItemClick(item.label) },
                    shape = RoundedCornerShape(14.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFFFE8E8),
                        unselectedContainerColor = Color.Transparent,
                        selectedIconColor = Brand,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Brand,
                        unselectedTextColor = Color(0xFF334155),
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
            Spacer(Modifier.height(10.dp))
            val syncNeedsAttention = !isOnline || pendingSyncCount > 0
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Sync, contentDescription = null, modifier = Modifier.size(22.dp)) },
                label = {
                    Column {
                        Text("Sincronizar catálogo", fontWeight = FontWeight.Bold)
                        Text(
                            when {
                                !isOnline && pendingSyncCount > 0 -> "$pendingSyncCount cambio(s) · Sin conexión"
                                !isOnline -> "Sin conexión"
                                pendingSyncCount > 0 -> "$pendingSyncCount cambio(s) por enviar"
                                else -> "Todo actualizado"
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                },
                badge = {
                    if (syncNeedsAttention) {
                        Box(
                            Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFEDD5)).padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(if (pendingSyncCount > 0) "↕ $pendingSyncCount" else "↕", color = Color(0xFFC2410C), fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                },
                selected = false,
                onClick = { onItemClick("Sincronizar catálogo") },
                shape = RoundedCornerShape(14.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = if (syncNeedsAttention) Color(0xFFFFF7ED) else Color.White,
                    unselectedIconColor = if (syncNeedsAttention) Color(0xFFC2410C) else Color(0xFF475569),
                    unselectedTextColor = if (syncNeedsAttention) Color(0xFF9A3412) else Color(0xFF334155),
                ),
            )
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
        Spacer(Modifier.height(10.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Logout, contentDescription = null) },
            label = { Text("Cerrar sesión", fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { onItemClick("Cerrar sesión") },
            shape = RoundedCornerShape(14.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Brand,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
            ),
        )
        Text(
            "V1.2 · PrestoMart POS",
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
    onSessionUpdated: (UserSession) -> Unit,
    cashModuleVm: CashModuleViewModel,
    onLogout: () -> Unit,
    onGoSync: () -> Unit,
) {
    val catalog: CatalogRepository = koinInject()
    val authRepository: AuthRepository = koinInject()
    val syncRepository: SyncRepository = koinInject()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val view = LocalView.current

    DisposableEffect(view, activity) {
        view.keepScreenOn = true
        PosIdleMonitor.reset()
        onDispose {
            view.keepScreenOn = false
            activity?.window?.let { window ->
                window.attributes = window.attributes.apply {
                    screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                }
            }
        }
    }

    LaunchedEffect(activity) {
        val window = activity?.window ?: return@LaunchedEffect
        var dimmed = false
        try {
            while (true) {
                delay(PosIdleMonitor.POLL_INTERVAL_MILLIS)
                val dimNow = PosIdleMonitor.shouldDim()
                if (dimNow != dimmed) {
                    dimmed = dimNow
                    window.attributes = window.attributes.apply {
                        screenBrightness = if (dimNow) {
                            PosIdleMonitor.DIMMED_BRIGHTNESS
                        } else {
                            PosIdleMonitor.NORMAL_BRIGHTNESS
                        }
                    }
                }
            }
        } finally {
            window.attributes = window.attributes.apply {
                screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
        }
    }

    val connectivity = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    fun hasValidatedInternet(): Boolean = connectivity.activeNetwork?.let { network ->
        connectivity.getNetworkCapabilities(network)?.let { capabilities ->
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    } == true
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
    var mobileCartOpen by rememberSaveable { mutableStateOf(false) }
    var showDiscountScreen by rememberSaveable { mutableStateOf(false) }
    var showQuickProductDialog by remember { mutableStateOf(false) }
    var openAdvancedProductForm by remember { mutableStateOf(false) }
    var newProductCategoryId by remember { mutableStateOf<Long?>(null) }
    var newProductSubcategoryId by remember { mutableStateOf<Long?>(null) }
    var isOnline by remember { mutableStateOf(hasValidatedInternet()) }
    var showReconnectPrompt by remember { mutableStateOf(false) }
    var reconnectSyncing by remember { mutableStateOf(false) }
    var pendingSyncCount by remember { mutableStateOf(0L) }
    val widthDp = LocalConfiguration.current.screenWidthDp
    val heightDp = LocalConfiguration.current.screenHeightDp
    val responsiveTwoPanels = widthDp >= 900
    val compactPosNavigation = widthDp < 700
    val expandedCartFraction =
        ((if (heightDp < 650) 0.615f else 0.545f) - (2f / heightDp.coerceAtLeast(1)))
            .coerceAtLeast(0.1f)
    val mobileCartFraction by animateFloatAsState(
        targetValue = if (state.cart.isEmpty()) {
            if (heightDp < 650) 0.38f else 0.30f
        } else {
            expandedCartFraction
        },
        animationSpec = tween(durationMillis = 220),
        label = "mobileCartHeight",
    )

    DisposableEffect(connectivity) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            private fun updateConnection(online: Boolean) {
                val recovered = online && !isOnline
                isOnline = online
                if (recovered) {
                    // Suspende la auto-sincronización de inmediato: el usuario
                    // aún no decidió si quiere sincronizar y el loop de refresco
                    // periódico (cada 5s) no debe adelantarse a esa decisión.
                    syncRepository.setAutoSyncSuspended(true)
                    showReconnectPrompt = true
                }
            }

            override fun onAvailable(network: Network) {
                updateConnection(hasValidatedInternet())
            }

            override fun onLost(network: Network) {
                updateConnection(hasValidatedInternet())
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                updateConnection(
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
                )
            }
        }
        connectivity.registerDefaultNetworkCallback(callback)
        onDispose { connectivity.unregisterNetworkCallback(callback) }
    }

    LaunchedEffect(connectivity) {
        while (isActive) {
            val online = hasValidatedInternet()
            if (online != isOnline) {
                val recovered = online && !isOnline
                isOnline = online
                if (recovered) {
                    syncRepository.setAutoSyncSuspended(true)
                    showReconnectPrompt = true
                }
            }
            delay(1_000)
        }
    }

    var dataWedgeSequence by remember { mutableStateOf(0) }
    var dataWedgeCode by remember { mutableStateOf("") }
    var lastDataWedgeCode by remember { mutableStateOf("") }
    var lastDataWedgeTime by remember { mutableStateOf(0L) }

    // Evita que al cerrar el carrito y volver al catálogo, CatalogPane se
    // remonte y reprocese el último escaneo de DataWedge como si fuera nuevo.
    LaunchedEffect(mobileCartOpen) {
        if (!mobileCartOpen) {
            dataWedgeSequence = 0
            dataWedgeCode = ""
        }
    }

    DisposableEffect(context, selectedModule) {
        if (selectedModule != "Punto de venta") {
            onDispose { }
        } else {
            val filter = IntentFilter(DataWedgeScanner.SCAN_ACTION).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(c: Context?, intent: Intent?) {
                    Log.d(
                        "DataWedge",
                        "Broadcast recibido: action=${intent?.action} data=${intent?.dataString} " +
                            "extras=${intent?.extras?.keySet()}",
                    )
                    val code = DataWedgeScanner.extractBarcode(intent)
                    if (code == null) {
                        Log.d("DataWedge", "Broadcast ignorado: action no es SCAN o falta data_string.")
                        return
                    }
                    Log.d("BarcodeDebug", "Broadcast DataWedge procesado: [$code] (length=${code.length})")
                    Log.d("DataWedge", "Barcode recibido desde DataWedge: $code")
                    val now = SystemClock.elapsedRealtime()
                    if (code == lastDataWedgeCode && now - lastDataWedgeTime < DataWedgeScanner.DATAWEDGE_DEDUP_MS) {
                        Log.d("DataWedge", "Escaneo duplicado ignorado (ventana 2s): $code")
                        return
                    }
                    lastDataWedgeCode = code
                    lastDataWedgeTime = now
                    dataWedgeCode = code
                    dataWedgeSequence += 1
                }
            }

            Log.d("DataWedge", "Registrando receiver para acción=${DataWedgeScanner.SCAN_ACTION}")
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED,
            )
            onDispose {
                context.unregisterReceiver(receiver)
                Log.d("DataWedge", "Receiver desregistrado")
            }
        }
    }

    LaunchedEffect(Unit) {
        DataWedgeScanner.setupProfile(context.applicationContext)
    }

    val externalScan = if (dataWedgeSequence > 0) {
        DataWedgeScanner.DataWedgeScan(dataWedgeSequence, dataWedgeCode)
    } else {
        null
    }

    LaunchedEffect(selectedModule) {
        if (selectedModule != "Punto de venta") {
            dataWedgeSequence = 0
            dataWedgeCode = ""
        }
    }

    LaunchedEffect(session.id) {
        if (session.offlineSession && hasValidatedInternet()) {
            syncRepository.setAutoSyncSuspended(true)
            isOnline = true
            showReconnectPrompt = true
        }
    }

    LaunchedEffect(state.offlineModeRequested) {
        if (state.offlineModeRequested && !session.offlineSession) {
            isOnline = false
            authRepository.enterOfflineMode()?.let(onSessionUpdated)
        }
    }

    LaunchedEffect(drawerState.isOpen, isOnline, selectedModule) {
        if (drawerState.isOpen) {
            pendingSyncCount = withContext(Dispatchers.IO) {
                syncRepository.listSyncModuleStatus().sumOf { it.pendingCount + it.failedCount }
            }
        }
    }

    LaunchedEffect(session.cashierId) {
        
        posVm.loadCashSession(session.cashierId)
    }

    val topBarTitle = if (compactPosNavigation && selectedModule == "Punto de venta" && mobileCartOpen) {
        "Carrito"
    } else {
        selectedModule
    }

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
    LaunchedEffect(state.message) {
        val notice = state.message
        if (!notice.isNullOrBlank()) {
            snackbarHostState.showSnackbar(notice)
            posVm.clearMessage()
        }
    }

    LaunchedEffect(selectedModule) {
        if (selectedModule != "Punto de venta") mobileCartOpen = false
        when (selectedModule) {
            "Punto de venta", "Historial de ventas" -> {
                posVm.refreshCatalog(isOnline)
                clientsVm.load()
            }
            "Productos" -> {
                posVm.refreshCatalog(isOnline)
                productsVm.load()
            }
            "Categorías" -> {
                posVm.refreshCatalog(isOnline)
                categoriesVm.loadAll()
            }
        }
    }

    
    LaunchedEffect(isOnline, selectedModule) {
        while (isActive && isOnline) {
            delay(5_000)
            when (selectedModule) {
                "Punto de venta" -> posVm.refreshCatalog(isOnline = true)
                "Productos" -> {
                    productsVm.load()
                    posVm.refreshCatalog(isOnline = true)
                }
                "Categorías" -> {
                    categoriesVm.loadAll()
                    posVm.refreshCatalog(isOnline = true)
                }
                "Clientes" -> clientsVm.load()
                "Proveedores" -> suppliersVm.load()
                "Usuarios" -> usersVm.load()
            }
        }
    }

    BackHandler(enabled = showDiscountScreen) {
        showDiscountScreen = false
    }
    BackHandler(enabled = !showDiscountScreen && compactPosNavigation && selectedModule == "Punto de venta" && mobileCartOpen) {
        dataWedgeSequence = 0
        dataWedgeCode = ""
        mobileCartOpen = false
    }

    val posContent: @Composable (PaddingValues) -> Unit = { padding ->
        when (selectedModule) {
            "Punto de venta" -> {
                val renderDiscountScreen: @Composable (Modifier) -> Unit = { mod ->
                    GlobalDiscountScreen(
                        modifier = mod,
                        cart = state.cart,
                        currentPercent = state.descuentoPorcentaje,
                        currentLineKeys = state.descuentoLineKeys,
                        onApply = { percent, lineKeys ->
                            posVm.applyGlobalDiscount(percent, lineKeys)
                            showDiscountScreen = false
                        },
                        onClear = {
                            posVm.clearGlobalDiscount()
                            showDiscountScreen = false
                        },
                        onBack = { showDiscountScreen = false },
                    )
                }
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    if (state.cashSession != null) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            CashSessionIndicator(
                                state = state,
                                onClose = {
                                    scope.launch { posVm.loadCashSummary() }
                                    requestCashClose()
                                },
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        if (compactPosNavigation) {
                            if (mobileCartOpen) {
                                if (showDiscountScreen) {
                                    renderDiscountScreen(Modifier.fillMaxSize())
                                } else {
                                    CartPane(
                                        Modifier.fillMaxSize(),
                                        state,
                                        "${session.name} ${session.lastName}".trim(),
                                        clientsState.clients,
                                        catalog,
                                        posVm::increase,
                                        posVm::decrease,
                                        posVm::selectConversion,
                                        posVm::updateQuantity,
                                        posVm::removeLine,
                                        onPay = posVm::pay,
                                        onSaveClient = clientsVm::save,
                                        clientsSaving = clientsState.isSaving,
                                        clientsSaveError = clientsState.error,
                                        clientsSaveMessage = clientsState.message,
                                        onClearClientMessages = clientsVm::clearMessages,
                                        onBack = {
                                            dataWedgeSequence = 0
                                            dataWedgeCode = ""
                                            mobileCartOpen = false
                                        },
                                        onOpenDiscount = { showDiscountScreen = true },
                                        onApplyGlobalDiscount = posVm::applyGlobalDiscount,
                                        onClearGlobalDiscount = posVm::clearGlobalDiscount,
                                    )
                                }
                            } else {
                                Column(Modifier.fillMaxSize()) {
                                    CatalogPane(
                                        Modifier.weight(1f).fillMaxWidth(),
                                        externalScan = externalScan,
                                        state,
                                        posVm::setSearch,
                                        posVm::setCategory,
                                        posVm::setSubcategory,
                                        onAddToCart = posVm::addToCart,
                                        onToggleFeatured = posVm::toggleFeatured,
                                        onNewProduct = {
                                            newProductCategoryId = state.selectedCategoryId
                                            newProductSubcategoryId = state.selectedSubcategoryId
                                            showQuickProductDialog = true
                                        },
                                        onScanMessage = { message -> scope.launch { snackbarHostState.showSnackbar(message) } },
                                        onRefresh = { posVm.refreshCatalog(isOnline) },
                                    )
                                    MobileCartSummaryBar(
                                        itemCount = state.cart.size,
                                        total = state.total,
                                        onClick = { mobileCartOpen = true },
                                    )
                                }
                            }
                        } else if (responsiveTwoPanels) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                CatalogPane(
                                    Modifier.weight(1f).fillMaxHeight(),
                                    externalScan = externalScan,
                                    state,
                                    posVm::setSearch,
                                    posVm::setCategory,
                                    posVm::setSubcategory,
                                    onAddToCart = posVm::addToCart,
                                    onToggleFeatured = posVm::toggleFeatured,
                                    onNewProduct = {
                                        newProductCategoryId = state.selectedCategoryId
                                        newProductSubcategoryId = state.selectedSubcategoryId
                                        showQuickProductDialog = true
                                    },
                                    onScanMessage = { message ->
                                        scope.launch { snackbarHostState.showSnackbar(message) }
                                    },
                                    onRefresh = { posVm.refreshCatalog(isOnline) },
                                )
                                if (showDiscountScreen) {
                                    renderDiscountScreen(Modifier.width(360.dp))
                                } else {
                                    CartPane(
                                        Modifier.width(360.dp),
                                        state,
                                        "${session.name} ${session.lastName}".trim(),
                                        clientsState.clients,
                                        catalog,
                                        posVm::increase,
                                        posVm::decrease,
                                        posVm::selectConversion,
                                        posVm::updateQuantity,
                                        posVm::removeLine,
                                        onPay = posVm::pay,
                                        onSaveClient = clientsVm::save,
                                        clientsSaving = clientsState.isSaving,
                                        clientsSaveError = clientsState.error,
                                        clientsSaveMessage = clientsState.message,
                                        onClearClientMessages = clientsVm::clearMessages,
                                        onApplyGlobalDiscount = posVm::applyGlobalDiscount,
                                        onClearGlobalDiscount = posVm::clearGlobalDiscount,
                                        onOpenDiscount = { showDiscountScreen = true },
                                    )
                                }
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxSize()) {
                                CatalogPane(
                                    Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                                    externalScan = externalScan,
                                    state,
                                    posVm::setSearch,
                                    posVm::setCategory,
                                    posVm::setSubcategory,
                                    onAddToCart = posVm::addToCart,
                                    onToggleFeatured = posVm::toggleFeatured,
                                    onNewProduct = {
                                        newProductCategoryId = state.selectedCategoryId
                                        newProductSubcategoryId = state.selectedSubcategoryId
                                        showQuickProductDialog = true
                                    },
                                    onScanMessage = { message ->
                                        scope.launch { snackbarHostState.showSnackbar(message) }
                                    },
                                    onRefresh = { posVm.refreshCatalog(isOnline) },
                                )
                                CartPane(
                                    Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(mobileCartFraction),
                                    state,
                                    "${session.name} ${session.lastName}".trim(),
                                    clientsState.clients,
                                    catalog,
                                    posVm::increase,
                                    posVm::decrease,
                                    posVm::selectConversion,
                                    posVm::updateQuantity,
                                    posVm::removeLine,
                                    onPay = posVm::pay,
                                    onSaveClient = clientsVm::save,
                                    clientsSaving = clientsState.isSaving,
                                    clientsSaveError = clientsState.error,
                                    clientsSaveMessage = clientsState.message,
                                    onClearClientMessages = clientsVm::clearMessages,
                                    onApplyGlobalDiscount = posVm::applyGlobalDiscount,
                                    onClearGlobalDiscount = posVm::clearGlobalDiscount,
                                    onOpenDiscount = { showDiscountScreen = true },
                                )
                            }
                        }
                    }
                }
            }
            "Historial de ventas" -> Box(Modifier.fillMaxSize().padding(padding)) { SalesHistoryScreen(catalog, clientsState.clients) }
            "Productos" -> Box(Modifier.fillMaxSize().padding(padding)) {
                ProductsCrudScreen(
                    vm = productsVm,
                    openCreateAdvanced = openAdvancedProductForm,
                    initialCategoryId = newProductCategoryId,
                    initialSubcategoryId = newProductSubcategoryId,
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
                    catalogRepository = catalog,
                    onSessionUpdated = onSessionUpdated,
                ) { notice -> scope.launch { snackbarHostState.showSnackbar(notice) } }
            }
            "Caja" -> {
                val cashSession = state.cashSession
                if (cashSession != null) {
                    CashModuleScreen(
                        session = cashSession,
                        viewModel = cashModuleVm,
                        onCashClosed = {
                            // Refresca la sesión de caja en PosViewModel y vuelve al POS
                            scope.launch { posVm.loadCashSession(session.cashierId) }
                            selectedModule = "Punto de venta"
                        },
                    )
                } else {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("No hay caja abierta", color = Color(0xFF6B7280), fontWeight = FontWeight.SemiBold)
                            Text("Abre una caja desde el Punto de venta para ver el módulo.", color = Color(0xFF9CA3AF), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            else -> Box(Modifier.fillMaxSize().padding(padding)) { Text("Seleccione una opción del menú.") }
        }
    }

    val scaffold: @Composable () -> Unit = {
        Box(Modifier.fillMaxSize()) {
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
                                if (compactPosNavigation && selectedModule == "Punto de venta" && !mobileCartOpen) {
                                    Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                                        IconButton(onClick = { mobileCartOpen = true }) {
                                            Icon(
                                                Icons.Filled.ShoppingBasket,
                                                contentDescription = "Abrir carrito",
                                                tint = TextPrimary,
                                                modifier = Modifier.size(23.dp),
                                            )
                                        }
                                        val cartQuantity = state.cart.size
                                        if (cartQuantity > 0) {
                                            Surface(
                                                modifier = Modifier.align(Alignment.TopEnd).size(18.dp),
                                                shape = androidx.compose.foundation.shape.CircleShape,
                                                color = Brand,
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        cartQuantity.coerceAtMost(99).toString(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.labelSmall,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(Modifier.width(6.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isOnline) Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                ) {
                                    Text(
                                        if (isOnline) "ONLINE" else "OFFLINE",
                                        color = if (isOnline) Color(0xFF16A34A) else Brand,
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
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(20f)
                    .fillMaxWidth()
                    .padding(top = 76.dp, end = 18.dp, start = 18.dp),
                snackbar = { data -> FloatingRightNotice(data) },
            )
        }
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
                    isOnline = isOnline,
                    pendingSyncCount = pendingSyncCount,
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

    if (showReconnectPrompt) {
        AlertDialog(
            onDismissRequest = {
                if (!reconnectSyncing) {
                    syncRepository.setAutoSyncSuspended(true)
                    showReconnectPrompt = false
                }
            },
            title = { Text(if (reconnectSyncing) "Sincronizando datos" else "Conexión recuperada") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        if (reconnectSyncing) {
                            "Enviando operaciones pendientes y actualizando el catálogo."
                        } else {
                            "Se detectó acceso a Internet por Wi-Fi o datos móviles. ¿Deseas sincronizar ahora con la web?"
                        },
                    )
                    if (reconnectSyncing) {
                        CircularProgressIndicator(color = Brand, modifier = Modifier.size(28.dp))
                    }
                }
            },
            dismissButton = {
                if (!reconnectSyncing) {
                    OutlinedButton(onClick = {
                        syncRepository.setAutoSyncSuspended(true)
                        showReconnectPrompt = false
                    }) {
                        Text("Ahora no")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            reconnectSyncing = true
                            val result = withContext(Dispatchers.IO) {
                                syncRepository.syncModules(
                                    session,
                                    SyncPlan.orderedModules.toSet(),
                                )
                            }
                            reconnectSyncing = false
                            showReconnectPrompt = false
                            result.onSuccess {
                                authRepository.resumeOnlineSession()?.let(onSessionUpdated)
                                posVm.refreshCatalog()
                                productsVm.load()
                                categoriesVm.loadAll()
                                clientsVm.load()
                                suppliersVm.load()
                                snackbarHostState.showSnackbar("Sincronización completada correctamente.")
                            }.onFailure { error ->
                                snackbarHostState.showSnackbar(
                                    error.message ?: "No se pudo completar la sincronización.",
                                )
                            }
                        }
                    },
                    enabled = !reconnectSyncing,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                ) {
                    Text("Sincronizar", color = Color.White)
                }
            },
        )
    }

    if (showQuickProductDialog) {
        ProductEditDialog(
            initial = ProductAdminRow(
                id = 0L,
                categoryId = newProductCategoryId?.takeIf { selectedId ->
                    productsState.categories.any { it.active && it.id == selectedId }
                } ?: productsState.categories.firstOrNull { it.active }?.id ?: 0L,
                subcategoryId = newProductSubcategoryId?.takeIf { selectedId ->
                    productsState.subcategories.any {
                        it.active && it.id == selectedId && it.categoryId == newProductCategoryId
                    }
                } ?: 0L,
                subcategoryIds = listOfNotNull(newProductSubcategoryId),
                name = "",
                code = "",
                imageUrl = "",
                price = 0.0,
                stock = 0.0,
                active = true,
            ),
            categories = productsState.categories,
            subcategories = productsState.subcategories,
            productTypes = productsState.productTypes,
            onDismiss = {
                showQuickProductDialog = false
                productsVm.clearMessages()
            },
            onSave = {
                showQuickProductDialog = false
                productsVm.save(it) {
                    scope.launch {
                        posVm.refreshCatalog(isOnline)
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
    CashFlowHost(
        session = session,
        state = state,
        viewModel = posVm,
        onLoginRequested = onLogout,
    )
}

private fun UserSession.avatarModel(): Any? {
    val encoded = avatarBase64.ifBlank {
        avatar.takeIf { it.startsWith("data:image/") }?.substringAfter("base64,").orEmpty()
    }
    if (encoded.isNotBlank()) {
        return runCatching {
            val bytes = Base64.decode(encoded, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        }.getOrNull()
    }
    if (avatar.startsWith("http://") || avatar.startsWith("https://")) return avatar
    return null
}
