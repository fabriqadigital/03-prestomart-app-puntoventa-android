package com.ecommerce.ecommerceposapp.domain.model.sync

data class SyncProgress(
    val activeModuleKey: String,
    val completedModules: Int,
    val totalModules: Int,
) {
    val fraction: Float
        get() = if (totalModules <= 0) 0f else completedModules.toFloat() / totalModules
}
