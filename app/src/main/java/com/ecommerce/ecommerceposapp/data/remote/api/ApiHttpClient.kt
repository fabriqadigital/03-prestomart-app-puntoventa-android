package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ApiHttpClient(context: Context) {
    private val sessionStore = ApiSessionStore(context)

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionStore))
        .addInterceptor(HostHeaderInterceptor(sessionStore))
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
}
