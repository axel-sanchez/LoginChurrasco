package com.example.loginchurrasco.data.service

import androidx.lifecycle.MutableLiveData
import com.example.loginchurrasco.data.models.MyBody
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.data.models.Sities
import com.google.gson.JsonObject
import org.json.JSONObject




/**
 * Esta clase es la encargada de conectarse a las api's
 * @author Axel Sanchez
 */
class ConnectToApi(private var service: ApiService) {

    suspend fun getAuth(email: String, password: String): MutableLiveData<String?> {
        var mutableLiveData = MutableLiveData<String?>()
        var response = service.getAuth(MyBody(email, password), "application/json")
        if (response.isSuccessful) {
            mutableLiveData.value = response.body()
            println("Token: ${response.body()}")
        }
        else {
            mutableLiveData.value = null
            println("Token: Falló la api de autenticación")
        }
        return mutableLiveData
    }

    suspend fun getSities(token: String): MutableLiveData<Sities?> {
        var mutableLiveData = MutableLiveData<Sities?>()
        var response = service.getSities("application/json", "Bearer $token")
        if (response.isSuccessful) {
            var body = response.body()
            mutableLiveData.value = body
        } else {
            mutableLiveData.value = null
            println("Sities: Falló la api que trae las sities")
        }
        return mutableLiveData
    }

    suspend fun createSite(token: String, site: Site): MutableLiveData<Site?>{
        var mutableLiveData = MutableLiveData<Site?>()
        var response = service.createSite(site, "application/json", "Bearer $token")
        if (response.isSuccessful) {
            var body = response.body()
            mutableLiveData.value = body
        } else {
            mutableLiveData.value = null
            println("Sities: Falló la api que crea una site")
            println("Razon del error: ${response.errorBody().toString()}")
        }
        return mutableLiveData
    }
}