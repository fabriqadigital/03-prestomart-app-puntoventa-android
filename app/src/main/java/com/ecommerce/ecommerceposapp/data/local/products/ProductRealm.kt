package com.ecommerce.ecommerceposapp.data.local.products

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ProductRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var categoryId: Long = 0
    var subcategoryId: Long = 0
    var name: String = ""
    var codigo: String = ""
    var imageUrl: String = ""
    var price: Double = 0.0
    var stock: Double = 0.0
    var active: Boolean = true
}
