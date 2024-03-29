package com.example.ktolinfinal.ejercicio1.actividades

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ktolinfinal.DataHelper
import com.example.ktolinfinal.Ejercicio1DetalleActivity
import com.example.ktolinfinal.Ejercicio1FormularioActivity
import com.example.ktolinfinal.Extra
import com.example.ktolinfinal.Nota
import com.example.ktolinfinal.R

class Ejercicio1Activity : AppCompatActivity() {
    private val INSERTAR_DATOS_PRUEBA = true

    private val CODIGO_RESPUESTA_ACTIVIDAD = 1

    private lateinit var mDb: DataHelper
    private lateinit var mAdapter: SimpleCursorAdapter

    private lateinit var mListView: ListView
    private lateinit var mTvListaVacia: TextView
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)

        mListView = findViewById(R.id.list)
        mTvListaVacia = findViewById(R.id.emptyView)
        mTvListaVacia.visibility = View.GONE


        // Instanciamos el DataHelper para el acceso a la base de datos
        mDb = DataHelper(this)

        // Insertamos datos de prueba
        if(INSERTAR_DATOS_PRUEBA) {
            mDb.deleteAll()
            for(i in 1..5) {
                mDb.insert(
                    Nota(
                    -1,
                    "Nota de ejemplo " + String.format("%02d", i),
                    "Contenido de la nota $i",
                    "Observaciones..."
                ),
                )
            }
        }

        // Creamos el adapter usando un SimpleCursorAdapter y el layout simple_list_item_2
        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_2,  // layout propio de Android
            mDb.getCursor(),
            arrayOf("titulo", "contenido"),
            intArrayOf(android.R.id.text1, android.R.id.text2),  // ids de las vistas
            0   // Evitar el auto-requery (deprecated)
        )

        mListView.adapter = mAdapter
        mDb.setAdapter(mAdapter)

        // Listener pulsación
        mListView.setOnItemClickListener { adapterView, view, position, id ->
            val cursor = mAdapter.getItem(position) as Cursor
            val notaId = cursor.getLong(cursor.getColumnIndexOrThrow("_id")) // Obtenemos el identificador de la nota
            Log.d(javaClass.name, "Id: $notaId")
            val intent = Intent(this@Ejercicio1Activity, Ejercicio1DetalleActivity::class.java)
            intent.putExtra(Extra.NOTA_ID, notaId)
            startActivityForResult(intent, CODIGO_RESPUESTA_ACTIVIDAD)
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Si el permiso no se ha concedido, solicítalo
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 3)
            } else {
                // Ya se concedió el permiso, puedes acceder al archivo de audio
                playAudioInfo()
            }
        }

        updateVisibility()
    }

    private fun playAudioInfo() {
        val audioPath = "/sdcard/Download/handle-paper-foley-1-172688.mp3"
        val audioUri: Uri = Uri.parse(audioPath)

        mediaPlayer = MediaPlayer.create(this, audioUri)
        mediaPlayer?.start()
    }

    // Actualizar la visibilidad del listado. Si el listado está vacío mostramos un aviso.
    private fun updateVisibility() {
        if (mAdapter.count == 0) {
            mListView.visibility = View.GONE
            mTvListaVacia.visibility = View.VISIBLE
        } else {
            mListView.visibility = View.VISIBLE
            mTvListaVacia.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        // Asociamos el menú mediante el fichero de recurso
        menuInflater.inflate(R.menu.listado_notas_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.nueva_nota) {
            val intent = Intent(this@Ejercicio1Activity, Ejercicio1FormularioActivity::class.java)
            startActivityForResult(intent, CODIGO_RESPUESTA_ACTIVIDAD)
            return true
        }
        // Devolvemos false si no hemos hecho nada con el elemento seleccionado.
        // (permitirá que se ejecuten otros manejadores)
        // Si devolvemos true finalizará el procesamiento.
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_RESPUESTA_ACTIVIDAD && resultCode == RESULT_OK) {
            mAdapter.changeCursor( mDb.getCursor() ) // Actualizamos el cursor
            updateVisibility()
        }
    }
}
