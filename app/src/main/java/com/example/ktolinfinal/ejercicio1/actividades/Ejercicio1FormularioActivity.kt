package com.example.ktolinfinal
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Ejercicio1FormularioActivity : AppCompatActivity() {

    private lateinit var mDb: DataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etContenido = findViewById<EditText>(R.id.etContenido)

        mDb = DataHelper(this)

        val notaId = intent.getLongExtra(Extra.NOTA_ID, -1)
        if (notaId != -1L) {
            // Si se está editando una nota existente, rellena los campos con los datos de la nota
            val titulo = intent.getStringExtra(Extra.NOTA_TITULO)
            val contenido = intent.getStringExtra(Extra.NOTA_CONTENIDO)
            etTitulo.setText(titulo)
            etContenido.setText(contenido)
        }

        // Listener Guardar
        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val titulo = etTitulo.text.toString()
            val contenido = etContenido.text.toString()

            if (titulo.isEmpty())
                showToast("El título no puede estar vacío")
            else if (contenido.isEmpty())
                showToast("El contenido no puede estar vacío")
            else {
                val otraInformacion = "Observaciones..."
                if (notaId != -1L) {
                    // Si estamos editando una nota existente, actualiza la nota en la base de datos
                    val nota = Nota(notaId, titulo, contenido, otraInformacion)
                    mDb.update(nota)
                    showToast("La nota se ha actualizado")
                } else {
                    // Si estamos agregando una nueva nota, inserta la nota en la base de datos
                    mDb.insert(Nota(-1, titulo, contenido, otraInformacion))
                    showToast("La nota se ha añadido")
                }
                setResult(RESULT_OK)
                finish()
            }
        }


        // Listener Cancelar
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}