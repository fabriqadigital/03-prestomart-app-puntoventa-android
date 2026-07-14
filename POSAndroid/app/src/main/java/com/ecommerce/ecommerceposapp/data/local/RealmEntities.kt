package com.ecommerce.ecommerceposapp.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var role: String = "admin"
    var active: Boolean = true
}

open class CategoryRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var active: Boolean = true
}

open class ProductRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var categoryId: Long = 0
    var name: String = ""
    var codigo: String = ""
    var imageUrl: String = ""
    var price: Double = 0.0
    var stock: Double = 0.0
    var active: Boolean = true
}

open class ClientRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var document: String = ""
    var phone: String = ""
    var active: Boolean = true
}

open class SupplierRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var businessName: String = ""
    var ruc: String = ""
    var phone: String = ""
    var active: Boolean = true
}

open class SyncStateRealm : RealmObject() {
    @PrimaryKey
    var id: Long = 1
    var syncedUserId: Long = 0
    var initialSyncDone: Boolean = false
}

open class SyncModuleStateRealm : RealmObject() {
    @PrimaryKey
    var moduleKey: String = ""
    var lastSyncAt: Long = 0L
}
