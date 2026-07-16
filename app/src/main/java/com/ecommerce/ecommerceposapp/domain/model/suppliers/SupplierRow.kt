package com.ecommerce.ecommerceposapp.domain.model.suppliers

data class SupplierRow(
    val id: Long,
    val businessName: String,
    val ruc: String,
    val phone: String,
    val active: Boolean = true,
)
