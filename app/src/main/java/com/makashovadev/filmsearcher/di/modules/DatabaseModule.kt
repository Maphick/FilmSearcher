package com.makashovadev.filmsearcher.di.modules

import android.content.Context
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import com.makashovadev.filmsearcher.data.db.DatabaseHelper
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
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}
