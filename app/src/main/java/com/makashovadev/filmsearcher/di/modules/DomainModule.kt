package com.makashovadev.filmsearcher.di.modules

import com.makashovadev.filmsearcher.data.Interfaces.InteractorInterface
import com.makashovadev.filmsearcher.domain.Interactor
import dagger.Binds
import dagger.Module
import jakarta.inject.Singleton

@Module
abstract class DomainModule {
    @Binds
    @Singleton
    abstract fun bindInteractor(interactor: Interactor): InteractorInterface
}

