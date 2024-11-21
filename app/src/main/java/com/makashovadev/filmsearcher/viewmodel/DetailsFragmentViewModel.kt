package com.makashovadev.filmsearcher.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.net.URL

class DetailsFragmentViewModel : ViewModel() {
    // Во вью модель мы прописываем ссылку на наш BehaviourSubject:
    val showProgressBar: BehaviorSubject<Boolean>  = BehaviorSubject.create()
    //var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    val errorEvent = SingleLiveEvent<String>()

    //  Ошибка получения данных с сервер
    fun postError(){
        errorEvent.postValue("Error receiving picture from server")
    }


    // Метод для загрузки картинки с использованием RxJava
    fun loadWallpaper(url: String): Single<Bitmap> {
        return Single.create<Bitmap> { emitter ->
            try {
                val imageUrl = URL(url)
                val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
                if (bitmap != null) {
                    // Выдать битмап, если загрузка прошла успешно
                    emitter.onSuccess(bitmap)
                } else {
                    //  Выдать искючение, если битмап null
                    emitter.onError(Exception("Bitmap is null"))
                }
            } catch (e: Exception) {
                // Выдать ошибку, если возникнет какое-либо исключение
                emitter.onError(e)
                postError()
            }
        }
            // Выполнить сетевую операцию в потоке ввода-вывода
            .subscribeOn(Schedulers.io())
            /// Наблюдаем в основном потоке для обновления пользовательского интерфейса
            .observeOn(AndroidSchedulers.mainThread())
    }
}
