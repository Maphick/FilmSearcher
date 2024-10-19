package com.makashovadev.filmsearcher.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makashovadev.filmsearcher.data.Interfaces.InteractorInterface
import com.makashovadev.filmsearcher.domain.Interactor
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import com.makashovadev.filmsearcher.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


import dagger.MapKey
import kotlin.reflect.KClass

/*
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    //@ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindMyViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}*/