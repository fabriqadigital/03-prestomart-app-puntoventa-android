package com.ecommerce.ecommerceposapp.domain.model.users

data class UserRow(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val active: Boolean,
    /** Campo `name` de la BD — equivale a username en la respuesta JSON */
    val username: String = "",
    /** Fecha de creación en millis (parseada de created_at "yyyy-MM-dd HH:mm:ss") */
    val createdAt: Long = 0L,
    /** 1 = bloqueado, 0 = activo */
    val isBlocked: Int = 0,
)
