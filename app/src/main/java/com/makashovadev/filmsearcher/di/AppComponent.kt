package com.makashovadev.filmsearcher.di

import com.makashovadev.filmsearcher.di.modules.DomainModule
import com.makashovadev.filmsearcher.di.modules.DatabaseModule
import com.makashovadev.filmsearcher.di.modules.RemoteModule
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import com.makashovadev.filmsearcher.viewmodel.SettingsFragmentViewModel
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
       // ViewModelModule::class
    ]
)

interface AppComponent {
    //метод для того, чтобы появилась возможность внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}



