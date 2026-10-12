package com.example.nav3compose.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nav3compose.data.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertUsuario(usuario: UsuarioEntity) : Long
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE nombre = :nombre")
    suspend fun buscarPorNombre(nombre: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun buscarPorId(id: Long): UsuarioEntity?

}