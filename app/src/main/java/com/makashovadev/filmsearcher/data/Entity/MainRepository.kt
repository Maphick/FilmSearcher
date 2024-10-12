package com.makashovadev.filmsearcher.data.Entity

import com.makashovadev.filmsearcher.data.Interfaces.RepositoryInterface
import com.makashovadev.filmsearcher.domain.Film


class  MainRepository : RepositoryInterface {

    override val filmsDataBase: MutableList<Film> = mutableListOf()

    override fun getData(): List<Film> {
        return filmsDataBase
    }
}
