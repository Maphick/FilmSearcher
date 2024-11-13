package com.makashovadev.filmsearcher.data.Entity

import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.LiveData
import com.makashovadev.filmsearcher.data.DAO.FilmDao
import com.makashovadev.filmsearcher.data.db.DatabaseHelper
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors


// Операции взаимодействия с БД,
class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        // мы теперь кладем фильмы в БД в Корутине, то есть асинхронно, поэтому в методе putToDb
        // можно убрать использование Executor-a.
        filmDao.insertAll(films)
    }

    fun getAllFromDB(): Flow<List<Film>> {
        return filmDao.getCachedFilms()
    }


    fun clearAll() {
        //Запросы в БД должны быть в отдельном потоке
        //filmDao.deleteAll()
    }
}