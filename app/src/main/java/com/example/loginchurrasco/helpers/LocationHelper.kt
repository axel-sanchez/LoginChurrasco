package com.example.loginchurrasco.helpers

import com.example.loginchurrasco.data.models.Ubicacion

/**
 * @author Axel Sanchez
 */
object LocationHelper {

    fun getUbicacion(cadena: String): Ubicacion {
        var coma = cadena.indexOf(',')

        var latitud = ""
        var longitud = ""

        var firstSecuence = cadena.substring(0, coma + 1)
        var secondSecuence = cadena.substring(coma + 1, cadena.length)
        if (firstSecuence.indexOf("lat") != 1) {
            latitud = getValue(firstSecuence)
            longitud = getValue(secondSecuence)
        } else if (firstSecuence.indexOf("lon") != 1) {
            latitud = getValue(secondSecuence)
            longitud = getValue(firstSecuence)
        }

        return Ubicacion(longitud, latitud)
    }

    fun getValue(cadena: String): String {
        var coma = cadena.indexOf(',')
        var igual = cadena.indexOf('=')
        return if (coma != -1) cadena.substring(igual + 1, coma)
        else cadena.substring(igual + 1, cadena.length)
    }
}