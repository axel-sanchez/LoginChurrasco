package com.example.loginchurrasco.data.models

data class Sities(
    val sites: List<Site>
)

data class Site(
    val descripcion: String,
    val detalle: Detalle,
    val id: Int,
    val image: String,
    val name: String,
    val nombre: String,
    val ubicacion: Any,
    val url_imagen: String
)

data class Detalle(
    val categoria: String,
    val rubro: String,
    val seccion: String
)