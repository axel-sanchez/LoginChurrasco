package com.example.loginchurrasco.data.models

import android.os.Parcel
import android.os.Parcelable

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
    val ubicacion: Any?,
    val url_imagen: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?.let { it }?:"",
        parcel.readParcelable(Detalle::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readString()?.let { it }?:"",
        parcel.readString()?.let { it }?:"",
        parcel.readString()?.let { it }?:"",
        TODO(),
        //parcel.readParcelable(Ubicacion::class.java.classLoader)!!,
        parcel.readString()?.let { it }?:"")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(descripcion)
        parcel.writeParcelable(detalle, flags)
        //parcel.writeParcelable(ubicacion, flags)
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(nombre)
        parcel.writeString(url_imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Site> {
        override fun createFromParcel(parcel: Parcel): Site {
            return Site(parcel)
        }

        override fun newArray(size: Int): Array<Site?> {
            return arrayOfNulls(size)
        }
    }
}

data class Detalle(
    val categoria: String,
    val rubro: String,
    val seccion: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?.let { it }?:"",
        parcel.readString()?.let { it }?:"",
        parcel.readString()?.let { it }?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoria)
        parcel.writeString(rubro)
        parcel.writeString(seccion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Detalle> {
        override fun createFromParcel(parcel: Parcel): Detalle {
            return Detalle(parcel)
        }

        override fun newArray(size: Int): Array<Detalle?> {
            return arrayOfNulls(size)
        }
    }
}

data class Ubicacion(val _long: String, val _lat: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?.let { it }?:"",
        parcel.readString()?.let { it }?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_long)
        parcel.writeString(_lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ubicacion> {
        override fun createFromParcel(parcel: Parcel): Ubicacion {
            return Ubicacion(parcel)
        }

        override fun newArray(size: Int): Array<Ubicacion?> {
            return arrayOfNulls(size)
        }
    }
}