package com.ecommerce.ecommerceposapp.data.local.products

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ProductRealm : RealmObject() {
    @PrimaryKey var id: Long = 0
    var categoryId: Long = 0
    var subcategoryId: Long = 0
    var name: String = ""
    var codigo: String = ""
    var barcode: String = ""
    var slug: String = ""
    var description: String = ""
    var location: String = ""
    var canalVenta: String = "ambos"
    var imageUrl: String = ""
    var price: Double = 0.0
    var stock: Double = 0.0
    var oldPrice: Double = 0.0
    var costPrice: Double = 0.0
    var wholesalePrice: Double = 0.0
    var wholesaleOldPrice: Double = 0.0
    var yapePrice: Double = 0.0
    var minimumStock: Double = 0.0
    var productTypeId: Long = 2L
    var ratingsEnabled: Boolean = false
    var adminRating: Double = 0.0
    var packageMeasures: String = ""
    var packageDimension: String = ""
    var weightKg: Double = 0.0
    var promoCutoffTime: String = ""
    var saturdayCutoffTime: String = ""
    var offerMaxQuantity: Double = 0.0
    var offerMaxQuantityPrice: Double = 0.0
    var metaTitle: String = ""
    var metaDescription: String = ""
    var active: Boolean = true
    var localCreatedAt: Long = 0L
    var remoteCreatedAt: Long = 0L
    var remoteUpdatedAt: Long = 0L
    var syncState: String = "SYNCED"
    var syncError: String = ""
}
