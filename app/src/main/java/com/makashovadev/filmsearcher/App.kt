package com.makashovadev.filmsearcher

import android.app.Application
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Entity.TmdbApi
import com.makashovadev.filmsearcher.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Инициализируем репозиторий
        repo = MainRepository()

        //Создаём кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()

        // Create your Retrofit instance
        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor {

                    chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMjA3NGVhNmJhZDYxNGJhMDQwMmI2MWI3MzkyY2YwMiIsIm5iZiI6MTcyNzMzMjY2Mi43NDQ3NzksInN1YiI6IjY2YzZmYzRlMzlmYjExOWQxNmQ3ZWRhMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kTq6cXUMkw2F22MYT1J6qKp-kP54lTfgMgH5o0jqFgE")
                        .build()
                    chain.proceed(request)


                }
                .build()
            )
            .build()


        //Создаем Ретрофит
        val retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient).build()
            //Создаем сам сервис с методами для запросов


        /*val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/movie_id/images")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMjA3NGVhNmJhZDYxNGJhMDQwMmI2MWI3MzkyY2YwMiIsIm5iZiI6MTcyNzMzMjY2Mi43NDQ3NzksInN1YiI6IjY2YzZmYzRlMzlmYjExOWQxNmQ3ZWRhMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kTq6cXUMkw2F22MYT1J6qKp-kP54lTfgMgH5o0jqFgE")
            .build()*/

        val retrofitService = retrofit.create(TmdbApi::class.java)
        //Инициализируем интерактор
        interactor = Interactor(repo, retrofitService)

    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}