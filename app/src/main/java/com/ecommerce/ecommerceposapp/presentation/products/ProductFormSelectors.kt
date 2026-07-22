package com.ecommerce.ecommerceposapp.presentation.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

internal data class ProductSelectOption(val id: Long, val label: String)

@Composable
internal fun ProductSelectField(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<ProductSelectOption>,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    clearLabel: String? = null,
    enabled: Boolean = true,
) {
    Column(modifier) {
        Text(
            text = label + if (required) " *" else "",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF475569),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Box(Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 54.dp)
                    .clickable(enabled = enabled) { onExpandedChange(true) },
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = BorderStroke(1.dp, if (expanded) Color(0xFFFD0505) else Color(0xFFCBD5E1)),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        value,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (enabled) Color(0xFF111827) else Color(0xFF94A3B8),
                    )
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Abrir $label", tint = Color(0xFF64748B))
                }
            }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.widthIn(min = 240.dp, max = 420.dp).heightIn(max = 280.dp),
                ) {
                    clearLabel?.let { text ->
                        DropdownMenuItem(text = { Text(text) }, onClick = { onSelect(0L); onExpandedChange(false) })
                    }
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                            onClick = { onSelect(option.id); onExpandedChange(false) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ProductMultiSelectField(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<ProductSelectOption>,
    selectedIds: Set<Long>,
    onToggle: (Long) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF475569),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Box(Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp)
                    .clickable(enabled = enabled) { onExpandedChange(true) },
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = BorderStroke(1.dp, if (expanded) Color(0xFFFD0505) else Color(0xFFCBD5E1)),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(value, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Abrir $label", tint = Color(0xFF64748B))
                }
            }
            MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.widthIn(min = 240.dp, max = 420.dp).heightIn(max = 280.dp),
                ) {
                    DropdownMenuItem(text = { Text("Sin subcategoría") }, onClick = { onClear(); onExpandedChange(false) })
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label, modifier = Modifier.weight(1f)) },
                            trailingIcon = {
                                if (option.id in selectedIds) Icon(Icons.Filled.Check, contentDescription = "Seleccionado", tint = Color(0xFFFD0505))
                            },
                            onClick = { onToggle(option.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ProductCheckboxField(
    checked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable { onCheckedChange(!checked) }.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFFD0505),
                checkmarkColor = Color.White,
                uncheckedColor = Color(0xFF94A3B8),
            ),
        )
        Text(label, color = Color(0xFF111827), modifier = Modifier.padding(start = 6.dp))
    }
}
