package com.example.loginchurrasco.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginchurrasco.data.models.Sities
import com.example.loginchurrasco.domain.SitiesUseCase

/**
 * View model de [SitiesFragment]
 * @author Axel Sanchez
 */
class SitiesViewModel(private val sitiesUseCase: SitiesUseCase) : ViewModel() {

    private val listData = MutableLiveData<Sities?>()

    private fun setListData(sities: Sities?) {
        listData.postValue(sities)
    }

    suspend fun getSities(token: String) {
        setListData(sitiesUseCase.getSities(token))
    }

    fun getSitiesLiveData(): LiveData<Sities?> {
        return listData
    }
}