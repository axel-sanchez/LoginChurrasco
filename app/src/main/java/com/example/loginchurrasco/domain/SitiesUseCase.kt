package com.example.loginchurrasco.domain

import com.example.loginchurrasco.data.models.Sities
import com.example.loginchurrasco.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para [SitiesViewModel]
 * @author Axel Sanchez
 */
class SitiesUseCase : KoinComponent {
    private val api: ConnectToApi by inject()

    /**
     * Le pido a la api que me devuelva los sitios de interés
     * @return devuelve un Sities
     */
    suspend fun getSities(token: String): Sities? {
        var sities = api.getSities(token)
        var value = sities.value
        return value
    }
}