package com.example.loginchurrasco.data.service

import com.example.loginchurrasco.data.models.MyAuth
import com.example.loginchurrasco.data.models.MyBody
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.data.models.Sities
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface para generar las peticiones a la api
 * @author Axel Sanchez
 */
interface ApiService {
    @POST("auth")
    suspend fun getAuth(@Body body: MyBody, @Header("Content-Type") contentType: String): Response<String?>

    @GET("sites")
    suspend fun getSities(@Header("Content-Type") contentType: String, @Header("Authorization") token: String): Response<Sities?>

    @POST("sites")
    suspend fun createSite(@Body json: JSONObject, @Header("Content-Type") contentType: String, @Header("Authorization") token: String): Response<String?>
}