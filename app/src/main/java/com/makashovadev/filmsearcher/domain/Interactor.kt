package com.makashovadev.filmsearcher.domain

import com.makashovadev.filmsearcher.data.repository.MainRepository
import kotlin.random.Random

class Interactor(val repo: MainRepository) {
    private var pageInfo = PageInfo()

    init {
        reset()
    }

    fun reset() {
        repo.initProducts()
        pageInfo = PageInfo()
    }

    fun getFilmsDB(): List<Film> = repo.filmsDataBase


    fun firstPage(): MutableList<Film> {
        pageInfo.nextPage()
        var firstIndex = pageInfo.page * pageInfo.COUNT_ON_PAGE
        var endIndex = firstIndex + pageInfo.COUNT_ON_PAGE - 1

        if (firstIndex > repo.filmsDataBase.size - 1 || endIndex > repo.filmsDataBase.size - 1) {
            pageInfo.isFinish = true
            if (firstIndex > repo.filmsDataBase.size - 1) firstIndex = repo.filmsDataBase.size - 1
            if (endIndex > repo.filmsDataBase.size - 1) endIndex = repo.filmsDataBase.size - 1
        }

        return repo.filmsDataBase.slice(firstIndex..endIndex).toMutableList()
    }

    fun nextPage(): MutableList<Film> {
        if (pageInfo.isFinish) return mutableListOf()
        pageInfo.nextPage()
        var firstIndex = pageInfo.page * pageInfo.COUNT_ON_PAGE
        var endIndex = firstIndex + pageInfo.COUNT_ON_PAGE - 1

        if (firstIndex > repo.filmsDataBase.size - 1 || endIndex > repo.filmsDataBase.size - 1) {
            pageInfo.isFinish = true
            if (firstIndex > repo.filmsDataBase.size - 1) firstIndex = repo.filmsDataBase.size - 1
            if (endIndex > repo.filmsDataBase.size - 1) endIndex = repo.filmsDataBase.size - 1
        }

        return repo.filmsDataBase.slice(firstIndex..endIndex).toMutableList()
    }


    /** class for save page info*/
    inner class PageInfo(var page: Int = -1) {
        val COUNT_ON_PAGE = 6
        var isFinish = false
        fun nextPage() {
            page += 1
        }
    }
}