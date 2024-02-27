package com.example.ktolinfinal.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ktolinfinal.DBControler.Usuario
import com.example.ktolinfinal.DBControler.UsuarioDatabase
import com.example.ktolinfinal.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class Registro : AppCompatActivity() {
    lateinit var database: UsuarioDatabase
    lateinit var guardar: Button
    lateinit var cancelar: Button
    lateinit var user: EditText
    lateinit var pass: EditText
    lateinit var passAgain: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        database = UsuarioDatabase(this)

        guardar = findViewById(R.id.btnGuardar)
        cancelar = findViewById(R.id.btnCancelar)
        user = findViewById(R.id.editTextUser)
        pass = findViewById(R.id.editTextPass)
        passAgain = findViewById(R.id.editTextPassAgain)

        guardar.setOnClickListener {

            if (comprobarContra(pass.text.toString(), passAgain.text.toString())) {
                showToast("Las contraseñas coinciden.")
                val usuario = Usuario(usuario = user.text.toString(), contrasenya = pass.text.toString())
                // Lanzar una corrutina en el hilo de fondo
                CoroutineScope(Dispatchers.IO).launch {
                    // Llamar a insertUsuarioSafe en el hilo de fondo
                    val exitoso = database.usuarioDao().insertUsuarioSafe(usuario)

                    // Actualizar la interfaz de usuario en el hilo principal
                    withContext(Dispatchers.Main) {
                        if (exitoso) {
                            showToast("Usuario insertado correctamente.")
                            finish()
                        } else {
                            showToast("Error al insertar el usuario.")
                        }
                    }
                }
            } else {
                showToast("Las contraseñas no coinciden.")
            }
        }

        cancelar.setOnClickListener {
            if(!isFinishing){
                finish()
            }
        }
    }

    private fun comprobarContra(pass:String, passAgain:String):Boolean{
        return pass == passAgain
    }

    private fun showToast(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}