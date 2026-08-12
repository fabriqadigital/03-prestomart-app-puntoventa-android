package com.ecommerce.ecommerceposapp.domain.model.common

data class ServerPage<T>(
    val rows: List<T>,
    val total: Int,
    val page: Int,
    val perPage: Int,
)
