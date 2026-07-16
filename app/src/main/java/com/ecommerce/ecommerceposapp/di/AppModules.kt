package com.ecommerce.ecommerceposapp.di

import com.ecommerce.ecommerceposapp.data.repository.PosRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.categories.CategoryRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.clients.ClientRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.products.ProductRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.suppliers.SupplierRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.users.UserRepositoryImpl
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.presentation.auth.LoginViewModel
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository
import com.ecommerce.ecommerceposapp.domain.usecase.categories.*
import com.ecommerce.ecommerceposapp.domain.usecase.clients.*
import com.ecommerce.ecommerceposapp.domain.usecase.products.*
import com.ecommerce.ecommerceposapp.domain.usecase.suppliers.*
import com.ecommerce.ecommerceposapp.domain.usecase.users.*
import com.ecommerce.ecommerceposapp.presentation.categories.CategoriesViewModel
import com.ecommerce.ecommerceposapp.presentation.clients.ClientsViewModel
import com.ecommerce.ecommerceposapp.presentation.products.ProductsViewModel
import com.ecommerce.ecommerceposapp.presentation.pos.PosViewModel
import com.ecommerce.ecommerceposapp.presentation.sync.SyncViewModel
import com.ecommerce.ecommerceposapp.presentation.suppliers.SuppliersViewModel
import com.ecommerce.ecommerceposapp.presentation.users.UsersViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { PosRepositoryImpl(androidContext()) }
    single<AuthRepository> { get<PosRepositoryImpl>() }
    single<CatalogRepository> { get<PosRepositoryImpl>() }
    single<SyncRepository> { get<PosRepositoryImpl>() }
    single<CategoryRepository> { CategoryRepositoryImpl(androidContext()) }
    single<ClientRepository> { ClientRepositoryImpl(androidContext()) }
    single<ProductRepository> { ProductRepositoryImpl(androidContext()) }
    single<SupplierRepository> { SupplierRepositoryImpl(androidContext()) }
    single<UserRepository> { UserRepositoryImpl(androidContext()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SyncViewModel(get()) }
    viewModel { PosViewModel(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { SaveCategoryUseCase(get()) }
    factory { DeactivateCategoryUseCase(get()) }
    factory { GetSubcategoriesUseCase(get()) }
    factory { SaveSubcategoryUseCase(get()) }
    factory { DeactivateSubcategoryUseCase(get()) }
    factory { GetClientsUseCase(get()) }
    factory { SaveClientUseCase(get()) }
    factory { DeleteClientUseCase(get()) }
    factory { GetProductsUseCase(get()) }
    factory { SaveProductUseCase(get()) }
    factory { DeactivateProductUseCase(get()) }
    factory { GetSuppliersUseCase(get()) }
    factory { SaveSupplierUseCase(get()) }
    factory { DeleteSupplierUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { SaveUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }

    viewModel { CategoriesViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ClientsViewModel(get(), get(), get()) }
    viewModel { ProductsViewModel(get(), get(), get(), get(), get()) }
    viewModel { SuppliersViewModel(get(), get(), get()) }
    viewModel { UsersViewModel(get(), get(), get()) }
}
