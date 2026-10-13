package com.example.nav3compose.data.repository

import com.example.nav3compose.domain.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val usuariosCollection = firestore.collection("usuarios")

    // Obtener lista reactiva de usuarios desde Firestore
    val usuarios: Flow<List<Usuario>> = callbackFlow {
        val listener = usuariosCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val listaUsuarios = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Usuario::class.java)?.copy(id = doc.id)
                }
                trySend(listaUsuarios)
            }
        }
        awaitClose { listener.remove() }
    }

    // Insertar usuario en Firestore
    suspend fun insert(usuario: Usuario): String {
        val docRef = usuariosCollection.add(
            mapOf(
                "nombre" to usuario.nombre,
                "contrasena" to usuario.contrasena
            )
        ).await()
        return docRef.id
    }

    // Buscar por ID de documento
    suspend fun getById(id: String): Usuario? {
        val doc = usuariosCollection.document(id).get().await()
        return doc.toObject(Usuario::class.java)?.copy(id = doc.id)
    }

    // Buscar por nombre
    suspend fun getByNombre(nombre: String): Usuario? {
        val query = usuariosCollection
            .whereEqualTo("nombre", nombre)
            .get()
            .await()
        val doc = query.documents.firstOrNull()
        return doc?.toObject(Usuario::class.java)?.copy(id = doc.id)
    }
}