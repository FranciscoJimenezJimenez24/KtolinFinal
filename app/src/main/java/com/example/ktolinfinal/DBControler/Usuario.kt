package com.example.ktolinfinal.DBControler

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "usuario")
    val usuario: String,

    @ColumnInfo(name = "contrasenya")
    val contrasenya: String,
){// Función redefinida para mostrar los datos correctamente
    override fun toString(): String {

        return "Usuario: "+usuario +" Contrasenya: "+contrasenya
    }
}