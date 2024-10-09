package com.makashovadev.filmsearcher.di

import com.makashovadev.filmsearcher.BuildConfig
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Entity.TmdbApi
import com.makashovadev.filmsearcher.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.dsl.module

object DI {
    val mainModule = module {
        //Создаем репозиторий
        single { MainRepository() }
        //Создаем объект для получения данных из сети
        single<TmdbApi> {
            val okHttpClient = OkHttpClient.Builder()
                //Настраиваем таймауты для медленного интернета
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //Добавляем логгер
                .addInterceptor(HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                })
                .build()
            //Создаем ретрофит
            val retrofit = Retrofit.Builder()
                //Указываем базовый URL из констант
                .baseUrl(ApiConstants.BASE_URL)
                //Добавляем конвертер
                .addConverterFactory(GsonConverterFactory.create())
                //Добавляем кастомный клиент
                .client(okHttpClient)
                .build()
            //Создаем сам сервис с методами для запросов
            retrofit.create(TmdbApi::class.java)
        }
        //Создаем интерактор
        single { Interactor(get(), get()) }
    }
}