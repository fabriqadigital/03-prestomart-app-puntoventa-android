package com.ecommerce.ecommerceposapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.ClientRow
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.UserRow
import com.ecommerce.ecommerceposapp.domain.UserSession

private fun parseLong(s: String, default: Long = 0L): Long = s.trim().toLongOrNull() ?: default
private fun parseDouble(s: String, default: Double = 0.0): Double = s.trim().replace(',', '.').toDoubleOrNull() ?: default

private data class PendingConfirm(
    val title: String,
    val body: String,
    val confirmButtonText: String,
    val onConfirm: () -> Unit,
)

@Composable
private fun ConfirmDestructiveDialog(pending: PendingConfirm?, onDismiss: () -> Unit) {
    val p = pending ?: return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(p.title) },
        text = { Text(p.body) },
        confirmButton = {
            TextButton(
                onClick = {
                    p.onConfirm()
                    onDismiss()
                },
            ) {
                Text(p.confirmButtonText, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
    )
}

@Composable
private fun RowScope.CrudEditDeleteIcons(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    deleteContentDescription: String,
) {
    IconButton(onClick = onDelete) {
        Icon(Icons.Filled.Delete, contentDescription = deleteContentDescription)
    }
    IconButton(onClick = onEdit) {
        Icon(Icons.Filled.Edit, contentDescription = "Editar")
    }
}

@Composable
private fun RowScope.CrudEditDeactivateIcons(
    onEdit: () -> Unit,
    onDeactivate: () -> Unit,
) {
    IconButton(onClick = onDeactivate) {
        Icon(Icons.Filled.Delete, contentDescription = "Desactivar")
    }
    IconButton(onClick = onEdit) {
        Icon(Icons.Filled.Edit, contentDescription = "Editar")
    }
}

@Composable
private fun ToolbarAddIconButton(onClick: () -> Unit, contentDescription: String) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = contentDescription)
    }
}

@Composable
fun UsersCrudScreen(vm: MaestroViewModel, session: UserSession) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editing by remember { mutableStateOf<UserRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Usuarios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nuevo usuario")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error); Spacer(Modifier.height(4.dp)) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(4.dp)) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.users, key = { it.id }) { row ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(row.name, fontWeight = FontWeight.Medium)
                        Text("${row.email} · ${row.role}", style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeleteIcons(
                        onEdit = { editing = row },
                        onDelete = {
                            pendingConfirm = PendingConfirm(
                                title = "Eliminar usuario",
                                body = "¿Eliminar a «${row.name}» (${row.email})? Dejará de mostrarse en el listado.",
                                confirmButtonText = "Eliminar",
                                onConfirm = { vm.removeUser(row.id, session.id) },
                            )
                        },
                        deleteContentDescription = "Eliminar usuario",
                    )
                }
            }
        }
    }

    if (showCreate) {
        UserEditDialog(
            title = "Nuevo usuario",
            initial = UserRow(0, "", "", "admin", true),
            onDismiss = { showCreate = false; vm.clearMessages() },
            onSave = { row, pwd ->
                vm.saveUser(row, pwd)
                showCreate = false
            },
        )
    }
    editing?.let { row ->
        UserEditDialog(
            title = "Editar usuario",
            initial = row,
            onDismiss = { editing = null; vm.clearMessages() },
            onSave = { r, pwd ->
                vm.saveUser(r, pwd)
                editing = null
            },
        )
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun UserEditDialog(
    title: String,
    initial: UserRow,
    onDismiss: () -> Unit,
    onSave: (UserRow, String?) -> Unit,
) {
    var email by remember(initial) { mutableStateOf(initial.email) }
    var name by remember(initial) { mutableStateOf(initial.name) }
    var role by remember(initial) { mutableStateOf(initial.role) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    var password by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Rol") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(selected = active, onClick = { active = !active }, label = { Text("Activo") })
                }
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (initial.id == 0L) "Contraseña" else "Contraseña (vacío = no cambiar)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            IconButton(onClick = {
                onSave(
                    UserRow(initial.id, email, name, role, active),
                    password.ifBlank { null },
                )
            }) { Icon(Icons.Filled.Check, contentDescription = "Guardar") }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}

