package com.ecommerce.ecommerceposapp.domain.repository.sync

import com.ecommerce.ecommerceposapp.domain.model.sync.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncProgress
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession

interface SyncRepository {
    fun hasInitialSync(userId: Long): Boolean
    fun syncInitialData(
        user: UserSession,
        onProgress: (SyncProgress) -> Unit = {},
    ): Result<Unit>
    fun listSyncModuleStatus(): List<SyncModuleStatus>
    fun syncModules(
        user: UserSession,
        modules: Set<String>,
        onProgress: (SyncProgress) -> Unit = {},
    ): Result<Unit>
    fun processOutbox(): Result<Int>
}
