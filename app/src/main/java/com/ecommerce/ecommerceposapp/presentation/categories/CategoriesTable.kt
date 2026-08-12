package com.ecommerce.ecommerceposapp.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import kotlinx.coroutines.delay

private val TableText = Color(0xFF111827)
private val TableMuted = Color(0xFF64748B)
private val TableBorder = Color(0xFFE2E8F0)
private val TableSubcategory = Color(0xFFF8FAFC)
private val TableBrand = Color(0xFFFD0505)

@Composable
internal fun CategoriesTable(
    categories: List<CategoryAdminRow>,
    subcategories: List<SubcategoryAdminRow>,
    total: Int,
    page: Int,
    pageSize: Int,
    onQuery: (Int, Int, String) -> Unit,
    onEditCategory: (CategoryAdminRow) -> Unit,
    onDeleteCategory: (CategoryAdminRow) -> Unit,
    onCreateSubcategory: (CategoryAdminRow) -> Unit,
    onEditSubcategory: (SubcategoryAdminRow) -> Unit,
    onDeleteSubcategory: (SubcategoryAdminRow) -> Unit,
) {
    val compact = LocalConfiguration.current.screenWidthDp < 720
    var search by remember { mutableStateOf("") }
    var expandedIds by remember { mutableStateOf(setOf<Long>()) }
    val normalizedSearch = search.trim()
    val subcategoriesByCategory = remember(subcategories) { subcategories.groupBy { it.categoryId } }
    val totalPages = maxOf(1, (total + pageSize - 1) / pageSize)
    LaunchedEffect(search) {
        delay(350)
        onQuery(1, pageSize, search)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = TableText,
        shadowElevation = 1.dp,
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    placeholder = { Text("Buscar categoria o subcategoria") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().widthIn(max = 430.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
            }
            HorizontalDivider(color = TableBorder)
            CategoryHeader(compact)
            HorizontalDivider(color = TableBorder)
            if (categories.isEmpty()) {
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No se encontraron categorias.", color = TableMuted)
                }
            } else {
                LazyColumn(Modifier.fillMaxWidth().weight(1f).background(Color.White)) {
                    categories.forEach { category ->
                        val children = subcategoriesByCategory[category.id].orEmpty().sortedBy { it.name.lowercase() }
                        val searchShowsChildren = normalizedSearch.isNotEmpty() && children.any {
                            it.name.contains(normalizedSearch, ignoreCase = true)
                        }
                        val expanded = category.id in expandedIds || searchShowsChildren
                        item(key = "category-${category.id}") {
                            CategoryRow(
                                category = category,
                                childCount = children.size,
                                expanded = expanded,
                                compact = compact,
                                onToggle = {
                                    expandedIds = if (category.id in expandedIds) expandedIds - category.id else expandedIds + category.id
                                },
                                onEdit = { onEditCategory(category) },
                                onDelete = { onDeleteCategory(category) },
                                onCreateSubcategory = { onCreateSubcategory(category) },
                            )
                            HorizontalDivider(color = TableBorder)
                        }
                        if (expanded) {
                            if (children.isEmpty()) {
                                item(key = "empty-${category.id}") {
                                    Text(
                                        "Esta categoria no tiene subcategorias.",
                                        modifier = Modifier.fillMaxWidth().background(TableSubcategory).padding(start = 58.dp, top = 13.dp, bottom = 13.dp),
                                        color = TableMuted,
                                    )
                                    HorizontalDivider(color = TableBorder)
                                }
                            } else {
                                items(children, key = { "subcategory-${it.id}" }) { subcategory ->
                                    SubcategoryRow(
                                        subcategory = subcategory,
                                        compact = compact,
                                        onEdit = { onEditSubcategory(subcategory) },
                                        onDelete = { onDeleteSubcategory(subcategory) },
                                    )
                                    HorizontalDivider(color = TableBorder)
                                }
                            }
                        }
                    }
                }
            }
            CategoryPagination(
                compact = compact,
                page = page - 1,
                totalPages = totalPages,
                itemCount = total,
                pageSize = pageSize,
                onPageSize = {
                    onQuery(1, it, search)
                },
                onPrevious = { onQuery((page - 1).coerceAtLeast(1), pageSize, search) },
                onNext = { onQuery((page + 1).coerceAtMost(totalPages), pageSize, search) },
            )
        }
    }
}

