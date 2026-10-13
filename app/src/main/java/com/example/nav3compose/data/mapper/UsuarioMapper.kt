package com.example.nav3compose.data.mapper

import com.example.nav3compose.data.local.UsuarioEntity
import com.example.nav3compose.domain.model.Usuario

fun UsuarioEntity.toDomainModel(): Usuario {
    return Usuario(
        id = this.id.toString(),
        nombre = this.nombre,
        contrasena = this.contrasena
    )
}

fun Usuario.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = this.id.toLongOrNull() ?: 0L,
        nombre = this.nombre,
        contrasena = this.contrasena
    )
}
