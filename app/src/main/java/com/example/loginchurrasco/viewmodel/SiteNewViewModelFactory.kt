package com.example.loginchurrasco.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginchurrasco.domain.SiteNewUseCase

/**
 * Factory de nuestro [MyViewModel]
 * @author Axel Sanchez
 */
class SiteNewViewModelFactory(private val siteNewUseCase: SiteNewUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SiteNewUseCase::class.java).newInstance(siteNewUseCase)
    }
}