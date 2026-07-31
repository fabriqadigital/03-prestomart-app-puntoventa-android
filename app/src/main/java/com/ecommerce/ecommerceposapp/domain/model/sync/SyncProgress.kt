package com.ecommerce.ecommerceposapp.domain.model.sync

data class SyncProgress(
    val activeModuleKey: String,
    val completedModules: Int,
    val totalModules: Int,
    val completedItems: Int = 0,
    val totalItems: Int = 0,
) {
    val fraction: Float
        get() {
            if (totalModules <= 0) return 0f
            val itemProgress = if (totalItems > 0) completedItems.toFloat() / totalItems else 0f
            return (completedModules + itemProgress) / totalModules
        }
}
