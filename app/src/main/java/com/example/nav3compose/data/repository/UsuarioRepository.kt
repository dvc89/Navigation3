package com.example.nav3compose.data.repository

import com.example.nav3compose.data.dao.UsuarioDao
import com.example.nav3compose.data.mapper.toDomainModel
import com.example.nav3compose.data.mapper.toEntity
import com.example.nav3compose.domain.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepository @Inject constructor(private val dao: UsuarioDao) {
    val usuarios: Flow<List<Usuario>> =
        dao.obtenerTodos().map { list -> list.map { it.toDomainModel() } }

    suspend fun insert(usuario: Usuario): Long =
        dao.insertUsuario(usuario.toEntity())

    suspend fun getById(id: Long): Usuario? =
        dao.buscarPorId(id)?.toDomainModel()

    suspend fun getByNombre(nombre: String): Usuario? =
        dao.buscarPorNombre(nombre)?.toDomainModel()
}
