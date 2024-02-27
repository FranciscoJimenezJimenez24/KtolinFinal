package com.example.ktolinfinal
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class Ejercicio1DetalleActivity : AppCompatActivity() {
    private val CODIGO_RESPUESTA_ACTIVIDAD = 1

    private lateinit var tvTitulo: TextView
    private lateinit var tvContenido: TextView

    private lateinit var mDb: DataHelper
    private var mId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        tvTitulo = findViewById(R.id.tvTitulo)
        tvContenido = findViewById(R.id.tvContenido)

        if (intent == null) {
            returnResult("Error: no se ha especificado ninguna nota")
            return
        }

        mDb = DataHelper(this)
        mId = intent.getLongExtra(Extra.NOTA_ID, -1)

        if (mId == -1L) {
            returnResult("Error: nota incorrecta")
            return
        }

        updateInterface()

        findViewById<Button>(R.id.btnEditar).setOnClickListener {
            val nota = mDb.selectById(mId)
            if (nota != null) {
                val intent = Intent(this@Ejercicio1DetalleActivity, Ejercicio1FormularioActivity::class.java)
                intent.putExtra(Extra.NOTA_ID, nota._id)
                intent.putExtra(Extra.NOTA_TITULO, nota.titulo)
                intent.putExtra(Extra.NOTA_CONTENIDO, nota.contenido)
                startActivityForResult(intent, CODIGO_RESPUESTA_ACTIVIDAD)
            } else {
                returnResult("Error: nota no encontrada")
            }
        }

        findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            deleteNota(mId)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener{
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_RESPUESTA_ACTIVIDAD && resultCode == RESULT_OK) {
            updateInterface()
            setResult(RESULT_OK)
        }
    }

    private fun updateInterface() {
        val nota = mDb.selectById(mId)
        if (nota == null)
            returnResult("Error: nota incorrecta")
        else {
            tvTitulo.text = nota.titulo
            tvContenido.text = nota.contenido
        }
    }

    private fun deleteNota(id: Long) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Desea eliminar esta nota?")
            .setPositiveButton("Aceptar") { _, _ ->
                val result = mDb.deleteById(mId)
                if (result > 0)
                    returnResult("La nota se ha eliminado")
                else
                    Toast.makeText(this, "Error al eliminar la nota", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun returnResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }
}
