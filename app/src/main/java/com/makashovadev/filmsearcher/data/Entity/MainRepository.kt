package com.makashovadev.filmsearcher.data.Entity

import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.LiveData
import com.makashovadev.filmsearcher.data.DAO.FilmDao
import com.makashovadev.filmsearcher.data.db.DatabaseHelper
import java.util.concurrent.Executors


// Операции взаимодействия с БД,
class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            //filmDao.deleteAll()
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): LiveData<List<Film>> {
        return filmDao.getCachedFilms()
    }


    fun clearAll() {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            //filmDao.deleteAll()
        }
    }
}