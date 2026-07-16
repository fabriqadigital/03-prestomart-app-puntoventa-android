package com.ecommerce.ecommerceposapp.data.local.categories

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CategoryRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var name: String = ""
    var active: Boolean = true
}
