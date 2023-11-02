package com.hungry.binareats.di.koin

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.hungry.binareats.data.local.database.AppDatabase
import com.hungry.binareats.data.local.database.datasource.CartDataSource
import com.hungry.binareats.data.local.database.datasource.CartDatabaseDataSource
import com.hungry.binareats.data.local.datastore.appDataStore
import com.hungry.binareats.data.local.preferences.UserPreferenceDataSource
import com.hungry.binareats.data.local.preferences.UserPreferenceDataSourceImpl
import com.hungry.binareats.data.network.api.datasource.BinarEatsApiDataSource
import com.hungry.binareats.data.network.api.datasource.BinarEatsDataSource
import com.hungry.binareats.data.network.api.service.BinarEatsApiService
import com.hungry.binareats.data.network.firebase.auth.FirebaseAuthDataSource
import com.hungry.binareats.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.hungry.binareats.data.repository.CartRepository
import com.hungry.binareats.data.repository.CartRepositoryImpl
import com.hungry.binareats.data.repository.MenuRepository
import com.hungry.binareats.data.repository.MenuRepositoryImpl
import com.hungry.binareats.data.repository.UserRepository
import com.hungry.binareats.data.repository.UserRepositoryImpl
import com.hungry.binareats.presentation.feature.cart.CartViewModel
import com.hungry.binareats.presentation.feature.checkout.CheckoutViewModel
import com.hungry.binareats.presentation.feature.detailmenu.DetailMenuViewModel
import com.hungry.binareats.presentation.feature.home.HomeViewModel
import com.hungry.binareats.presentation.feature.login.LoginViewModel
import com.hungry.binareats.presentation.feature.profile.ProfileViewModel
import com.hungry.binareats.presentation.feature.register.RegisterViewModel
import com.hungry.binareats.presentation.feature.splashscreen.SplashScreenViewModel
import com.hungry.binareats.utils.PreferenceDataStoreHelper
import com.hungry.binareats.utils.PreferenceDataStoreHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {

    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { BinarEatsApiService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<BinarEatsDataSource> { BinarEatsApiDataSource(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<MenuRepository> { MenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModel { params -> DetailMenuViewModel(params.get(), get()) }
        viewModelOf(::CartViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashScreenViewModel)
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule
    )
}
