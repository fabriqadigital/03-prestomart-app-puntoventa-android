package com.ecommerce.ecommerceposapp.presentation.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.presentation.common.parseDouble

private val ProductBrand = Color(0xFFFD0505)
private val ProductText = Color(0xFF111827)
private val ProductBorder = Color(0xFFD7DCE3)

@Composable
fun ProductEditDialog(
    initial: ProductAdminRow,
    categories: List<CategoryAdminRow>,
    subcategories: List<SubcategoryAdminRow>,
    onDismiss: () -> Unit,
    onSave: (ProductAdminRow) -> Unit,
    quickMode: Boolean = true,
    initialAdvanced: Boolean = false,
    onAdvancedRequest: (() -> Unit)? = null,
) {
    var name by remember(initial) { mutableStateOf(initial.name) }
    var code by remember(initial) { mutableStateOf(initial.code) }
    var categoryId by remember(initial) { mutableStateOf(initial.categoryId) }
    var subcategoryId by remember(initial) { mutableStateOf(initial.subcategoryId) }
    var price by remember(initial) { mutableStateOf(initial.price.toString()) }
    var costPrice by remember(initial) { mutableStateOf(initial.costPrice.toString()) }
    var stock by remember(initial) { mutableStateOf(initial.stock.toString()) }
    var salesChannel by remember(initial) { mutableStateOf(initial.salesChannel.ifBlank { "ambos" }) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var subcategoryExpanded by remember { mutableStateOf(false) }

    val activeCategories = categories.filter { it.active }
    val availableSubcategories = subcategories.filter { it.active && it.categoryId == categoryId }
    val categoryName = activeCategories.firstOrNull { it.id == categoryId }?.name ?: "Seleccionar categoria"
    val subcategoryName = availableSubcategories.firstOrNull { it.id == subcategoryId }?.name ?: "Sin subcategoria"
    val canSave = name.isNotBlank() && categoryId > 0L && parseDouble(price, 0.0) >= 0.0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(16.dp).widthIn(max = 820.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            contentColor = ProductText,
            shadowElevation = 12.dp,
        ) {
            Column(Modifier.background(Color.White).padding(22.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Crear nuevo producto", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                }
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Canal de venta", fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ChannelChip("ambos", "Ambos", salesChannel) { salesChannel = it }
                        ChannelChip("fisica", "Venta fisica", salesChannel) { salesChannel = it }
                        ChannelChip("ecommerce", "Ecommerce", salesChannel) { salesChannel = it }
                    }
                    Text(
                        "Venta fisica se muestra solo en este punto de venta; Ambos tambien publica el producto en ecommerce.",
                        color = Color(0xFF64748B),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    ResponsiveFieldRow {
                        OutlinedTextField(name, { name = it }, label = { Text("Nombre *") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(code, { code = it }, label = { Text("Codigo de producto") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                    ResponsiveFieldRow {
                        SelectionField(categoryName, Modifier.weight(1f), { categoryExpanded = true }) {
                            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                                DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                                    activeCategories.forEach { category ->
                                        DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                            categoryId = category.id
                                            subcategoryId = 0L
                                            categoryExpanded = false
                                        })
                                    }
                                }
                            }
                        }
                        SelectionField(subcategoryName, Modifier.weight(1f), { subcategoryExpanded = availableSubcategories.isNotEmpty() }) {
                            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                                DropdownMenu(expanded = subcategoryExpanded, onDismissRequest = { subcategoryExpanded = false }) {
                                    DropdownMenuItem(text = { Text("Sin subcategoria") }, onClick = {
                                        subcategoryId = 0L
                                        subcategoryExpanded = false
                                    })
                                    availableSubcategories.forEach { subcategory ->
                                        DropdownMenuItem(text = { Text(subcategory.name) }, onClick = {
                                            subcategoryId = subcategory.id
                                            subcategoryExpanded = false
                                        })
                                    }
                                }
                            }
                        }
                    }
                    ResponsiveFieldRow {
                        OutlinedTextField(stock, { stock = it }, label = { Text("Cantidad *") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(costPrice, { costPrice = it }, label = { Text("Costo inicial por unidad") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(price, { price = it }, label = { Text("Precio de venta *") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { onAdvancedRequest?.invoke() },
                        enabled = onAdvancedRequest != null,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ProductBrand),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ProductBrand),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.size(8.dp))
                        Text("Ir al formulario avanzado")
                    }
                    Button(
                        onClick = {
                            onSave(
                                initial.copy(
                                    categoryId = categoryId,
                                    subcategoryId = subcategoryId,
                                    name = name.trim(),
                                    code = code.trim(),
                                    price = parseDouble(price, initial.price),
                                    costPrice = parseDouble(costPrice, initial.costPrice),
                                    stock = parseDouble(stock, initial.stock),
                                    salesChannel = salesChannel,
                                ),
                            )
                        },
                        enabled = canSave,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ProductBrand, contentColor = Color.White),
                    ) { Text("Crear producto") }
                }
            }
        }
    }
}

@Composable
private fun RowScope.ChannelChip(value: String, label: String, selected: String, onSelect: (String) -> Unit) {
    FilterChip(
        selected = selected == value,
        onClick = { onSelect(value) },
        label = { Text(label) },
        modifier = Modifier.weight(1f),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFF3F4F6),
            selectedLabelColor = ProductBrand,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected == value,
            borderColor = ProductBorder,
            selectedBorderColor = ProductBrand,
        ),
    )
}

@Composable
private fun ResponsiveFieldRow(content: @Composable FlowRowScope.() -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = if (maxWidth >= 620.dp) 3 else 1,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content,
        )
    }
}

@Composable
private fun SelectionField(label: String, modifier: Modifier, onClick: () -> Unit, menu: @Composable () -> Unit) {
    Box(modifier) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, ProductBorder),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ProductText),
        ) { Text(label, modifier = Modifier.weight(1f)) }
        menu()
    }
}
