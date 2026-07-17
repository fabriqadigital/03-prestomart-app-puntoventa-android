package com.ecommerce.ecommerceposapp.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkSyncCoordinator(
    context: Context,
    private val products: ProductRepository,
    private val catalog: CatalogRepository,
) {
    private val connectivity = context.getSystemService(ConnectivityManager::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val syncing = AtomicBoolean(false)

    fun start() {
        connectivity.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) synchronize()
            }
        })
    }

    private fun synchronize() {
        if (!syncing.compareAndSet(false, true)) return
        scope.launch {
            try {
                // Permite que Android complete el arranque antes de abrir o migrar Realm.
                delay(6_000)
                val productsResult = products.syncPendingProducts()
                if (productsResult.isSuccess) catalog.refreshCatalog()
            } finally {
                syncing.set(false)
            }
        }
    }
}
