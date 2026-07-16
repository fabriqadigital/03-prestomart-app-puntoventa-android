package com.ecommerce.ecommerceposapp.presentation.users

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.CrudEditDeleteIcons
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.ToolbarAddIconButton
import com.ecommerce.ecommerceposapp.presentation.users.UsersViewModel
import java.io.File

@Composable
fun UsersCrudScreen(vm: UsersViewModel, session: UserSession) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
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
                        Text("${row.email} - ${row.role}", style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeleteIcons(
                        onEdit = { editing = row },
                        onDelete = {
                            pendingConfirm = PendingConfirm(
                                title = "Eliminar usuario",
                                body = "Eliminar a ${row.name} (${row.email})? Dejara de mostrarse en el listado.",
                                confirmButtonText = "Eliminar",
                            onConfirm = { vm.remove(row.id, session.id) },
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
                vm.save(row, pwd)
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
                vm.save(r, pwd)
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
                    label = { Text(if (initial.id == 0L) "Contrasena" else "Contrasena (vacio = no cambiar)") },
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
