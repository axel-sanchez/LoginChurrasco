package com.example.loginchurrasco.data.service

import androidx.lifecycle.MutableLiveData
import com.example.loginchurrasco.data.models.MyBody
import retrofit2.Converter

/**
 * Esta clase es la encargada de conectarse a las api's
 * @author Axel Sanchez
 */
class ConnectToApi(private var service: ApiService) {
    suspend fun getAuth(email: String, password: String): MutableLiveData<String?> {
        var mutableLiveData = MutableLiveData<String?>()
        var response = service.getAuth(MyBody(email, password), "application/json")
        if (response.isSuccessful) mutableLiveData.postValue(response.body())
        else mutableLiveData.postValue(null)
        return mutableLiveData
    }
}