package com.ecommerce.ecommerceposapp.domain.model.sync

data class SyncModuleStatus(
    val key: String,
    val label: String,
    val lastSyncAt: Long,
)
