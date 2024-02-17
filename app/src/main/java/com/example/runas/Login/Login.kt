package com.example.runas.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.runas.R

class Login : AppCompatActivity() {
    lateinit var editTextUser: EditText
    lateinit var editTextPass: EditText
    lateinit var checkRememberUser: CheckBox
    lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUser = findViewById(R.id.editTextUser)
        editTextPass = findViewById(R.id.editTextPass)
        checkRememberUser = findViewById(R.id.checkRememberUser)
        btnLogin = findViewById(R.id.btnLogin)

        // para cargar los datos guardados del fichero de variables compartidas
        getValuesFromShared()
        btnLogin.setOnClickListener{ onClickLogin()}
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
            showToast("Datos guardados")
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