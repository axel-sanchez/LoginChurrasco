package com.example.loginchurrasco.data.service

import androidx.lifecycle.MutableLiveData
import com.example.loginchurrasco.data.models.MyBody
import com.example.loginchurrasco.data.models.Sities

/**
 * Esta clase es la encargada de conectarse a las api's
 * @author Axel Sanchez
 */
class ConnectToApi(private var service: ApiService) {
    suspend fun getAuth(email: String, password: String): MutableLiveData<String?> {
        var mutableLiveData = MutableLiveData<String?>()
        var response = service.getAuth(MyBody(email, password), "application/json")
        if (response.isSuccessful) {
            mutableLiveData.postValue(response.body())
            println("Token: ${response.body()}")
        }
        else {
            mutableLiveData.postValue(null)
            println("Token: Falló la api de autenticación")
        }
        return mutableLiveData
    }

    suspend fun getSities(token: String): MutableLiveData<Sities?> {
        var mutableLiveData = MutableLiveData<Sities?>()
        var response = service.getSities("application/json", "Bearer $token")
        if (response.isSuccessful) {
            var body = response.body()
            mutableLiveData.postValue(body)
            println("Tiene ${response.body()?.sites?.size} sities")
        } else {
            mutableLiveData.postValue(null)
            println("Sities: Falló la api que trae las sities")
        }
        return mutableLiveData
    }
}