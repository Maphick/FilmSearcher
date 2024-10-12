package com.makashovadev.filmsearcher.di.modules

import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import dagger.Binds
import dagger.Module
import jakarta.inject.Singleton

//@Binds используется в модуле DatabaseModule,
// чтобы привязать интерфейс RepositoryInterface к реализации.
@Module
abstract class DatabaseModule {
    @Binds
    @Singleton
    abstract fun bindRepository(repository: MainRepository): RepositoryInterface
}

