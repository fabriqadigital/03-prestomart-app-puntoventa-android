package com.ecommerce.ecommerceposapp.data.local.users

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var role: String = "admin"
    var active: Boolean = true
}
