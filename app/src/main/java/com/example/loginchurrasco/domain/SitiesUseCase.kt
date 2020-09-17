package com.example.loginchurrasco.domain

import com.example.loginchurrasco.data.models.Sities
import com.example.loginchurrasco.data.repository.GenericRepository
import com.example.loginchurrasco.data.service.ConnectToApi
import com.example.loginchurrasco.helpers.LocationHelper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.lang.Exception

/**
 * Caso de uso para [SitiesViewModel]
 * @author Axel Sanchez
 */
class SitiesUseCase : KoinComponent {
    private val api: ConnectToApi by inject()
    private val repository: GenericRepository by inject()

    /**
     * Le pido a la api que me devuelva los sitios de interés
     * @return devuelve un Sities
     */
    suspend fun getSities(token: String): Sities? {

        var sites = repository.getSite(null, null, null)

        if (sites.isNotEmpty()) return Sities(sites)

        var sities = api.getSities(token)
        var value = sities.value
        value?.let {
            for (site in it.sites) {
                try {
                    var ubicacionStr = site.ubicacion.toString().let {
                        if (it.isNotEmpty()) it.substring(1, it.length - 1)
                        else ""
                    }

                    var ubicacion = LocationHelper.getUbicacion(ubicacionStr)
                    repository.insert(site, ubicacion._lat, ubicacion._long)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return value
    }
}