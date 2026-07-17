package com.ecommerce.ecommerceposapp.data.local.clients

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ClientRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var name: String = ""
    var document: String = ""
    var phone: String = ""
    var lastName: String = ""
    var email: String = ""
    var address: String = ""
    var businessName: String = ""
    var branchName: String = ""
    var active: Boolean = true
}
