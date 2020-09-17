package com.example.loginchurrasco.domain

import com.example.loginchurrasco.data.TableSite
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.data.repository.GenericRepository
import com.example.loginchurrasco.data.service.ConnectToApi
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
    suspend fun createSite(token: String, site: Site): String? {
        var site = api.createSite(token, site).value
        var siteLocal = repository.getSite(null, null, TableSite.Columns.COLUMN_NAME_ID).last()
        siteLocal.url_imagen = site?.let { it.url_imagen }?:""
        repository.update(siteLocal)
        return site?.url_imagen
    }
}