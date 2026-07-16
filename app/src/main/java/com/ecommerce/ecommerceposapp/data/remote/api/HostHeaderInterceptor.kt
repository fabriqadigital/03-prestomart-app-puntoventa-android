package com.ecommerce.ecommerceposapp.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class HostHeaderInterceptor(
    private val sessionStore: ApiSessionStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlHost = request.url.host
        val savedHost = sessionStore.hostHeader
        val needsEmulatorHost = urlHost == "10.0.3.2" || urlHost == "10.0.2.2"
        val shouldAttachHost = savedHost.isNotBlank() || needsEmulatorHost
        if (!shouldAttachHost) return chain.proceed(request)

        val port = request.url.port.takeIf { it > 0 }
        val hostHeader = savedHost.ifBlank {
            if (port != null && port != 80 && port != 443) "prestomart.localhost:$port" else "prestomart.localhost"
        }
        return chain.proceed(request.newBuilder().header("Host", hostHeader).build())
    }
}
