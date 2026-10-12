package com.example.nav3compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nav3compose.data.dao.UsuarioDao

@Database(
    entities = [UsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}
