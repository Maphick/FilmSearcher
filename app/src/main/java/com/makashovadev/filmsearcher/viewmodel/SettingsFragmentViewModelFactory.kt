package com.makashovadev.filmsearcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import com.makashovadev.filmsearcher.domain.Interactor
import jakarta.inject.Inject

// Чтобы внедрить ViewModel с помощью Dagger, необходимо создать пользовательский
// ViewModelProvider.Factory, который делегирует Dagger задачу создания ViewModel.
class SettingsFragmentViewModelFactory
@Inject constructor(
    private val interactor: Interactor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsFragmentViewModel::class.java)) {
            return SettingsFragmentViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}