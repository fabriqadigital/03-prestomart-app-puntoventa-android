package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.presentation.pos.PosUiState
import com.ecommerce.ecommerceposapp.presentation.pos.PosViewModel
import java.util.Locale
import kotlinx.coroutines.launch

private val CashBrand = Color(0xFFFD0505)
private val CashText = com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
private val CashMuted = com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
private val CashBorder = com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
private val CashWhite = com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite

@Composable
fun CashSessionIndicator(state: PosUiState, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val current = state.cashSession ?: return
    /*Row(modifier = modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Caja: ${current.cashRegisterName}", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
        OutlinedButton(onClick = onClose) { Text("Cerrar caja") }
    }*/
}

@Composable
fun CashFlowHost(
    session: UserSession,
    state: PosUiState,
    viewModel: PosViewModel,
    onLoginRequested: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var selectedId by remember(state.cashRegisters, session.defaultCashRegisterId) {
        mutableStateOf(session.defaultCashRegisterId.takeIf { id -> state.cashRegisters.any { it.id == id } }
            ?: state.cashRegisters.firstOrNull()?.id ?: 0L)
    }
    var amount by remember { mutableStateOf("0") }
    var expanded by remember { mutableStateOf(false) }
    var showClose by remember { mutableStateOf(false) }
    var counted by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
    val authenticationRequired = state.cashError.isAuthenticationError()

    if (state.cashSession == null) {
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.fillMaxWidth(.92f).widthIn(max = 620.dp),
            containerColor = CashWhite,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(22.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = CashWhite,
                        border = BorderStroke(1.dp, CashBrand.copy(alpha = .24f)),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Storefront, contentDescription = null, tint = CashBrand)
                        }
                    }
                    Column {
                        Text("Abrir caja", fontWeight = FontWeight.Bold, color = CashText)
                        Text("Inicia tu turno de venta", style = androidx.compose.material3.MaterialTheme.typography.bodySmall, color = CashMuted)
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Caja disponible", color = CashText, fontWeight = FontWeight.SemiBold)
                    Box(Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, CashBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CashWhite,
                                contentColor = CashText,
                                disabledContainerColor = CashWhite,
                                disabledContentColor = CashMuted,
                            ),
                        ) {
                            Text(
                                state.cashRegisters.firstOrNull { it.id == selectedId }?.name ?: "Seleccionar caja",
                                modifier = Modifier.weight(1f),
                                color = if (selectedId > 0L) CashText else CashBrand,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            state.cashRegisters.forEach { cash ->
                                DropdownMenuItem(text = { Text(cash.name) }, onClick = { selectedId = cash.id; expanded = false })
                            }
                        }
                    }
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                        label = { Text("Efectivo inicial") },
                        prefix = { Text("S/ ") },
                        supportingText = { Text("Dinero contado al comenzar el turno", color = CashMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = cashTextFieldColors(),
                    )
                    state.cashError?.let { error ->
                        Text(
                            if (authenticationRequired) {
                                "Tu sesión venció o ya no es válida. Inicia sesión nuevamente para continuar."
                            } else {
                                error
                            },
                            color = CashBrand,
                        )
                    }
                    if (state.cashLoading) CircularProgressIndicator(color = CashBrand)
                }
            },
            dismissButton = {
                if (authenticationRequired) {
                    OutlinedButton(
                        onClick = onLoginRequested,
                        border = BorderStroke(1.dp, CashBrand),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = CashWhite,
                            contentColor = CashBrand,
                        ),
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text("Iniciar sesión")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { scope.launch { viewModel.openCashSession(selectedId, session.cashierId, amount.toDoubleOrNull() ?: 0.0) } },
                    enabled = selectedId > 0L && !state.cashLoading && !authenticationRequired,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CashBrand,
                        contentColor = Color.White,
                        disabledContainerColor = CashBrand.copy(alpha = .34f),
                        disabledContentColor = Color.White.copy(alpha = .72f),
                    ),
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                ) { Text("Abrir caja") }
            },
        )
    }

    CashCloseDialogTrigger(show = showClose, onShowChange = { showClose = it })
    if (showClose && state.cashSession != null) {
        AlertDialog(
            onDismissRequest = { showClose = false },
            modifier = Modifier.fillMaxWidth(.92f).widthIn(max = 620.dp),
            containerColor = CashWhite,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(22.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = CashWhite,
                        border = BorderStroke(1.dp, CashBrand.copy(alpha = .24f)),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = CashBrand)
                        }
                    }
                    Column { Text("Cerrar caja", fontWeight = FontWeight.Bold, color = CashText); Text("Confirma el efectivo de tu turno", style = androidx.compose.material3.MaterialTheme.typography.bodySmall, color = CashMuted) }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(color = CashWhite, border = BorderStroke(1.dp, CashBorder), shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Ventas", color = CashMuted); Text("S/ %.2f".format(Locale.US, state.cashSummary?.totalSales ?: 0.0), color = CashText, fontWeight = FontWeight.Bold) }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Efectivo esperado", color = CashMuted); Text("S/ %.2f".format(Locale.US, state.cashSummary?.expectedCash ?: 0.0), color = CashText, fontWeight = FontWeight.Bold) }
                        }
                    }
                    OutlinedTextField(counted, { counted = it.filter { char -> char.isDigit() || char == '.' } }, label = { Text("Efectivo contado") }, prefix = { Text("S/ ") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(10.dp), colors = cashTextFieldColors())
                    OutlinedTextField(observations, { observations = it }, label = { Text("Observaciones (opcional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), minLines = 2, colors = cashTextFieldColors())
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClose = false },
                    border = BorderStroke(1.dp, CashBorder),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = CashWhite, contentColor = CashText),
                ) { Text("Cancelar") }
            },
            confirmButton = {
                Button(
                    onClick = { scope.launch { viewModel.closeCashSession(counted.toDoubleOrNull() ?: 0.0, observations).onSuccess { showClose = false } } },
                    colors = ButtonDefaults.buttonColors(containerColor = CashBrand, contentColor = Color.White),
                ) { Text("Cerrar caja") }
            },
        )
    }
}

internal fun String?.isAuthenticationError(): Boolean {
    val normalized = this?.lowercase(Locale.ROOT).orEmpty()
    return listOf(
        "no autentic",
        "inicia ses",
        "inicie ses",
        "sesión venc",
        "sesion venc",
        "token",
        "unauthorized",
        "forbidden",
        "401",
        "403",
    ).any(normalized::contains)
}

@Composable
private fun cashTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CashBrand,
    unfocusedBorderColor = CashBorder,
    focusedLabelColor = CashBrand,
    unfocusedLabelColor = CashMuted,
    cursorColor = CashBrand,
    focusedContainerColor = CashWhite,
    unfocusedContainerColor = CashWhite,
    disabledContainerColor = CashWhite,
)

// Keeps the close-dialog state owned by this host while allowing AppRoot to request it.
private var closeCashRequest: (() -> Unit)? = null

@Composable
private fun CashCloseDialogTrigger(show: Boolean, onShowChange: (Boolean) -> Unit) {
    closeCashRequest = { onShowChange(true) }
}

fun requestCashClose() {
    closeCashRequest?.invoke()
}
