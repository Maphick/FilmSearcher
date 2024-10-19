package com.makashovadev.filmsearcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.domain.Interactor
import jakarta.inject.Inject

class ViewModelFactory @Inject constructor(
    //private val interactor: Interactor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            return HomeFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}