package com.makashovadev.filmsearcher.data.Entity

import android.content.ContentValues
import android.database.Cursor
import com.makashovadev.filmsearcher.data.db.DatabaseHelper
import com.makashovadev.filmsearcher.domain.Film


// Операции взаимодействия с БД,
class MainRepository (databaseHelper: DatabaseHelper) {
    //Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase
    //Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor


    // добавить фильм в базу
    fun putToDb(film: Film) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
        //Кладем фильм в БД
        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }


    // обновить фильм по имени из базы
    fun  updateByTitle(film: Film) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.poster)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
        sqlDb.update(DatabaseHelper.TABLE_NAME,
            cv,
            DatabaseHelper.COLUMN_TITLE + "=" + "?",
            arrayOf(film.title)
        )
    }

    // удалить фильм по имени из базы
    fun removeByTitle(title: String) {
        sqlDb.delete(DatabaseHelper.TABLE_NAME,
            DatabaseHelper.COLUMN_TITLE+ "=" + "?",
            arrayOf(title)
        )
    }

    // удалить фильм по индексу из базы
    fun removeById(index: Int) {
        sqlDb.delete(DatabaseHelper.TABLE_NAME,
            DatabaseHelper.COLUMN_ID + "=" + "?",
            arrayOf(index.toString())
        )
    }

    // удалить все фильмы из базы
    fun removeAllFromDB() {
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        // проходим по всем строкам,
        // начиная с первой
        // пока возможно moveToNext()
        if (cursor.moveToLast()) {
            val columnNames = cursor.columnNames
            do {
                // id текущего фильма
                var curTitle = cursor.getString(1)
                removeByTitle(curTitle)
            } while (cursor.moveToPrevious())
        }
    }


    fun getAllFromDB(): List<Film> {
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        //Сюда будем сохранять результат получения данных
        val result = mutableListOf<Film>()
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)

                result.add(Film(title, poster, description, rating))
            } while (cursor.moveToNext())
        }
        //Возвращаем список фильмов
        return result
    }

    // запрос фильмов с высоким рейтингом
    fun getHighRating(raitnig: Double): List<Film>
    {
        //Сюда будем сохранять результат получения данных
        val result = mutableListOf<Film>()
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE  " +
                "${DatabaseHelper.COLUMN_RATING} >= ?", arrayOf(raitnig.toString()))
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                result.add(Film(title, poster, description, rating))
            } while (cursor.moveToNext())
        }
        //Возвращаем список фильмов с высоким рейтингом
        return result
    }
}
