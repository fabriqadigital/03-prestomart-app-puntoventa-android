package com.ecommerce.ecommerceposapp.domain.repository.catalog

import com.ecommerce.ecommerceposapp.domain.CartLine
import com.ecommerce.ecommerceposapp.domain.CategoryItem
import com.ecommerce.ecommerceposapp.domain.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.ProductItem
import com.ecommerce.ecommerceposapp.domain.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.SubcategoryItem
import com.ecommerce.ecommerceposapp.domain.TipoComprobanteEmision

interface CatalogRepository {
    fun categories(): List<CategoryItem>
    fun subcategories(): List<SubcategoryItem>
    fun products(): List<ProductItem>
    fun registerSale(lines: List<CartLine>, payment: SalePaymentInfo, idCliente: Long = 0L): Result<CompletedSaleReceipt>
    fun emitComprobanteForVenta(ventaId: Long, tipo: TipoComprobanteEmision, idCliente: Long = 0L): Result<ComprobanteEmitidoResult>
    fun getClienteDisplay(idCliente: Long): Pair<String, String>?
    fun getClienteTelefono(idCliente: Long): String?
    fun actualizarClienteEnVenta(ventaId: Long, idCliente: Long): Result<Unit>
    fun listSalesHistory(): List<SalesHistoryRow>
    fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt>
}
