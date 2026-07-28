package com.ecommerce.ecommerceposapp.presentation.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.ui.theme.AppBackground
import com.ecommerce.ecommerceposapp.ui.theme.BadgeVariant
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.ButtonSize
import com.ecommerce.ecommerceposapp.ui.theme.GrayLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayMedium
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess
import com.ecommerce.ecommerceposapp.ui.theme.OutlineButton
import com.ecommerce.ecommerceposapp.ui.theme.PosBadge
import com.ecommerce.ecommerceposapp.ui.theme.PosCard
import com.ecommerce.ecommerceposapp.ui.theme.PosEmptyState
import com.ecommerce.ecommerceposapp.ui.theme.PosLinearLoader
import com.ecommerce.ecommerceposapp.ui.theme.PosTextField
import com.ecommerce.ecommerceposapp.ui.theme.PrimaryButton
import com.ecommerce.ecommerceposapp.ui.theme.Radius
import com.ecommerce.ecommerceposapp.ui.theme.RedDanger
import com.ecommerce.ecommerceposapp.ui.theme.Spacing
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceSubtle
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max

private const val UsersPageSize = 8

@Composable
fun UsersCrudScreen(vm: UsersViewModel, session: UserSession) {
    val state by vm.uiState.collectAsState()
    var editing by remember { mutableStateOf<UserRow?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    var page by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) { vm.load() }

    val users = state.users.sortedWith(
        compareByDescending<UserRow> { it.createdAt }.thenByDescending { it.id },
    )
    val pageCount = max(1, ceil(users.size / UsersPageSize.toDouble()).toInt())
    if (page > pageCount - 1) page = pageCount - 1
    val visibleUsers = users.drop(page * UsersPageSize).take(UsersPageSize)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(Spacing.xl),
    ) {
        UsersHeader(
            total = users.size,
            isLoading = state.isLoading,
            onRefresh = vm::load,
            onCreate = { showCreate = true },
        )

        Spacer(Modifier.height(Spacing.lg))

        state.error?.let {
            UsersMessage(text = it, color = RedDanger)
            Spacer(Modifier.height(Spacing.sm))
        }
        state.message?.let {
            UsersMessage(text = it, color = GreenSuccess)
            Spacer(Modifier.height(Spacing.sm))
        }

        UsersTable(
            users = visibleUsers,
            isLoading = state.isLoading,
            modifier = Modifier.weight(1f),
            onEdit = { editing = it },
            onDelete = { row ->
                pendingConfirm = PendingConfirm(
                    title = "Eliminar usuario",
                    body = "Eliminar a ${row.displayName()} (${row.email})? Dejara de mostrarse en el listado.",
                    confirmButtonText = "Eliminar",
                    onConfirm = { vm.remove(row.id, session.id) },
                )
            },
        )

        Spacer(Modifier.height(Spacing.md))

        UsersPagination(
            currentPage = page,
            pageCount = pageCount,
            total = users.size,
            onPrevious = { page = (page - 1).coerceAtLeast(0) },
            onNext = { page = (page + 1).coerceAtMost(pageCount - 1) },
        )
    }

    if (showCreate) {
        UserEditDialog(
            title = "Nuevo usuario",
            initial = UserRow(0, "", "", "Cliente web", true),
            isSaving = state.isSaving,
            onDismiss = { showCreate = false; vm.clearMessages() },
            onSave = { row, password ->
                vm.save(row, password)
                showCreate = false
            },
        )
    }

    editing?.let { row ->
        UserEditDialog(
            title = "Editar usuario",
            initial = row,
            isSaving = state.isSaving,
            onDismiss = { editing = null; vm.clearMessages() },
            onSave = { updated, password ->
                vm.save(updated, password)
                editing = null
            },
        )
    }

    ConfirmDestructiveDialog(pendingConfirm, onDismiss = { pendingConfirm = null })
}

@Composable
private fun UsersHeader(
    total: Int,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onCreate: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                Text(
                    "Usuarios web",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                PosBadge("$total", BadgeVariant.Muted)
            }
            Spacer(Modifier.height(Spacing.xs))
            Text(
                "Administra las cuentas registradas desde la tienda y la aplicacion.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            OutlinedButton(
                onClick = onRefresh,
                enabled = !isLoading,
                shape = RoundedCornerShape(Radius.md),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandRed),
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(Spacing.xs))
                Text("Actualizar")
            }
            PrimaryButton(
                text = "Nuevo usuario",
                onClick = onCreate,
                enabled = !isLoading,
                leadingIcon = Icons.Filled.Add,
            )
        }
    }
}

@Composable
private fun UsersMessage(text: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.md),
        color = color.copy(alpha = 0.08f),
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
        )
    }
}

@Composable
private fun UsersTable(
    users: List<UserRow>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onEdit: (UserRow) -> Unit,
    onDelete: (UserRow) -> Unit,
) {
    PosCard(modifier = modifier.fillMaxWidth(), radius = Radius.lg) {
        Column(Modifier.fillMaxSize()) {
            if (isLoading) PosLinearLoader()

            UsersTableHeader()
            HorizontalDivider(color = BorderDefault)

            if (!isLoading && users.isEmpty()) {
                PosEmptyState(
                    icon = Icons.Filled.People,
                    title = "Sin usuarios",
                    description = "Cuando existan cuentas registradas apareceran aqui.",
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(users, key = { it.id }) { user ->
                        UsersTableRow(user, onEdit, onDelete)
                        HorizontalDivider(color = BorderDefault)
                    }
                }
            }
        }
    }
}

