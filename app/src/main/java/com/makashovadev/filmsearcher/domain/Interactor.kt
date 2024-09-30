package com.makashovadev.filmsearcher.domain

import com.makashovadev.filmsearcher.data.Entity.API
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Entity.TmdbApi
import com.makashovadev.filmsearcher.data.dto.TmdbResultsDto
import com.makashovadev.filmsearcher.utils.Converter
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {


        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }
}

/*
class Interactor(val repo: MainRepository, retrofitService: TmdbApi) {
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
}*/