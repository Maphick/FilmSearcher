package com.makashovadev.filmsearcher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.domain.Interactor
import jakarta.inject.Inject


class HomeFragmentViewModel(
) : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<Film>> = MutableLiveData()

    //Инициализируем интерактор
    @Inject
    lateinit var interactor: Interactor


    init {
        //И нам нужно при инициализации самого класса HomeFragmentViewModel вызвать метод inject
        // на компоненте, передав туда ссылку на наш класс:
        App.instance.dagger.inject(this)
        getFilms(1)
    }

    //  загрузка страницы по номеру
    fun getFilms(page: Int) {
        // появилась сеть
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                // удалить фильмы из БД
                //interactor.removeFilmsFromDB()
                filmsListLiveData.postValue(films)
            }

            // коллбэк от Retrofit onFailure, вызывается, когда, например, возникают проблемы с Сетью
            override fun onFailure() {
                // кладем фильмы из БД в LiveData, чтобы на UI у нас появился список фильмов:
                //filmsListLiveData.postValue(interactor.getFilmsFromDB())
                // возвращаем только фильмы с высоким рейтингом
                filmsListLiveData.postValue(interactor.getHighRatingFilmsFromDB(HIGH_RAITING))
            }
        })
    }

    // интерфейс, который будет отвечать за коллбэк
    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }


    companion object {
        const val HIGH_RAITING = 7.0
    }

}

