package com.example.ktolinfinal.DBControler

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuarios: Usuario)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsuario(usuario: Usuario): Long

    @Query("SELECT id FROM usuarios WHERE usuario = :usuario AND contrasenya = :contrasenya;")
    suspend fun checkUsuario(usuario: String, contrasenya: String): Long?

    // Método para insertar un nuevo usuario y devolver true si la inserción fue exitosa, false en caso contrario
    @Transaction
    fun insertUsuarioSafe(usuario: Usuario): Boolean {
        return try {
            insertUsuario(usuario)
            true // La inserción fue exitosa
        } catch (e: Exception) {
            false // La inserción falló
        }
    }

}