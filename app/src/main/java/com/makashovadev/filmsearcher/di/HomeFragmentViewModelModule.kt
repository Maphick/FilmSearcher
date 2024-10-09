package com.makashovadev.filmsearcher.di

import androidx.lifecycle.ViewModelProvider
import com.makashovadev.filmsearcher.BuildConfig
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.MainRepository
import com.makashovadev.filmsearcher.data.Entity.TmdbApi
import com.makashovadev.filmsearcher.domain.Interactor
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
//@InstallIn(ActivityComponent::class)
@InstallIn(SingletonComponent::class)
object HomeFragmentViewModelModule {

    // provideTmdbApi: создает и возвращает экземпляр нашего интерфейса TmdbApi,
    // привязанный к экземпляру Retrofit.
    @Provides
   // @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }


    @Provides
   // @Singleton
    fun provideMainRepository(): MainRepository {
        return MainRepository()
    }

    @Provides
    //@Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
    }

    // provideRetrofit: создает и возвращает экземпляр Retrofit,
    // используя предоставленный OkHttpClient.
    @Provides
    //@Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient).build()
    }


    // InteractorModule: создает и предоставляет экземпляр Interactor,
    // внедряя repo и retrofitService.
    @Provides
    //@Singleton
    fun provideInteractor(repo: MainRepository, retrofitService: TmdbApi): Interactor {
        return Interactor(repo, retrofitService)
    }


    @Provides
    fun provideViewModel(iterator: Interactor): HomeFragmentViewModel
            = HomeFragmentViewModel(iterator)
        //ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)



}