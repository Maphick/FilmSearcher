package com.makashovadev.filmsearcher.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.domain.Interactor
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(
) : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<Film>> = MutableLiveData()
    // текущая страница для пагинации
    var currentPage = START_PAGE

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor
    // время последнего обновления
    val lastDownloadTimeLifeData: MutableLiveData<Long> = MutableLiveData()

    init {
        //И нам нужно при инициализации самого класса HomeFragmentViewModel вызвать метод inject
        // на компоненте, передав туда ссылку на наш класс:
        App.instance.dagger.inject(this)
        // загрузка времени последнего обновления из шаред преференсис
        getLastUpdateTime()
        loadMovies(currentPage)
    }

    // загрузка страницы из сети или из БД, если не прошло 10мин
    fun loadMovies(page: Int) {
            try {
                // Проверка, кэшированы ли данные и являются ли они действительными.
                // получить последнее время обновления из шаред преференсис
                val lastDownloadTime = lastDownloadTimeLifeData.value
                val currentTime = Calendar.getInstance().timeInMillis
                // Если прошло не более 10 минут с момента последней загрузки
                if (currentTime - (lastDownloadTime?:0) <= TimeUnit.MINUTES.toMillis(10))
                {
                    // запрос в БД на получения фильмов из кэша в отдельном потоке:
                    Executors.newSingleThreadExecutor().execute {
                        filmsListLiveData.postValue(interactor.getFilmsFromDB())
                    }
                }
                else {
                // Если данные не кэшированы или устарели, необходимо извлечь из сети
                getFilms(page)
                // Сохранить теущее время в SharedPreferences
                putLastUpdateTime(currentTime?:0)
                }
            } catch (e: Exception) {
                // Произошла ошибка, загрузка из кэша
                Executors.newSingleThreadExecutor().execute {
                    filmsListLiveData.postValue(interactor.getFilmsFromDB())
                }
            }
    }


    //  загрузка страницы по номеру
    fun getFilms(page: Int) {
        // появилась сеть
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            // коллбэк от Retrofit onFailure, вызывается, когда, например, возникают проблемы с Сетью
            override fun onFailure() {
                // коллбеке onFailure нужно обернуть запрос в БД на получения фильмов из кэша в
                // отдельный поток:
                Executors.newSingleThreadExecutor().execute {
                    filmsListLiveData.postValue(interactor.getFilmsFromDB())
                }
            }
        })
    }


    //  получить время последнего обновления из шаред преференсис
    private fun getLastUpdateTime() {
        //Кладем категорию в LiveData
        lastDownloadTimeLifeData.value = interactor.getLastUpdateTimeFromPreferences()
    }


    // положить время последнего обновления в шаред преференсис
    fun putLastUpdateTime(time: Long) {
        //Сохраняем в настройки
        interactor.saveLastUpdateTimeToPreferences(time)
        //И сразу забираем, чтобы сохранить состояние в модели
        getLastUpdateTime()
    }


    // интерфейс, который будет отвечать за коллбэк
    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }


    companion object {
        const val HIGH_RAITING = 7.0
        const val START_PAGE = 1
        const val TOTAL_PAGES = 500
    }

}

