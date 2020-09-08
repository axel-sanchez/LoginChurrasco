package com.example.loginchurrasco.domain

import com.example.loginchurrasco.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para el Login
 * @author Axel Sanchez
 */
class LoginUseCase: KoinComponent {
    private val api: ConnectToApi by inject()

    /**
     * Funcion que llama al ConnecToApi y le pide el token
     * @return devuelve un String con el token
     */
    suspend fun getToken(email: String, password: String): String? {
        return api.getAuth(email, password).value
    }
}