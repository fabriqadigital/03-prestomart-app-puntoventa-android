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
    var userId: Long = 0
    var personType: String = "Natural"
    var documentType: String = "DNI"
    var alias: String = ""
    var gender: String = ""
    var maritalStatus: String = ""
    var discountPercentage: Double = 0.0
    var observations: String = ""
    var webAccess: Boolean = false
    var active: Boolean = true
}
