package com.ecommerce.ecommerceposapp.presentation.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.presentation.common.ConfirmDestructiveDialog
import com.ecommerce.ecommerceposapp.presentation.common.CrudEditDeactivateIcons
import com.ecommerce.ecommerceposapp.presentation.common.FieldBorderColor
import com.ecommerce.ecommerceposapp.presentation.common.FieldTextColor
import com.ecommerce.ecommerceposapp.presentation.common.PendingConfirm
import com.ecommerce.ecommerceposapp.presentation.common.ToolbarAddIconButton
import com.ecommerce.ecommerceposapp.presentation.categories.CategoriesViewModel

@Composable
fun CategoriesCrudScreen(vm: CategoriesViewModel) {
    val state by vm.uiState.collectAsState()
    LaunchedEffect(Unit) { vm.loadAll() }
    var editingCategory by remember { mutableStateOf<CategoryAdminRow?>(null) }
    var editingSubcategory by remember { mutableStateOf<SubcategoryAdminRow?>(null) }
    var creatingCategory by remember { mutableStateOf(false) }
    var creatingSubcategoryCategoryId by remember { mutableStateOf<Long?>(null) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    val compact = LocalConfiguration.current.screenWidthDp < 600

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (compact) Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Column {
                Text("Categorias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Organiza las categorias y despliega sus subcategorias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569),
                )
            }
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        creatingSubcategoryCategoryId = state.categories.firstOrNull { it.active }?.id
                    },
                    enabled = state.categories.any { it.active },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("+ Subcategoria") }
                Button(
                    onClick = { creatingCategory = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("+ Nueva categoria") }
            }
        } else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Categorias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Organiza las categorias y despliega sus subcategorias.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { creatingSubcategoryCategoryId = state.categories.firstOrNull { it.active }?.id }, enabled = state.categories.any { it.active }, shape = RoundedCornerShape(8.dp)) { Text("+ Subcategoria") }
                Button(onClick = { creatingCategory = true }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White)) { Text("+ Nueva categoria") }
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(12.dp))
        CategoriesTable(
            categories = state.categories.sortedByDescending { it.id },
            subcategories = state.subcategories,
            onEditCategory = { editingCategory = it },
            onDeleteCategory = { category ->
                pendingConfirm = PendingConfirm(
                    title = "Eliminar categoria",
                    body = "¿Eliminar ${category.name}? Solo se podrá eliminar si no tiene productos asociados.",
                    confirmButtonText = "Eliminar",
                    onConfirm = { vm.removeCategory(category.id) },
                )
            },
            onCreateSubcategory = { creatingSubcategoryCategoryId = it.id },
            onEditSubcategory = { editingSubcategory = it },
            onDeleteSubcategory = { subcategory ->
                pendingConfirm = PendingConfirm(
                    title = "Eliminar subcategoria",
                    body = "¿Eliminar ${subcategory.name}? Solo se podrá eliminar si no tiene productos asociados.",
                    confirmButtonText = "Eliminar",
                    onConfirm = { vm.removeSubcategory(subcategory.id) },
                )
            },
        )
    }

    if (creatingCategory) {
        CategoryEditDialog(CategoryAdminRow(0, "", true), { creatingCategory = false; vm.clearMessages() }) {
            vm.saveCategory(it)
            creatingCategory = false
        }
    }
    editingCategory?.let { category ->
        CategoryEditDialog(category, { editingCategory = null; vm.clearMessages() }) {
            vm.saveCategory(it)
            editingCategory = null
        }
    }
    creatingSubcategoryCategoryId?.let { categoryId ->
        SubcategoryEditDialog(SubcategoryAdminRow(0, categoryId, "", true), state.categories, { creatingSubcategoryCategoryId = null; vm.clearMessages() }) {
            vm.saveSubcategory(it)
            creatingSubcategoryCategoryId = null
        }
    }
    editingSubcategory?.let { subcategory ->
        SubcategoryEditDialog(subcategory, state.categories, { editingSubcategory = null; vm.clearMessages() }) {
            vm.saveSubcategory(it)
            editingSubcategory = null
        }
    }
    ConfirmDestructiveDialog(pendingConfirm) { pendingConfirm = null }
}

@Composable
private fun CategoryEditDialog(
    initial: CategoryAdminRow,
    onDismiss: () -> Unit,
    onSave: (CategoryAdminRow) -> Unit,
) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nueva categoria" else "Editar categoria") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    if (initial.id == 0L) "Crea una categoria para organizar tus productos." else "Actualiza los datos de esta categoria.",
                    color = Color(0xFF64748B),
                )
                OutlinedTextField(
                    name,
                    { name = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Estado", fontWeight = FontWeight.Medium)
                        Text(if (active) "Categoria activa" else "Categoria inactiva", color = Color(0xFF64748B))
                    }
                    Switch(checked = active, onCheckedChange = { active = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(initial.copy(name = name.trim(), active = active)) },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
            ) { Text(if (initial.id == 0L) "Crear categoria" else "Guardar cambios") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) { Text("Cancelar") } },
        containerColor = Color.White,
        tonalElevation = 0.dp,
    )
}

@Composable
private fun SubcategoryEditDialog(
    initial: SubcategoryAdminRow,
    categories: List<CategoryAdminRow>,
    onDismiss: () -> Unit,
    onSave: (SubcategoryAdminRow) -> Unit,
) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var categoryId by remember(initial) { mutableStateOf(initial.categoryId) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var active by remember(initial) { mutableStateOf(initial.active) }
    val activeCategories = categories.filter { it.active }
    val categoryName = activeCategories.firstOrNull { it.id == categoryId }?.name ?: "Seleccione categoria"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id == 0L) "Nueva subcategoria" else "Editar subcategoria") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Asocia la subcategoria a una categoria del catalogo.", color = Color(0xFF64748B))
                OutlinedTextField(
                    name,
                    { name = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                )
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { categoryExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, FieldBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FieldTextColor),
                    ) { Text(categoryName, modifier = Modifier.weight(1f)) }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                        DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                            activeCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = { categoryId = category.id; categoryExpanded = false },
                                )
                            }
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Estado", fontWeight = FontWeight.Medium)
                        Text(if (active) "Subcategoria activa" else "Subcategoria inactiva", color = Color(0xFF64748B))
                    }
                    Switch(checked = active, onCheckedChange = { active = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(initial.copy(categoryId = categoryId, name = name.trim(), active = active)) },
                enabled = name.isNotBlank() && categoryId > 0L,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD0505), contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
            ) { Text(if (initial.id == 0L) "Crear subcategoria" else "Guardar cambios") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) { Text("Cancelar") } },
        containerColor = Color.White,
        tonalElevation = 0.dp,
    )
}
