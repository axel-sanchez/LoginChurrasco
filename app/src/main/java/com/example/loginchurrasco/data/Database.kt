package com.example.loginchurrasco.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * Clase que crea la base de datos y contiene los nombres de las tablas para su mejor acceso
 * @author Axel Sanchez
 */
class Database(context: Context): SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        // If you change the database schema, you must increment the database version.
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "churrascoDB.db3"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Create Table
        db!!.execSQL(SQL_CREATE_SITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //delete table
        db!!.execSQL(SQL_DELETE_SITE)
        //create table
        onCreate(db)
    }
}

//CREATE
private const val  SQL_CREATE_SITE =
    "CREATE TABLE ${TableSite.Columns.TABLE_NAME} (" +
            "${TableSite.Columns.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${TableSite.Columns.COLUMN_NAME_NOMBRE} TEXT," +
            "${TableSite.Columns.COLUMN_NAME_LATITUD} TEXT," +
            "${TableSite.Columns.COLUMN_NAME_LONGITUD} TEXT," +
            "${TableSite.Columns.COLUMN_NAME_DESCRIPCION} TEXT," +
            "${TableSite.Columns.COLUMN_NAME_URL_IMAGEN} TEXT)"

//DELETE
private const val SQL_DELETE_SITE          = "DROP TABLE IF EXISTS ${TableSite.Columns.TABLE_NAME}"

//TABLE
object TableSite{
    // Table contents are grouped together in an anonymous object.
    object Columns : BaseColumns {
        const val TABLE_NAME                 =    "site"
        const val COLUMN_NAME_ID             =    "id"
        const val COLUMN_NAME_NOMBRE         =    "nombre"
        const val COLUMN_NAME_LATITUD        =    "latitud"
        const val COLUMN_NAME_LONGITUD       =    "longitud"
        const val COLUMN_NAME_DESCRIPCION    =    "descripcion"
        const val COLUMN_NAME_URL_IMAGEN     =    "url_imagen"
    }
}