package com.makashovadev.filmsearcher

import android.app.Application
import com.makashovadev.filmsearcher.di.AppComponent
import com.makashovadev.filmsearcher.di.DaggerAppComponent
import com.makashovadev.filmsearcher.di.modules.DatabaseModule
import com.makashovadev.filmsearcher.di.modules.DomainModule
import com.makashovadev.filmsearcher.di.modules.RemoteModule


class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}