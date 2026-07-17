package com.ecommerce.ecommerceposapp.domain.model.suppliers

data class SupplierRow(
    val id: Long,
    val codigoProveedor: String = "",
    val businessName: String,
    val ruc: String,
    val correo: String = "",
    val phone: String,
    val direccion: String = "",
    val personaContacto: String = "",
    val cargoContacto: String = "",
    val telefonoContacto: String = "",
    val correoContacto: String = "",
    val calificacion: Int = 0,
    val estado: String = "Activo", // Activo | Inactivo | Bloqueado
    val fechaRegistro: String = "",
    val observaciones: String = "",
    val banco: String = "",
    val cuenta: String = "",
    val cci: String = "",
    val active: Boolean = true,
)