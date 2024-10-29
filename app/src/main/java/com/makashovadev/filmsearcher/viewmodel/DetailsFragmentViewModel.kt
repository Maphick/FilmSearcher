package com.makashovadev.filmsearcher.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel : ViewModel() {

    val errorEvent = SingleLiveEvent<String>()

    //  Ошибка получения данных с сервер
    fun postError(){
        errorEvent.postValue("Error receiving picture from server")
    }
    //cметод для загрузки нашей картинки

    // Помечаем функцию как suspend, потому как у нас будет логика показа Прогресс-бара, а также
    // тоста в конце, и нам необходима прерывающаяся функция, которая будет возвращать объект Bitmap.
    suspend fun loadWallpaper(url: String): Bitmap? {
        // само по себе слово suspend никакой магии не делает, это просто маркер, а нам нужен объект
        // Continuation, поэтому мы используем метод suspendCoroutine, чтобы получить к нему доступ.
        return suspendCoroutine {
            // Далее мы загружаем файл из Сети вот таким нехитрым образом. В URL из представления мы
            // будем передавать адрес картинки.
            val url = URL(url)
            var bitmap: Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }
            catch (e:Exception) {
               postError()
            }
            // чтобы suspend функция не «потерялась» и продолжила свою работу, нужно вызывать метод
            // resume, а в нашем случае ещё возвращать и bitmap
            it.resume(bitmap)
        }
    }
}