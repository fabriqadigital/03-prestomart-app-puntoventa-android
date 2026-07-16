package com.ecommerce.ecommerceposapp.data.local.categories

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SubcategoryRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var categoryId: Long = 0
    var name: String = ""
    var active: Boolean = true
}