@Composable
private fun UsersTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceSubtle)
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderCell("ID", 0.65f)
        HeaderCell("Nombre", 1.5f)
        HeaderCell("Correo", 2f)
        HeaderCell("Fecha", 1.15f)
        HeaderCell("Estado", 0.9f)
        Box(Modifier.width(48.dp))
    }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
    )
}

@Composable
private fun UsersTableRow(
    user: UserRow,
    onEdit: (UserRow) -> Unit,
    onDelete: (UserRow) -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BodyCell("#${user.id}", 0.65f, color = TextSecondary)
        BodyCell(user.displayName(), 1.5f, fontWeight = FontWeight.SemiBold)
        BodyCell(user.email.ifBlank { "-" }, 2f, color = TextSecondary)
        BodyCell(user.createdAtLabel(), 1.15f, color = TextSecondary)
        Box(Modifier.weight(0.9f)) {
            PosBadge(
                text = if (user.active) "Activo" else "Bloqueado",
                variant = if (user.active) BadgeVariant.Success else BadgeVariant.Error,
            )
        }
        Box(Modifier.width(48.dp), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Acciones", tint = TextSecondary)
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        menuOpen = false
                        onEdit(user)
                    },
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        menuOpen = false
                        onDelete(user)
                    },
                )
            }
        }
    }
}

@Composable
private fun RowScope.BodyCell(
    text: String,
    weight: Float,
    color: Color = TextPrimary,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        fontWeight = fontWeight,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun UsersPagination(
    currentPage: Int,
    pageCount: Int,
    total: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            "$total usuarios",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            IconButton(
                onClick = onPrevious,
                enabled = currentPage > 0,
                modifier = Modifier
                    .size(40.dp)
                    .background(SurfaceMuted, RoundedCornerShape(Radius.md)),
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Anterior")
            }
            Text(
                "Pagina ${currentPage + 1} de $pageCount",
                modifier = Modifier.widthIn(min = 112.dp),
                textAlign = TextAlign.Center,
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall,
            )
            IconButton(
                onClick = onNext,
                enabled = currentPage < pageCount - 1,
                modifier = Modifier
                    .size(40.dp)
                    .background(SurfaceMuted, RoundedCornerShape(Radius.md)),
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Siguiente")
            }
        }
    }
}

@Composable
private fun UserEditDialog(
    title: String,
    initial: UserRow,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (UserRow, String?) -> Unit,
) {
    var email by remember(initial) { mutableStateOf(initial.email) }
    var name by remember(initial) { mutableStateOf(initial.displayName()) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    var password by remember(initial.id) { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        title = {
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                PosTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = "Nombre",
                    isError = nameError,
                    errorMessage = "El nombre es obligatorio",
                )
                PosTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = "Correo",
                    isError = emailError,
                    errorMessage = "Ingrese un correo valido",
                )
                PosTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = if (initial.id == 0L) "Contrasena" else "Contrasena (vacio = no cambiar)",
                )
                FilterChip(
                    selected = active,
                    onClick = { active = !active },
                    label = { Text(if (active) "Activo" else "Bloqueado") },
                    leadingIcon = if (active) {
                        { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null,
                    shape = RoundedCornerShape(Radius.pill),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandRed,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceMuted,
                        labelColor = TextSecondary,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = active,
                        selectedBorderColor = BrandRed,
                        borderColor = BorderDefault,
                        selectedBorderWidth = 1.dp,
                        borderWidth = 1.dp,
                    ),
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = if (initial.id == 0L) "Crear" else "Guardar",
                onClick = {
                    emailError = !email.contains("@") || email.isBlank()
                    nameError = name.isBlank()
                    if (emailError || nameError) return@PrimaryButton

                    onSave(
                        initial.copy(
                            email = email.trim(),
                            name = name.trim(),
                            username = name.trim(),
                            active = active,
                            isBlocked = if (active) 0 else 1,
                        ),
                        password.ifBlank { null },
                    )
                },
                loading = isSaving,
                height = ButtonSize.heightSmall,
            )
        },
        dismissButton = {
            OutlineButton(
                text = "Cancelar",
                onClick = onDismiss,
                enabled = !isSaving,
                leadingIcon = Icons.Filled.Close,
                height = ButtonSize.heightSmall,
                color = GrayMedium,
            )
        },
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(Radius.lg),
    )
}

private fun UserRow.displayName(): String = name.ifBlank { username }.ifBlank { email }.ifBlank { "Usuario" }

private fun UserRow.createdAtLabel(): String {
    if (createdAt <= 0L) return "-"
    return runCatching {
        SimpleDateFormat("dd/MM/yyyy", Locale("es", "PE")).format(Date(createdAt))
    }.getOrDefault("-")
}
