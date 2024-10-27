package com.makashovadev.filmsearcher.data.Interfaces

import com.makashovadev.filmsearcher.data.Entity.Film

interface RepositoryInterface {
    abstract val filmsDataBase: List<Film>

    fun getData():  List<Film>
}