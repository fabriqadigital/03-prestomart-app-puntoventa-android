package com.ecommerce.ecommerceposapp.data.local.sync

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SyncStateRealm : RealmObject() {
    @PrimaryKey var id: Long = 1
    var syncedUserId: Long = 0
    var initialSyncDone: Boolean = false
}

open class SyncModuleStateRealm : RealmObject() {
    @PrimaryKey var moduleKey: String = ""
    var lastSyncAt: Long = 0L
}