@Composable
fun ClientsCrudScreen(vm: MaestroViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editing by remember { mutableStateOf<ClientRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Clientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nuevo cliente")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.clients, key = { it.id }) { row ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(row.name, fontWeight = FontWeight.Medium)
                        Text("${row.document} · ${row.phone}", style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeleteIcons(
                        onEdit = { editing = row },
                        onDelete = {
                            pendingConfirm = PendingConfirm(
                                title = "Eliminar cliente",
                                body = "¿Eliminar al cliente «${row.name}»? Dejará de mostrarse en el listado.",
                                confirmButtonText = "Eliminar",
                                onConfirm = { vm.removeClient(row.id) },
                            )
                        },
                        deleteContentDescription = "Eliminar cliente",
                    )
                }
            }
        }
    }
    if (showCreate) {
        ClientEditDialog(ClientRow(0, "", "", ""), onDismiss = { showCreate = false; vm.clearMessages() }, onSave = { vm.saveClient(it); showCreate = false })
    }
    editing?.let { row ->
        ClientEditDialog(row, onDismiss = { editing = null; vm.clearMessages() }, onSave = { vm.saveClient(it); editing = null })
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun ClientEditDialog(initial: ClientRow, onDismiss: () -> Unit, onSave: (ClientRow) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var doc by remember(initial) { mutableStateOf(initial.document) }
    var phone by remember(initial) { mutableStateOf(initial.phone) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nuevo cliente" else "Editar cliente") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = doc, onValueChange = { doc = it }, label = { Text("Documento") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            IconButton(onClick = { onSave(ClientRow(initial.id, name, doc, phone, active = true)) }) {
                Icon(Icons.Filled.Check, contentDescription = "Guardar")
            }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}

@Composable
fun SuppliersCrudScreen(vm: MaestroViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editing by remember { mutableStateOf<SupplierRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Proveedores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nuevo proveedor")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.suppliers, key = { it.id }) { row ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(row.businessName, fontWeight = FontWeight.Medium)
                        Text("${row.ruc} · ${row.phone}", style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeleteIcons(
                        onEdit = { editing = row },
                        onDelete = {
                            pendingConfirm = PendingConfirm(
                                title = "Eliminar proveedor",
                                body = "¿Eliminar al proveedor «${row.businessName}»? Dejará de mostrarse en el listado.",
                                confirmButtonText = "Eliminar",
                                onConfirm = { vm.removeSupplier(row.id) },
                            )
                        },
                        deleteContentDescription = "Eliminar proveedor",
                    )
                }
            }
        }
    }
    if (showCreate) {
        SupplierEditDialog(SupplierRow(0, "", "", ""), onDismiss = { showCreate = false; vm.clearMessages() }, onSave = { vm.saveSupplier(it); showCreate = false })
    }
    editing?.let { row ->
        SupplierEditDialog(row, onDismiss = { editing = null; vm.clearMessages() }, onSave = { vm.saveSupplier(it); editing = null })
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun SupplierEditDialog(initial: SupplierRow, onDismiss: () -> Unit, onSave: (SupplierRow) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial.businessName) }
    var ruc by remember(initial) { mutableStateOf(initial.ruc) }
    var phone by remember(initial) { mutableStateOf(initial.phone) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nuevo proveedor" else "Editar proveedor") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Razón social") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ruc, onValueChange = { ruc = it }, label = { Text("RUC") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            IconButton(onClick = { onSave(SupplierRow(initial.id, name, ruc, phone, active = true)) }) {
                Icon(Icons.Filled.Check, contentDescription = "Guardar")
            }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}

@Composable
fun CategoriesCrudScreen(vm: MaestroViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editing by remember { mutableStateOf<CategoryAdminRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Categorías", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nueva categoría")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.categories, key = { it.id }) { row ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(row.name, fontWeight = FontWeight.Medium)
                    }
                    CrudEditDeactivateIcons(
                        onEdit = { editing = row },
                        onDeactivate = {
                            pendingConfirm = PendingConfirm(
                                title = "Desactivar categoría",
                                body = "¿Desactivar la categoría «${row.name}»? Dejará de mostrarse en el listado.",
                                confirmButtonText = "Desactivar",
                                onConfirm = { vm.removeCategory(row.id) },
                            )
                        },
                    )
                }
            }
        }
    }
    if (showCreate) {
        CategoryEditDialog(CategoryAdminRow(0, "", true), onDismiss = { showCreate = false; vm.clearMessages() }, onSave = { vm.saveCategory(it); showCreate = false })
    }
    editing?.let { row ->
        CategoryEditDialog(row, onDismiss = { editing = null; vm.clearMessages() }, onSave = { vm.saveCategory(it); editing = null })
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun CategoryEditDialog(initial: CategoryAdminRow, onDismiss: () -> Unit, onSave: (CategoryAdminRow) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nueva categoría" else "Editar categoría") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Row { FilterChip(selected = active, onClick = { active = !active }, label = { Text("Activa") }) }
            }
        },
        confirmButton = {
            IconButton(onClick = { onSave(CategoryAdminRow(initial.id, name, active)) }) {
                Icon(Icons.Filled.Check, contentDescription = "Guardar")
            }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}

