package ejercicios_android.sqlite.ejercicio1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.widget.BaseAdapter

class DataHelper(contexto: Context) {
    private val DBNAME = "notas_ej1.bd" // Cambiamos el nombre de la base de datos
    private val DBVERSION = 2

    private var mAdapter: BaseAdapter? = null

    private lateinit var mDB: SQLiteDatabase

    private lateinit var mInsertStatement: SQLiteStatement

    init {
        val oHelper = MiOpenHelper(contexto, DBNAME, null, DBVERSION)
        mDB = oHelper.writableDatabase

        val INSERT = "INSERT INTO notas (titulo, contenido) VALUES (?,?)"
        mInsertStatement = mDB.compileStatement(INSERT)
    }

    fun setAdapter(adapter: BaseAdapter?) {
        mAdapter = adapter
    }

    fun getCursor(): Cursor? {
        val orderBy = "titulo ASC"
        return mDB.query("notas", null, null, null, null, null, orderBy) // Cambiamos el nombre de la tabla
    }

    fun selectById(id: Long): Nota? {
        val columns = arrayOf("titulo", "contenido") // Cambiamos los nombres de las columnas
        val selection = "_id = ?"
        val args = arrayOf(id.toString())
        val limit = "1"
        val cursor = mDB.query("notas", columns, selection, args, null, null, null, limit) // Cambiamos el nombre de la tabla
        var nota: Nota? = null
        if (cursor != null && cursor.moveToFirst()) {
            nota = Nota(id, cursor.getString(0), cursor.getString(1), null) // Cambiamos el orden de los campos
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close() // Cerramos el cursor
        }
        return nota
    }

    fun insert(nota: Nota): Long {
        val values = ContentValues()
        values.put("titulo", nota.titulo) // Cambiamos el nombre de la columna
        values.put("contenido", nota.contenido) // Cambiamos el nombre de la columna
        val rvalue = mDB.insert("notas", null, values) // Cambiamos el nombre de la tabla
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    // Insertar datos mediante sentencia compilada
    fun insertWithCompileStatement(nota: Nota): Long {
        mInsertStatement.bindString(1, nota.titulo) // Cambiamos el nombre de la columna
        mInsertStatement.bindString(2, nota.contenido) // Cambiamos el nombre de la columna
        val rvalue = mInsertStatement.executeInsert()
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun update(nota: Nota): Int {
        val values = ContentValues()
        values.put("titulo", nota.titulo) // Cambiamos el nombre de la columna
        values.put("contenido", nota.contenido) // Cambiamos el nombre de la columna
        val selection = "_id = ?"
        val args = arrayOf(nota._id.toString())
        val rvalue = mDB.update("notas", values, selection, args) // Cambiamos el nombre de la tabla
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun deleteAll(): Int {
        val rvalue = mDB.delete("notas", "1", null) // Cambiamos el nombre de la tabla
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun deleteById(id: Long): Int {
        val rvalue = mDB.delete("notas", "_id = ?", arrayOf(id.toString())) // Cambiamos el nombre de la tabla
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    class MiOpenHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
        SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase) {
            val CREATE_DB = ("CREATE TABLE notas (" // Cambiamos el nombre de la tabla
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "titulo TEXT,"
                    + "contenido TEXT)") // Cambiamos el nombre de la columna
            db.execSQL(CREATE_DB)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if(oldVersion == 1)
                db.execSQL("ALTER TABLE notas ADD COLUMN contenido TEXT") // Cambiamos el nombre de la tabla y de la columna
            if(oldVersion > 1)
                throw IllegalStateException("Versión desconocida $oldVersion")
        }
    }
}
