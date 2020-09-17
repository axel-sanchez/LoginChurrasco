package com.example.loginchurrasco.domain

import com.example.loginchurrasco.data.TableSite
import com.example.loginchurrasco.data.repository.GenericRepository
import com.example.loginchurrasco.data.service.ConnectToApi
import org.json.JSONObject
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class SiteNewUseCase : KoinComponent {
    private val api: ConnectToApi by inject()
    private val repository: GenericRepository by inject()

    /**
     *
     * @return devuelve la url de la imagen
     */
    suspend fun createSite(token: String, json: JSONObject): String? {
        var urlImagen = api.createSite(token, json).value
        var site = repository.getSite(null, null, TableSite.Columns.COLUMN_NAME_ID).last()
        site.url_imagen = urlImagen?.let { it }?:""
        repository.update(site)
        return urlImagen
    }
}