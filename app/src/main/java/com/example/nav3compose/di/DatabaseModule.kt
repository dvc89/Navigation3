package com.example.nav3compose.di

import android.content.Context
import androidx.room.Room
import com.example.nav3compose.data.dao.UsuarioDao
import com.example.nav3compose.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "usuarios_database"
        ).build()
    }

    @Provides
    fun provideUsuarioDao(database: AppDatabase): UsuarioDao {
        return database.usuarioDao()
    }
}
