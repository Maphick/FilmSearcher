package com.makashovadev.filmsearcher.di

import com.makashovadev.filmsearcher.di.modules.DomainModule
import com.makashovadev.filmsearcher.di.modules.DatabaseModule
import com.makashovadev.filmsearcher.di.modules.RemoteModule
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
    ]
)
interface AppComponent {
    //метод для того, чтобы внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}

