package com.makashovadev.filmsearcher.data.Interfaces

import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel

interface InteractorInterface {
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback)
}

