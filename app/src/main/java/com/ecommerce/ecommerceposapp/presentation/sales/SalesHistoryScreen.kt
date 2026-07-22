package com.ecommerce.ecommerceposapp.presentation.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.presentation.pos.VistaPreviaReciboDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

private fun formatVentaFecha(millis: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.US).format(Date(millis))

private fun mapPago(code: String): String = when (code) {
    "EFE" -> "EF"
    "TAR" -> "TJ"
    "YAP" -> "YP"
    "PLN" -> "PL"
    else -> code
}

private fun mapTipoForReissue(tipoComprobante: String): TipoComprobanteEmision = when (tipoComprobante.uppercase()) {
    "01", "FACTURA" -> TipoComprobanteEmision.FACTURA
    "03", "BOLETA" -> TipoComprobanteEmision.BOLETA
    else -> TipoComprobanteEmision.SOLO_TICKET
}

@Composable
fun SalesHistoryScreen(
    catalog: CatalogRepository,
    clients: List<ClientRow>,
) {
    var rows by remember { mutableStateOf<List<SalesHistoryRow>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var previewReceipt by remember { mutableStateOf<CompletedSaleReceipt?>(null) }
    var previewComp by remember { mutableStateOf<ComprobanteEmitidoResult?>(null) }
    var saleToCancel by remember { mutableStateOf<SalesHistoryRow?>(null) }
    var cancelComment by remember { mutableStateOf("") }
    var restoreStock by remember { mutableStateOf(true) }
    var cancelling by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun reload() {
        scope.launch {
            loading = true
            error = null
            runCatching {
                withContext(Dispatchers.IO) { catalog.listSalesHistory() }
            }.onSuccess { rows = it }
                .onFailure { error = it.message ?: "No se pudo cargar historial." }
            loading = false
        }
    }

    LaunchedEffect(Unit) { reload() }

    val filtered = rows.filter {
        search.isBlank() ||
            it.numeroComprobante.contains(search, ignoreCase = true) ||
            it.clienteNombre.contains(search, ignoreCase = true) ||
            it.cajeroNombre.contains(search, ignoreCase = true)
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Text("Historial de Ventas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            "Consulta y vuelve a imprimir comprobantes",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.padding(vertical = 6.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar por comprobante, cliente o cajero") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.padding(vertical = 6.dp))
        if (loading) Text("Cargando ventas...")
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.ventaId }) { row ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(row.numeroComprobante, fontWeight = FontWeight.SemiBold)
                            Text(
                                "${formatVentaFecha(row.fechaMillis)} · ${row.clienteNombre} · ${row.cajeroNombre}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        Text(mapPago(row.tipoPago), modifier = Modifier.padding(end = 12.dp))
                        Text("S/ ${"%.2f".format(row.total)}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                        if (!row.estado.equals("Anulada", ignoreCase = true)) {
                            IconButton(onClick = {
                                saleToCancel = row
                                cancelComment = ""
                                restoreStock = true
                            }) {
                                Icon(Icons.Filled.Cancel, contentDescription = "Anular venta")
                            }
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    loading = true
                                    error = null
                                    val rec = withContext(Dispatchers.IO) { catalog.getSaleReceipt(row.ventaId) }
                                    val tipo = mapTipoForReissue(row.tipoComprobante)
                                    val em = withContext(Dispatchers.IO) {
                                        val receipt = rec.getOrNull()
                                        catalog.emitComprobanteForVenta(
                                            row.ventaId,
                                            tipo,
                                            row.idCliente,
                                            ReceiptCustomerInfo(
                                                id = row.idCliente,
                                                name = receipt?.clienteNombre.orEmpty(),
                                                document = receipt?.clienteDocumento.orEmpty(),
                                            ),
                                        )
                                    }
                                    loading = false
                                    if (rec.isSuccess && em.isSuccess) {
                                        previewReceipt = rec.getOrNull()
                                        previewComp = em.getOrNull()
                                    } else {
                                        error = rec.exceptionOrNull()?.message ?: em.exceptionOrNull()?.message ?: "No se pudo reemitir."
                                    }
                                }
                            },
                        ) {
                            Icon(Icons.Filled.Print, contentDescription = "Reimprimir")
                        }
                    }
                }
            }
        }
    }

    if (previewReceipt != null && previewComp != null) {
        val receipt = previewReceipt!!
        val comp = previewComp!!
        val cdisp = if (receipt.clienteNombre.isNotBlank() || receipt.clienteDocumento.isNotBlank()) {
            receipt.clienteNombre to receipt.clienteDocumento
        } else if (receipt.idCliente > 0L) {
            catalog.getClienteDisplay(receipt.idCliente)
        } else {
            null
        }
        VistaPreviaReciboDialog(
            receipt = receipt,
            emitido = comp,
            clienteNombre = cdisp?.first,
            clienteDoc = cdisp?.second,
            whatsappPhone = if (receipt.idCliente > 0L) catalog.getClienteTelefono(receipt.idCliente).orEmpty() else "",
            onDismiss = {
                previewReceipt = null
                previewComp = null
            },
        )
    }

    saleToCancel?.let { row ->
        AlertDialog(
            onDismissRequest = { if (!cancelling) saleToCancel = null },
            title = { Text("Anular venta ${row.numeroComprobante}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Indica el motivo. Esta accion se registra en el backend.")
                    OutlinedTextField(
                        value = cancelComment,
                        onValueChange = { cancelComment = it.take(1000) },
                        label = { Text("Motivo de anulacion") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = restoreStock, onCheckedChange = { restoreStock = it })
                        Text("Devolver productos al stock")
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = !cancelling && cancelComment.trim().length >= 5,
                    onClick = {
                        scope.launch {
                            cancelling = true
                            error = null
                            withContext(Dispatchers.IO) {
                                catalog.cancelSale(row.ventaId, cancelComment, restoreStock)
                            }.onSuccess {
                                saleToCancel = null
                                reload()
                            }.onFailure {
                                error = it.message ?: "No se pudo anular la venta."
                            }
                            cancelling = false
                        }
                    },
                ) { Text(if (cancelling) "Anulando..." else "Anular venta") }
            },
            dismissButton = {
                TextButton(enabled = !cancelling, onClick = { saleToCancel = null }) { Text("Cancelar") }
            },
        )
    }
}

