package com.ecommerce.ecommerceposapp.data.repository.common

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject

class RealmDataSource(context: Context) {
    fun <T> query(block: (Realm) -> T): T = Realm.getDefaultInstance().use(block)

    fun write(block: (Realm) -> Unit) {
        Realm.getDefaultInstance().use { realm -> realm.executeTransaction { block(it) } }
    }

    fun nextId(realm: Realm, clazz: Class<out RealmObject>): Long {
        val max = realm.where(clazz).max("id") as Number?
        return (max?.toLong() ?: 0L) + 1L
    }

}
