package com.makashovadev.filmsearcher.di

import com.makashovadev.filmsearcher.di.modules.DomainModule
import com.makashovadev.filmsearcher.di.modules.DatabaseModule
import com.makashovadev.filmsearcher.di.modules.RemoteModule
import com.makashovadev.filmsearcher.view.fragments.DetailsFragment
import com.makashovadev.filmsearcher.view.fragments.HomeFragment
import com.makashovadev.filmsearcher.view.fragments.SettingsFragment
import com.makashovadev.filmsearcher.viewmodel.DetailsFragmentViewModelFactory
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
    ]
)

// Настройка своего компонента Dagger
// компонент Dagger, который будет внедрять зависимости в Home Fragment, где мы хотим использовать
// HomeFragmentViewModel .
interface AppComponent {
    //метод для того, чтобы появилась возможность внедрять зависимости HomeFragmentViewModel во фрагмент
    fun inject(homeFragment : HomeFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(detailsFragment: DetailsFragment)
    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    //fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    //fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}




