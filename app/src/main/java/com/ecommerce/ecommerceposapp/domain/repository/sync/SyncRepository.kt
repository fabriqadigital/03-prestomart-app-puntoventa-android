package com.ecommerce.ecommerceposapp.domain.repository.sync

import com.ecommerce.ecommerceposapp.domain.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.UserSession

interface SyncRepository {
    fun hasInitialSync(userId: Long): Boolean
    fun syncInitialData(user: UserSession): Result<Unit>
    fun listSyncModuleStatus(): List<SyncModuleStatus>
    fun syncModules(user: UserSession, modules: Set<String>): Result<Unit>
}
