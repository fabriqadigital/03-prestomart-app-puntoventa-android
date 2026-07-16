package com.ecommerce.ecommerceposapp.data.local.suppliers

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SupplierRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var businessName: String = ""
    var ruc: String = ""
    var phone: String = ""
    var active: Boolean = true
}
