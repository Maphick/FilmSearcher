package com.makashovadev.filmsearcher.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.domain.Interactor
import com.makashovadev.filmsearcher.utils.diff_util.updateData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


// @HiltViewModel сообщает Hilt, что нужно управлять зависимостями этой ViewModel
//@HiltViewModel
// Конструктор @Inject используется для получения зависимости Interactor,
// автоматически внедряемой Hilt.
//@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()

    init {
        loadPage(1) // Load the first page on initialization
    }

    //  загрузка страницы по номеру
    fun loadPage(page: Int) {
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>)
            {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
                // Handle the failure case appropriately
            }
        })
    }

    // интерфейс, который будет отвечать за коллбэк
    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

}

