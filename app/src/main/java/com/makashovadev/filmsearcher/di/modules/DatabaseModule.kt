package com.makashovadev.filmsearcher.di.modules

import android.content.Context
import androidx.room.Room
import com.makashovadev.filmsearcher.data.DAO.FilmDao
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import com.makashovadev.filmsearcher.data.db.DatabaseHelper
import com.makashovadev.filmsearcher.data.db.FilmsDatabase
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

//@Binds используется в модуле DatabaseModule,
// чтобы привязать интерфейс RepositoryInterface к реализации.
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context,
            FilmsDatabase::class.java,
            "film_db"
        ).build().filmDao()

        @Provides
        @Singleton
        fun provideRepository(filmDao: FilmDao): MainRepository{
            return MainRepository(filmDao) // Provide the repository implementation
        }

}
