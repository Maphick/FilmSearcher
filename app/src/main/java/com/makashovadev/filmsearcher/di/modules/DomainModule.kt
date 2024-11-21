package com.makashovadev.filmsearcher.di.modules

import android.content.Context
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.InteractorInterface
import com.makashovadev.filmsearcher.data.Interfaces.TmdbApi
import com.makashovadev.filmsearcher.data.settings.PreferenceProvider
import com.makashovadev.filmsearcher.domain.Interactor
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
//Передаем контекст для SharedPreferences через конструктор
class DomainModule(val context: Context) {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(
        repository: MainRepository,
        tmdbApi: TmdbApi,
        preferenceProvider: PreferenceProvider
    ) = Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)


    // определить модуль Dagger для предоставления ViewModelFactory и Repository, которые внедряются во
    // ViewModel.
    @Singleton
    @Provides
    fun provideHomeFragmentViewModelFactory(interactor: Interactor): HomeFragmentViewModelFactory {
        return HomeFragmentViewModelFactory(interactor)
    }


}

