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
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })
    }

    // интерфейс, который будет отвечать за коллбэк
    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }


}

