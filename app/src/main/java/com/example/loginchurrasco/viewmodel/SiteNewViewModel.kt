package com.example.loginchurrasco.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.domain.SiteNewUseCase
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.*

/**
 * View model de [SiteNewFragment]
 * @author Axel Sanchez
 */
class SiteNewViewModel(private val siteNewUseCase: SiteNewUseCase) : ViewModel() {

    private val listData = MutableLiveData<String?>()

    private fun setListData(urlImage: String?) {
        listData.postValue(urlImage)
    }

    suspend fun createSite(token: String, site: Site) {
        setListData(siteNewUseCase.createSite(token, site))
    }

    fun getUrlLiveData(): LiveData<String?> {
        return listData
    }
}