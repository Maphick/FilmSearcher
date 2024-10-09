package com.makashovadev.filmsearcher

import android.app.Application
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Entity.TmdbApi
import com.makashovadev.filmsearcher.domain.Interactor
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

// Мы добавляем @HiltAndroidApp к классу App, что дает Hilt сигнал управлять
// внедрением зависимостей в нашем приложении.
// Hilt автоматически внедрит зависимости в поля класса App.
// Нам больше не нужно вручную инициализировать репозиторий и интерактор в onCreate().
@HiltAndroidApp
class App : Application() {


    /*

    @Inject
    lateinit var repo: MainRepository

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var retrofitService: TmdbApi
    */
}