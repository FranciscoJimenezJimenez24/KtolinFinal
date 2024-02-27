package com.example.ktolinfinal.DBControler

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

@Database(entities = [Usuario::class], version = 1)
abstract class UsuarioDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile
        private var instance: UsuarioDatabase? = null
        private val DBNAME = "usuarios.bd"
        private val LOCK = Any()

        operator fun invoke(contexto: Context) = instance ?: synchronized(LOCK){//es como un constructor pero sin serlo exactamente
            instance ?: buildDatabase(contexto).also { instance = it }
        }

        private fun buildDatabase(contexto: Context) = Room.databaseBuilder(
            contexto, UsuarioDatabase::class.java, DBNAME
        ).fallbackToDestructiveMigration().build()
    }
}