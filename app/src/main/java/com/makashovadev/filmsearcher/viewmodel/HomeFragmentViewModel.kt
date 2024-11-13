package com.makashovadev.filmsearcher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.domain.Interactor
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(
) : ViewModel() {

    val errorEvent = SingleLiveEvent<String>()

    //  Ошибка получения данных с сервер
    fun postError() {
        errorEvent.postValue("Error receiving films from server")
    }


    // для показа прогресс-бара
    // когда мы хотим его показывать, кладем true, а когда не хотим — false.
    // Канал у нас готов, теперь ссылку на него нужно пробросить во View модель:
    val showProgressBar: Channel<Boolean>

    // ʕ•ᴥ•ʔ
    val filmsListData: Flow<List<Film>>

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
        showProgressBar = interactor.progressBarState
        filmsListData = interactor.getFilmsFromDB()
        loadMovies(currentPage)
        //getFilms(currentPage)
    }

    // загрузка страницы из сети или из БД, если не прошло 10мин
    fun loadMovies(page: Int) {
        try {
            // Проверка, кэшированы ли данные и являются ли они действительными.
            // получить последнее время обновления из шаред преференсис
            val lastDownloadTime = lastDownloadTimeLifeData.value
            val currentTime = Calendar.getInstance().timeInMillis
            // Если прошло не более 10 минут с момента последней загрузки
            if (currentTime - (lastDownloadTime ?: 0) <= TimeUnit.MINUTES.toMillis(10)) {
                // запрос в БД на получения фильмов из кэша в отдельном потоке:
                // Executors.newSingleThreadExecutor().execute {
                //filmsListLiveData.postValue(interactor.getFilmsFromDB())
                //}
            } else {
                // Если данные не кэшированы или устарели, необходимо извлечь из сети
                getFilms(page)
                // Сохранить теущее время в SharedPreferences
                putLastUpdateTime(currentTime ?: 0)
            }
        } catch (e: Exception) {
            // Произошла ошибка, загрузка из кэша
            //Executors.newSingleThreadExecutor().execute {
            //filmsListLiveData.postValue(interactor.getFilmsFromDB())
            //}
        }
    }


    //  загрузка страницы по номеру
    fun getFilms(page: Int) {
        //showProgressBar.postValue(true)
        // появилась сеть
        interactor.getFilmsFromApi(page)
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


    companion object {
        const val HIGH_RAITING = 7.0
        const val START_PAGE = 1
        const val TOTAL_PAGES = 500
    }

}

