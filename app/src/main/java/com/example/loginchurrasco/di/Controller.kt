package com.example.loginchurrasco.di

import com.example.loginchurrasco.data.service.ApiService
import com.example.loginchurrasco.data.service.ConnectToApi
import com.example.loginchurrasco.domain.LoginUseCase
import com.example.loginchurrasco.domain.SitiesUseCase
import com.example.loginchurrasco.viewmodel.LoginViewModelFactory
import com.example.loginchurrasco.viewmodel.SitiesViewModelFactory
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Axel Sanchez
 */

const val END_POINT = "http://churrasco.uk.to:3005/api/"

val moduleApp = module {
    single { Retrofit.Builder()
        .baseUrl(END_POINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()}
    single { (get() as Retrofit).create(ApiService::class.java) }
    single { ConnectToApi(get()) }
    single { LoginUseCase() }
    single { LoginViewModelFactory(get()) }
    single { SitiesUseCase() }
    single { SitiesViewModelFactory(get()) }
}