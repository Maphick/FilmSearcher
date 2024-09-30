package com.makashovadev.filmsearcher.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.domain.Interactor


class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()

    //Инициализируем интерактор
    private var interactor: Interactor = App.instance.interactor


    init {
        //filmsListLiveData.postValue(interactor.getFilmsDB())
        interactor.getFilmsFromApi(1, object : ApiCallback {

            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })

    }

    //  загрузка страницы по номеру
    fun loadPage(page: Int) {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                // добавление новых фильмов
                var oldData = mutableListOf<Film>()
                filmsListLiveData.value?.let { oldData.addAll(it) }
                oldData.addAll(films)
                filmsListLiveData.postValue(oldData.toList())
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