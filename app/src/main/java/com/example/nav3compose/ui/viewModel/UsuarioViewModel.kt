package com.example.nav3compose.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nav3compose.data.repository.UsuarioRepository
import com.example.nav3compose.domain.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val repository: UsuarioRepository
) : ViewModel() {

    // Lista de usuarios almacenados en Firebase
    val usuarios: StateFlow<List<Usuario>> = repository.usuarios.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Usuario seleccionado
    private val _usuarioSeleccionado = MutableStateFlow<Usuario?>(null)

    val usuarioSeleccionado: StateFlow<Usuario?> = _usuarioSeleccionado.asStateFlow()

    // Insertar un nuevo usuario
    fun insertarUsuario(nombre: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = Usuario(
                id = "",
                nombre = nombre,
                contrasena = contrasena
            )

            repository.insert(usuario)
        }
    }

    // Buscar usuario por identificador
    fun buscarPorId(id: String) {
        viewModelScope.launch {
            _usuarioSeleccionado.value = repository.getById(id)
        }
    }

    // Buscar usuario por nombre
    fun buscarPorNombre(nombre: String) {
        viewModelScope.launch {
            _usuarioSeleccionado.value = repository.getByNombre(nombre)
        }
    }
}
