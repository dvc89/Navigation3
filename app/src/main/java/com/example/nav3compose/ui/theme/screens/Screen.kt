package com.example.nav3compose.ui.theme.screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Login : NavKey
data class Home(
    val nombre: String
) : NavKey