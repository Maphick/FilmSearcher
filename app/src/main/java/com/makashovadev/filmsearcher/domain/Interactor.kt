package com.makashovadev.filmsearcher.domain

import com.makashovadev.filmsearcher.data.Entity.API
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.TmdbApi
import com.makashovadev.filmsearcher.data.Interfaces.InteractorInterface
import com.makashovadev.filmsearcher.data.dto.TmdbResultsDto
import com.makashovadev.filmsearcher.data.settings.PreferenceProvider
import com.makashovadev.filmsearcher.utils.Converter
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import jakarta.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor @Inject constructor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
    ): InteractorInterface {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    override fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный
        // нам список фильмов
        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            API.KEY,
            "ru-RU",
            page
        ).enqueue(object : Callback<TmdbResultsDto> {
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

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }
    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()


    //fun getFilmsDB(): List<Film> = repo.filmsDataBase
}

