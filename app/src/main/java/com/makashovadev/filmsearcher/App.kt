package com.makashovadev.filmsearcher

import android.app.Application
import com.makashovadev.filmsearcher.di.AppComponent
import com.makashovadev.filmsearcher.di.DaggerAppComponent


class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}