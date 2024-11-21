package com.makashovadev.filmsearcher.domain

import android.annotation.SuppressLint
import android.util.Log
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
import com.makashovadev.filmsearcher.utils.Converter.convertApiListToDtoListObservable
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
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

    // BehaviorSubject (Cold Observable) — отправляет только последнее значение последовательности
    // каждому новому подписчику. По сути, ReplaySubject с размером кэша 1.
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()


    override fun getFilmsFromApi(page: Int) {
        //Показываем ProgressBar
        progressBarState.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(
            category = getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page
        )
            // Запрос выполняется асинхронно с помощью метода enqueue, который не блокирует основной
            // поток и передает управление обратно в UI после завершения запроса.
            .enqueue(object : Callback<TmdbResultsDto> {
                @SuppressLint("CheckResult")
                override fun onResponse(
                    call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>
                ) {
                    // конвертацию ответа от API в DTO-объект
                    if (response.isSuccessful) {
                        // Продолжать обработку списка только в случае успешного ответа (200 OK)
                        val list = response.body()?.tmdbFilms
                        if (list != null) {
                            // Конвертация данных в список объектов типа Film
                            val listObservable = Converter.convertApiListToDtoListObservable(list)
                            listObservable
                                // Выполнить в фоновом потоке
                                .subscribeOn(Schedulers.io())
                                .subscribe({ films ->
                                    // Вставить фильмы в базу данных
                                    repo.putToDb(films)
                                    // Скрыть ProgressBar
                                    progressBarState.onNext(false)
                                }, { throwable ->
                                    // Обработать ошибку, если конвертация или вставка не удалась
                                    Log.e("Interactor", "Error processing films", throwable)
                                    // Скрыть ProgressBar
                                    progressBarState.onNext(false)
                                })
                        } else {
                            // Ответ API пуст
                            Log.e("Interactor", "API response is empty")
                            progressBarState.onNext(false)
                        }
                    } else {
                        // Обработать неуспешный ответ (например, 404, 500)
                        Log.e("Interactor", "API call failed with HTTP code: ${response.code()}")
                        progressBarState.onNext(false)
                    }
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    //В случае провала выключаем ProgressBar
                    progressBarState.onNext(false)
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
    //fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFromDB()
    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()

    // удалить все фильмы из базы
    fun clearFilmsFromDB() = repo.clearAll()


}

