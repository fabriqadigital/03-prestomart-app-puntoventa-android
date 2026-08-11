package com.ecommerce.ecommerceposapp.domain.repository.catalog

import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.catalog.CategoryItem
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductItem
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryPage
import com.ecommerce.ecommerceposapp.domain.model.catalog.SubcategoryItem
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.cash.CashRegister
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary

interface CatalogRepository {
    fun categories(): List<CategoryItem>
    fun subcategories(): List<SubcategoryItem>
    fun products(): List<ProductItem>
    fun refreshCatalog(): Result<Unit>
    fun setProductFeatured(productId: Long, featured: Boolean)
    fun registerSale(
        lines: List<CartLine>,
        payment: SalePaymentInfo,
        idCliente: Long = 0L,
        customerInfo: ReceiptCustomerInfo = ReceiptCustomerInfo(),
        receiptType: TipoComprobanteEmision = TipoComprobanteEmision.SOLO_TICKET,
    ): Result<CompletedSaleReceipt>
    fun emitComprobanteForVenta(
        ventaId: Long,
        tipo: TipoComprobanteEmision,
        idCliente: Long = 0L,
        customerInfo: ReceiptCustomerInfo = ReceiptCustomerInfo(),
        allowOffline: Boolean = false,
    ): Result<ComprobanteEmitidoResult>
    fun getClienteDisplay(idCliente: Long): Pair<String, String>?
    fun getClienteTelefono(idCliente: Long): String?
    fun actualizarClienteEnVenta(ventaId: Long, idCliente: Long): Result<Unit>
    fun listSalesHistory(page: Int, perPage: Int, search: String = ""): SalesHistoryPage
    fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt>
    fun listCashRegisters(): Result<List<CashRegister>>
    fun findOpenCashSession(cashierId: Long): Result<CashSession?>
    fun openCashSession(cashRegisterId: Long, cashierId: Long, openingAmount: Double): Result<CashSession>
    fun cashSummary(sessionId: Long): Result<CashSummary>
    fun closeCashSession(sessionId: Long, countedCash: Double, observations: String): Result<Unit>
    fun cancelSale(ventaId: Long, comment: String, restoreStock: Boolean): Result<Unit>
    fun withdrawSaleCancellation(ventaId: Long): Result<Unit>
}