@Composable
fun ProductsCrudScreen(vm: MaestroViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editing by remember { mutableStateOf<ProductAdminRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    val visibleCategories = state.categories.filter { it.active }
    val filteredProducts = state.products
        .filter { it.active }
        .filter { selectedCategoryId == null || it.categoryId == selectedCategoryId }
        .filter {
            search.isBlank() ||
                it.name.contains(search, ignoreCase = true) ||
                it.code.contains(search, ignoreCase = true)
        }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Productos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton(onClick = { showCreate = true }, contentDescription = "Nuevo producto")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar producto...") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 2.dp)) {
            item {
                FilterChip(
                    selected = selectedCategoryId == null,
                    onClick = { selectedCategoryId = null },
                    label = { Text("Todos") },
                )
            }
            items(visibleCategories, key = { it.id }) { category ->
                Spacer(Modifier.padding(horizontal = 4.dp))
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = { selectedCategoryId = category.id },
                    label = { Text(category.name) },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 170.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(filteredProducts, key = { it.id }) { product ->
                ProductCrudCard(
                    product = product,
                    onEdit = { editing = product },
                    onDelete = {
                        pendingConfirm = PendingConfirm(
                            title = "Desactivar producto",
                            body = "¿Desactivar el producto «${product.name}»? Dejará de mostrarse en el listado.",
                            confirmButtonText = "Desactivar",
                            onConfirm = { vm.removeProduct(product.id) },
                        )
                    },
                )
            }
        }
    }
    if (showCreate) {
        ProductEditDialog(
            ProductAdminRow(0, 1, "", "", "", 0.0, 0.0, true),
            state.categories,
            onDismiss = { showCreate = false; vm.clearMessages() },
            onSave = { vm.saveProduct(it); showCreate = false },
        )
    }
    editing?.let { row ->
        ProductEditDialog(row, state.categories, onDismiss = { editing = null; vm.clearMessages() }, onSave = { vm.saveProduct(it); editing = null })
    }
    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun ProductCrudCard(
    product: ProductAdminRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val primaryBlue = Color(0xFFfd0505)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (product.imageUrl.isBlank()) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                        )
                    } else {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
            Text(
                text = product.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "S/ ${"%.2f".format(product.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = primaryBlue,
            )
            Spacer(Modifier.height(2.dp))
            Text("Stock: ${product.stock.toInt()}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar producto",
                        tint = Color(0xFF333333),
                    )
                }
                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar producto",
                        tint = MaterialTheme.colorScheme.onError,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductEditDialog(
    initial: ProductAdminRow,
    categories: List<CategoryAdminRow>,
    onDismiss: () -> Unit,
    onSave: (ProductAdminRow) -> Unit,
) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var code by remember(initial) { mutableStateOf(initial.code) }
    var imageUrl by remember(initial) { mutableStateOf(initial.imageUrl) }
    var catId by remember(initial) { mutableStateOf(initial.categoryId.toString()) }
    var price by remember(initial) { mutableStateOf(initial.price.toString()) }
    var stock by remember(initial) { mutableStateOf(initial.stock.toString()) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nuevo producto" else "Editar producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL imagen") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = catId, onValueChange = { catId = it }, label = { Text("Id categoría") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
                Row { FilterChip(selected = active, onClick = { active = !active }, label = { Text("Activo") }) }
                Text("Categorías: " + categories.filter { it.active }.joinToString { "${it.id} ${it.name}" }, style = MaterialTheme.typography.labelSmall)
            }
        },
        confirmButton = {
            IconButton(onClick = {
                onSave(
                    ProductAdminRow(
                        id = initial.id,
                        categoryId = parseLong(catId, initial.categoryId),
                        name = name,
                        code = code,
                        imageUrl = imageUrl,
                        price = parseDouble(price, initial.price),
                        stock = parseDouble(stock, initial.stock),
                        active = active,
                    ),
                )
            }) { Icon(Icons.Filled.Check, contentDescription = "Guardar") }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") }
        },
    )
}

@Composable
fun ProfileScreen(session: UserSession, onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mi perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Nombre: ${session.name}")
        Text("Correo: ${session.email}")
        Text("Rol: ${session.role}")
        Text("Sesión: ${if (session.offlineSession) "Sin conexión (Realm)" else "En línea"}")
        Spacer(Modifier.height(16.dp))
        IconButton(onClick = onLogout) {
            Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión")
        }
    }
}
