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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
    var creatingSubcategory by remember { mutableStateOf(false) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Categorias", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ToolbarAddIconButton({ creatingCategory = true }, "Nueva categoria")
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.categories, key = { it.id }) { category ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(category.name, fontWeight = FontWeight.Medium)
                        Text(if (category.active) "Activa" else "Inactiva", style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeactivateIcons(
                        onEdit = { editingCategory = category },
                        onDeactivate = {
                            pendingConfirm = PendingConfirm(
                                title = "Desactivar categoria",
                                body = "Desactivar ${category.name}? Sus subcategorias y productos dejaran de estar disponibles.",
                                confirmButtonText = "Desactivar",
                                onConfirm = { vm.removeCategory(category.id) },
                            )
                        },
                    )
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Subcategorias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    ToolbarAddIconButton({ creatingSubcategory = true }, "Nueva subcategoria")
                }
            }
            items(state.subcategories, key = { it.id }) { subcategory ->
                val categoryName = state.categories.firstOrNull { it.id == subcategory.categoryId }?.name ?: "Categoria no disponible"
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(subcategory.name, fontWeight = FontWeight.Medium)
                        Text(categoryName, style = MaterialTheme.typography.bodySmall)
                    }
                    CrudEditDeactivateIcons(
                        onEdit = { editingSubcategory = subcategory },
                        onDeactivate = {
                            pendingConfirm = PendingConfirm(
                                title = "Desactivar subcategoria",
                                body = "Desactivar ${subcategory.name}? Sus productos dejaran de estar disponibles.",
                                confirmButtonText = "Desactivar",
                                onConfirm = { vm.removeSubcategory(subcategory.id) },
                            )
                        },
                    )
                }
            }
        }
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
    if (creatingSubcategory) {
        val firstCategoryId = state.categories.firstOrNull { it.active }?.id ?: 0L
        SubcategoryEditDialog(SubcategoryAdminRow(0, firstCategoryId, "", true), state.categories, { creatingSubcategory = false; vm.clearMessages() }) {
            vm.saveSubcategory(it)
            creatingSubcategory = false
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                FilterChip(active, { active = !active }, label = { Text("Activa") })
            }
        },
        confirmButton = {
            IconButton(onClick = { onSave(initial.copy(name = name, active = active)) }) {
                Icon(Icons.Filled.Check, contentDescription = "Guardar")
            }
        },
        dismissButton = { IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") } },
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { categoryExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, FieldBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FieldTextColor),
                    ) { Text(categoryName, modifier = Modifier.weight(1f)) }
                    DropdownMenu(categoryExpanded, { categoryExpanded = false }) {
                        activeCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = { categoryId = category.id; categoryExpanded = false },
                            )
                        }
                    }
                }
                FilterChip(active, { active = !active }, label = { Text("Activa") })
            }
        },
        confirmButton = {
            IconButton(onClick = { onSave(initial.copy(categoryId = categoryId, name = name, active = active)) }) {
                Icon(Icons.Filled.Check, contentDescription = "Guardar")
            }
        },
        dismissButton = { IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, contentDescription = "Cancelar") } },
    )
}
