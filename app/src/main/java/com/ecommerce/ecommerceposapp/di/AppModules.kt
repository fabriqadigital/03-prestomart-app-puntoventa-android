package com.ecommerce.ecommerceposapp.di

import com.ecommerce.ecommerceposapp.data.repository.AuthRepository
import com.ecommerce.ecommerceposapp.data.repository.CatalogRepository
import com.ecommerce.ecommerceposapp.data.repository.MaestroRepository
import com.ecommerce.ecommerceposapp.data.repository.PosRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.SyncRepository
import com.ecommerce.ecommerceposapp.presentation.LoginViewModel
import com.ecommerce.ecommerceposapp.presentation.MaestroViewModel
import com.ecommerce.ecommerceposapp.presentation.PosViewModel
import com.ecommerce.ecommerceposapp.presentation.SyncViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { PosRepositoryImpl(androidContext()) }
    single<AuthRepository> { get<PosRepositoryImpl>() }
    single<CatalogRepository> { get<PosRepositoryImpl>() }
    single<SyncRepository> { get<PosRepositoryImpl>() }
    single<MaestroRepository> { get<PosRepositoryImpl>() }

    viewModel { LoginViewModel(get()) }
    viewModel { SyncViewModel(get()) }
    viewModel { PosViewModel(get()) }
    viewModel { MaestroViewModel(get()) }
}
