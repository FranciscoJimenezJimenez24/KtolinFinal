package com.example.ktolinfinal.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.ktolinfinal.DBControler.UsuarioDatabase
import com.example.ktolinfinal.Ejercicio1Activity
import com.example.ktolinfinal.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    lateinit var database: UsuarioDatabase
    lateinit var editTextUser: EditText
    lateinit var editTextPass: EditText
    lateinit var checkRememberUser: CheckBox
    lateinit var btnLogin: Button
    lateinit var btnRegistro: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        database = UsuarioDatabase(this)

        val id: Long = -1
        editTextUser = findViewById(R.id.editTextUser)
        editTextPass = findViewById(R.id.editTextPass)
        checkRememberUser = findViewById(R.id.checkRememberUser)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistro = findViewById(R.id.btnRegistro)

        // para cargar los datos guardados del fichero de variables compartidas
        getValuesFromShared()

        btnLogin.setOnClickListener{
            onClickLogin()

            // Utiliza un coroutine para llamar a checkUsuario
            CoroutineScope(Dispatchers.Main).launch {
                val id = database.usuarioDao().checkUsuario(editTextUser.text.toString(), editTextPass.text.toString())

                // Verificar si el usuario existe
                if (id != null) {
                    showToast("¡Inicio de sesión exitoso!"+id)
                    // startNewActivity()
                } else {
                    showToast("Usuario o contraseña incorrectos")
                }
            }
            val intent:Intent=Intent(this,Ejercicio1Activity::class.java);
            startActivity(intent)

        }

        btnRegistro.setOnClickListener {
            val intentBtn = Intent(this, Registro::class.java)
            startActivity(intentBtn)
        }
    }

    private fun getValuesFromShared(){
        editTextUser.text = SharedApplication.preferences.user.toEditable()
        editTextPass.text = SharedApplication.preferences.pass.toEditable()
    }

    private fun onClickLogin(){
        if(checkRememberUser.isChecked){
            persistValues()
        } else {
            deleteValues()
        }
    }

    private fun persistValues(){
        if(editTextUser.text.isEmpty() || editTextPass.text.isEmpty()){
            showToast("Los datos no pueden estar vacios")
        } else {
            SharedApplication.preferences.user = editTextUser.text.toString()
            SharedApplication.preferences.pass = editTextPass.text.toString()
            //showToast("Datos guardados")
        }
    }

    private fun deleteValues(){
        SharedApplication.preferences.clear()
        showToast("Datos guardados")
    }

    private fun showToast(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun String.toEditable() : Editable = Editable.Factory.getInstance().newEditable(this)
}