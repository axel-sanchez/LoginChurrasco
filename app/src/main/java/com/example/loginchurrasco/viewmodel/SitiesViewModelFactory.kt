package com.example.loginchurrasco.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginchurrasco.domain.SitiesUseCase

/**
 * Factory de nuestro [SitiesViewModel]
 * @author Axel Sanchez
 */
class SitiesViewModelFactory(private val sitiesUseCase: SitiesUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SitiesUseCase::class.java).newInstance(sitiesUseCase)
    }
}