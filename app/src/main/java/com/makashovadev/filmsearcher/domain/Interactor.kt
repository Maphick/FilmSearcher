package com.makashovadev.filmsearcher.domain

import androidx.lifecycle.LiveData
import com.makashovadev.filmsearcher.data.Entity.API
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Interfaces.TmdbApi
import com.makashovadev.filmsearcher.data.Interfaces.InteractorInterface
import com.makashovadev.filmsearcher.data.dto.TmdbFilm
import com.makashovadev.filmsearcher.data.dto.TmdbResultsDto
import com.makashovadev.filmsearcher.data.settings.PreferenceProvider
import com.makashovadev.filmsearcher.utils.Converter
import com.makashovadev.filmsearcher.utils.Converter.convertApiListToDtoListFlow
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor @Inject constructor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) : InteractorInterface {

    // Здесь нам нужен будет канал и скоуп, в котором мы будем отправлять данные в канал
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    // У канала нам важно, чтобы только одно значение было единовременно, поэтому мы его создаем
    // CONFLATED
    var progressBarState = Channel<Boolean>(Channel.CONFLATED)

    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    override fun getFilmsFromApi(page: Int) {

        //Показываем ProgressBar
        scope.launch {
            progressBarState.send(true)
        }
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(
            category = getDefaultCategoryFromPreferences(),
            API.KEY,
            "ru-RU",
            page
        )
            .enqueue(object : Callback<TmdbResultsDto> {

                override fun onResponse(
                    call: Call<TmdbResultsDto>,
                    response: Response<TmdbResultsDto>
                ) {


                    scope.launch {
                        val apiList: List<TmdbFilm>? =
                            response.body()?.tmdbFilms// Получаем список фильмов из API (например, через Retrofit)
                        // конвертацию ответа от API в DTO-объект
                        val listFlow = convertApiListToDtoListFlow(apiList)

                        listFlow.collect { list ->
                            // Теперь у нас есть List<Film> — можно передавать в репозиторий
                            repo.putToDb(list)  // Кладем фильмы в БД
                            progressBarState.send(false)  // Выключаем ProgressBar
                        }
                    }
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    //В случае провала выключаем ProgressBar
                    scope.launch {
                        progressBarState.send(false)
                    }
                }
            })
    }

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()


    // Метод для сохранения времени последнего обновления
    fun saveLastUpdateTimeToPreferences(time: Long) {
        preferences.saveLastUpdateTime(time)
    }

    //Метод для получения времени последнего обновления
    fun getLastUpdateTimeFromPreferences() = preferences.getLastUpdateTime()


    // получить все фильмы из БД:
    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFromDB()

    // удалить все фильмы из базы
    fun clearFilmsFromDB() = repo.clearAll()


}

