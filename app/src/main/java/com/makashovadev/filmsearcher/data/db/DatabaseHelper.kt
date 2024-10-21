package com.makashovadev.filmsearcher.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        //Создаем саму таблицу для фильмов
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    // этот столбец для PRIMARY KEY, он будет создаваться автоматически.
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    // этот столбец будет хранить название фильма, он также помечен как уникальный,
                    // чтобы у нас не было двух одинаковых фильмов в таблице, и их не нужно было бы потом фильтровать.
                    "$COLUMN_TITLE TEXT UNIQUE," +
                    // столбец для ссылки на постер.
                    "$COLUMN_POSTER TEXT," +
                    // столбец для хранения описания.
                    "$COLUMN_DESCRIPTION TEXT," +
                    // столбец для рейтинга. Рейтинг у нас в Double, поэтому мы создаем столбец
                    // с типом REAL.
                    "$COLUMN_RATING REAL);"
        )
    }
    //Миграций мы не предполагаем, поэтому метод пустой
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        //Название самой БД
        private const val DATABASE_NAME = "films.db"
        //Версия БД
        private const val DATABASE_VERSION = 1

        //Константы для работы с таблицей, они нам понадобятся в CRUD операциях и,
        //возможно, в составлении запросов
        const val TABLE_NAME = "films_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER = "poster_path"
        const val COLUMN_DESCRIPTION = "overview"
        const val COLUMN_RATING = "vote_average"
    }
}