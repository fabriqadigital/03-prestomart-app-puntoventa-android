package com.ecommerce.ecommerceposapp.data.local.suppliers

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SupplierRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var codigoProveedor: String = ""
    var businessName: String = ""
    var ruc: String = ""
    var correo: String = ""
    var phone: String = ""
    var direccion: String = ""
    var personaContacto: String = ""
    var cargoContacto: String = ""
    var telefonoContacto: String = ""
    var correoContacto: String = ""
    var calificacion: Int = 0
    var estado: String = "Activo"
    var fechaRegistro: String = ""
    var observaciones: String = ""
    var banco: String = ""
    var cuenta: String = ""
    var cci: String = ""
    var active: Boolean = true
}