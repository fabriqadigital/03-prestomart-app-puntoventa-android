package com.ecommerce.ecommerceposapp.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkSyncCoordinator(
    context: Context,
    private val catalog: CatalogRepository,
    private val sync: SyncRepository,
) {
    private val connectivity = context.getSystemService(ConnectivityManager::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val syncing = AtomicBoolean(false)

    fun start() {
        connectivity.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (hasValidatedInternet(network)) synchronize()
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                if (
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) synchronize()
            }
        })
        connectivity.activeNetwork?.takeIf(::hasValidatedInternet)?.let { synchronize() }
    }

    private fun hasValidatedInternet(network: Network): Boolean =
        connectivity.getNetworkCapabilities(network)?.let {
            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } == true

    private fun synchronize() {
        if (!syncing.compareAndSet(false, true)) return
        scope.launch {
            try {
                // Permite que Android complete el arranque antes de abrir o migrar Realm.
                delay(6_000)
                val catalogResult = catalog.refreshCatalog()
                if (catalogResult.isSuccess) {
                    val outboxResult = sync.processOutbox()
                    if (outboxResult.isSuccess) {
                        // Releer stock después de aplicar las ventas pendientes en el servidor.
                        catalog.refreshCatalog()
                    }
                }
            } finally {
                syncing.set(false)
            }
        }
    }
}
