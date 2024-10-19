package com.makashovadev.filmsearcher.di.modules

import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

//@Binds используется в модуле DatabaseModule,
// чтобы привязать интерфейс RepositoryInterface к реализации.
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}
