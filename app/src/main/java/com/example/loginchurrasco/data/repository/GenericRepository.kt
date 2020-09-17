package com.example.loginchurrasco.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.loginchurrasco.data.Database
import com.example.loginchurrasco.data.TableSite
import com.example.loginchurrasco.data.models.Detalle
import com.example.loginchurrasco.data.models.Site
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Clase utilizada para obtener y modificar datos de la database
 * @author Axel Sanchez
 */
class GenericRepository: KoinComponent {

    private val dbHelper: Database by inject()
    private val db = dbHelper.writableDatabase
    private val dbR = dbHelper.readableDatabase

    /**
     * Insertar un [Site] en la database
     * @param [item] recibe un [Site]
     * @return devuelve el id del registro insertado, si falla devuelve un -1
     */
    fun insert(item: Site, latitud: String, longitud: String): Long {
        return try {
            val values = ContentValues().apply {
                put(TableSite.Columns.COLUMN_NAME_ID, item.id)
                put(TableSite.Columns.COLUMN_NAME_NOMBRE, item.name)
                put(TableSite.Columns.COLUMN_NAME_LATITUD, latitud)
                put(TableSite.Columns.COLUMN_NAME_LONGITUD, longitud)
                put(TableSite.Columns.COLUMN_NAME_DESCRIPCION, item.descripcion)
                put(TableSite.Columns.COLUMN_NAME_URL_IMAGEN, item.url_imagen)
            }
            db.insertWithOnConflict(TableSite.Columns.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
            Log.e("INSERT MOVIE", e.message!!)
            -1
        }
    }

    /**
     * Actualiza una [Movie] en la database
     * @param [item] recibe una Movie
     * @return devuelve el id del registro actualizado, si falla devuelve un -1
     */
    fun update(item: Site): Int {

        val values = ContentValues().apply {
            put(TableSite.Columns.COLUMN_NAME_ID, item.id)
            put(TableSite.Columns.COLUMN_NAME_NOMBRE, item.name)
            put(TableSite.Columns.COLUMN_NAME_DESCRIPCION, item.descripcion)
            put(TableSite.Columns.COLUMN_NAME_URL_IMAGEN, item.url_imagen)
        }

        // Which row to update, based on the title
        val selection = "${TableSite.Columns.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(item.id.toString())

        return db.updateWithOnConflict(TableSite.Columns.TABLE_NAME, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE)
    }

    /**
     * Obtener una [Movie] de la database
     * @param [whereColumns] recibe un listado de nombres de campos que quiero filtrar
     * @param [whereArgs] recibe un listado de los resultados a comparar con [whereColumns]
     * @param [orderByColumn] recibe el nombre de la tabla con la que vamos a ordenar los resultados
     * @return devuelve un mutableList de peliculas
     */
    fun getSite(whereColumns: Array<String>?, whereArgs: Array<String>?, orderByColumn: String?): MutableList<Site> {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            TableSite.Columns.COLUMN_NAME_ID,
            TableSite.Columns.COLUMN_NAME_NOMBRE,
            TableSite.Columns.COLUMN_NAME_LATITUD,
            TableSite.Columns.COLUMN_NAME_LONGITUD,
            TableSite.Columns.COLUMN_NAME_DESCRIPCION,
            TableSite.Columns.COLUMN_NAME_URL_IMAGEN,
        )

        // Filter results WHERE "title" = 'My Title'
        val selection = setWhere(whereColumns)

        // How you want the results sorted in the resulting Cursor
        val sortOrder: String? = if (orderByColumn?.count() != 0) "$orderByColumn DESC" else null

        var cursor = dbR.query(
            TableSite.Columns.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            whereArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )


        val items = mutableListOf<Site>()
        while (cursor.moveToNext()) {
            items.add(
                Site(
                    cursor.getInt(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_DESCRIPCION)),
                    Detalle("", "", ""),
                    cursor.getString(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_URL_IMAGEN)),
                    cursor.getString(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_NOMBRE)),
                    null,
                    cursor.getString(cursor.getColumnIndex(TableSite.Columns.COLUMN_NAME_URL_IMAGEN))
                )
            )
        }
        return items
    }

    /**
     * Elimina una [Movie] de la database
     * @param [id] recibe el id de la pelicula
     * @return devuelve el id del registro eliminado, si falla devuelve un -1
     */
    fun deleteSite(id: Long): Int {
        // Define 'where' part of query.
        val selection = "${TableSite.Columns.COLUMN_NAME_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        return db.delete(TableSite.Columns.TABLE_NAME, selection, selectionArgs)
    }

    private fun setWhere(whereColumns: Array<String>?): String? {
        if (whereColumns != null) {
            val result = StringBuilder()
            var first = true
            for (w in whereColumns) {
                if (first)
                    first = false
                else
                    result.append(" AND ")

                result.append(w)
                result.append("=?")
            }
            return result.toString()
        } else {
            return null
        }
    }
}