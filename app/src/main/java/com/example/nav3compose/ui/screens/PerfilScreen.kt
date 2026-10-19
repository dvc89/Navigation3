package com.example.nav3compose.ui.screens

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun PerfilContentScreen(
    nombre: String
) {
    val context = LocalContext.current
    var fotoPerfilBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher de la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoPerfilBitmap = bitmap
        }
    }

    // Estado del Reproductor de Audio (MediaPlayer)
    var isAudioPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember {
        MediaPlayer.create(context, android.provider.Settings.System.DEFAULT_RINGTONE_URI)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    // Estado de ExoPlayer para Video (Media3)
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = "https://www.w3schools.com/html/mov_bbb.mp4"
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil de $nombre",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Foto de perfil de la Cámara
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (fotoPerfilBitmap != null) {
                Image(
                    bitmap = fotoPerfilBitmap!!.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(70.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { cameraLauncher.launch(null) }) {
            Text(text = "Tomar foto de perfil")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Reproductor de Audio (MediaPlayer)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reproductor de Audio (MediaPlayer)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (mediaPlayer != null) {
                                if (isAudioPlaying) {
                                    mediaPlayer.pause()
                                    isAudioPlaying = false
                                } else {
                                    mediaPlayer.start()
                                    isAudioPlaying = true
                                }
                            }
                        }
                    ) {
                        Text(if (isAudioPlaying) "Pausar Audio" else "Reproducir Audio")
                    }

                    OutlinedButton(
                        onClick = {
                            mediaPlayer?.let {
                                if (it.isPlaying) {
                                    it.pause()
                                }
                                it.seekTo(0)
                                isAudioPlaying = false
                            }
                        }
                    ) {
                        Text("Detener")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Reproductor de Video (ExoPlayer Media3)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reproductor de Video (ExoPlayer)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }
    }
}
