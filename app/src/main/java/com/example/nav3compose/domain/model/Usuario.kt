package com.example.nav3compose.domain.model

data class Usuario(
    val id: String = "", //Cambiado porque en Firestore el id se genera automáticamente como String
    val nombre: String = "",
    val contrasena: String = ""
)