@Composable
private fun CategoryHeader(compact: Boolean) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(40.dp))
        Text("Nombre", modifier = Modifier.weight(2f), fontWeight = FontWeight.SemiBold)
        if (!compact) Text("Subcategorias", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
        Text("Estado", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(48.dp))
    }
}

@Composable
private fun CategoryRow(
    category: CategoryAdminRow,
    childCount: Int,
    expanded: Boolean,
    compact: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCreateSubcategory: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(Color.White).clickable(onClick = onToggle).padding(horizontal = 8.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Ocultar subcategorias" else "Mostrar subcategorias",
            )
        }
        Text(category.name, modifier = Modifier.weight(2f), fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!compact) Text(childCount.toString(), modifier = Modifier.weight(1f), color = TableMuted)
        Text(if (category.active) "Activa" else "Inactiva", modifier = Modifier.weight(1f), color = if (category.active) TableText else TableBrand)
        Box {
            IconButton(onClick = { menuExpanded = true }) { Icon(Icons.Filled.MoreVert, contentDescription = "Opciones de categoria") }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Nueva subcategoria") },
                        onClick = { menuExpanded = false; onCreateSubcategory() },
                    )
                    DropdownMenuItem(text = { Text("Editar") }, onClick = { menuExpanded = false; onEdit() })
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = { menuExpanded = false; onDelete() },
                    )
                }
            }
        }
    }
}

@Composable
private fun SubcategoryRow(
    subcategory: SubcategoryAdminRow,
    compact: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(TableSubcategory).clickable(onClick = onEdit).padding(start = 58.dp, end = 8.dp, top = 9.dp, bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(subcategory.name, modifier = Modifier.weight(2f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (!compact) Text("Subcategoria", modifier = Modifier.weight(1f), color = TableMuted)
        Text(if (subcategory.active) "Activa" else "Inactiva", modifier = Modifier.weight(1f), color = TableMuted)
        Box {
            IconButton(onClick = { menuExpanded = true }) { Icon(Icons.Filled.MoreVert, contentDescription = "Opciones de subcategoria") }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Editar") }, onClick = { menuExpanded = false; onEdit() })
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = { menuExpanded = false; onDelete() },
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryPagination(
    compact: Boolean,
    page: Int,
    totalPages: Int,
    itemCount: Int,
    pageSize: Int,
    onPageSize: (Int) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    var sizeMenuExpanded by remember { mutableStateOf(false) }
    val from = if (itemCount == 0) 0 else page * pageSize + 1
    val to = minOf(itemCount, (page + 1) * pageSize)
    val pageSizeSelector: @Composable () -> Unit = {
        Text(if (compact) "Filas:" else "Registros por pagina:", color = TableMuted)
        Box {
            TextButton(onClick = { sizeMenuExpanded = true }) { Text("$pageSize", color = TableText) }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(expanded = sizeMenuExpanded, onDismissRequest = { sizeMenuExpanded = false }) {
                    listOf(20, 50, 100).forEach { size ->
                        DropdownMenuItem(
                            text = { Text(size.toString()) },
                            onClick = { sizeMenuExpanded = false; onPageSize(size) },
                        )
                    }
                }
            }
        }
        Text("$from-$to de $itemCount", color = TableMuted)
    }
    val navigation: @Composable () -> Unit = {
        IconButton(onClick = onPrevious, enabled = page > 0) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
        }
        IconButton(onClick = onNext, enabled = page < totalPages - 1) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
        }
    }
    if (compact) {
        Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 10.dp, vertical = 6.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { pageSizeSelector() }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text("Pagina ${page + 1} de $totalPages", color = TableMuted)
                navigation()
            }
        }
    } else {
        Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            pageSizeSelector()
            Spacer(Modifier.weight(1f))
            Text("Pagina ${page + 1} de $totalPages", color = TableMuted)
            navigation()
        }
    }
}
