package com.ecommerce.ecommerceposapp.presentation.common

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
import com.ecommerce.ecommerceposapp.domain.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.ClientRow
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.UserRow
import com.ecommerce.ecommerceposapp.domain.UserSession
import java.io.File

internal fun parseLong(s: String, default: Long = 0L): Long = s.trim().toLongOrNull() ?: default
internal fun parseDouble(s: String, default: Double = 0.0): Double = s.trim().replace(',', '.').toDoubleOrNull() ?: default
internal val FieldBorderColor = Color(0xFF8F8588)
internal val FieldTextColor = Color(0xFF4A4043)

internal fun copyPickedProductImage(context: Context, uri: Uri): String {
    val dir = File(context.filesDir, "picked_product_images").apply { mkdirs() }
    val ext = when (context.contentResolver.getType(uri)?.lowercase()) {
        "image/png" -> "png"
        "image/webp" -> "webp"
        "image/gif" -> "gif"
        else -> "jpg"
    }
    val target = File(dir, "picked_${System.currentTimeMillis()}.$ext")
    context.contentResolver.openInputStream(uri)?.use { input ->
        target.outputStream().use { output -> input.copyTo(output) }
    }
    return "file://${target.absolutePath}"
}

internal data class PendingConfirm(
    val title: String,
    val body: String,
    val confirmButtonText: String,
    val onConfirm: () -> Unit,
)

@Composable
internal fun ConfirmDestructiveDialog(pending: PendingConfirm?, onDismiss: () -> Unit) {
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
internal fun RowScope.CrudEditDeleteIcons(
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
internal fun RowScope.CrudEditDeactivateIcons(
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
internal fun ToolbarAddIconButton(onClick: () -> Unit, contentDescription: String) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = contentDescription)
    }
}
