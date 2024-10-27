package com.makashovadev.filmsearcher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makashovadev.filmsearcher.data.DAO.FilmDao
import com.makashovadev.filmsearcher.data.Entity.Film

@Database(entities = [Film::class], version = 1, exportSchema = true)
abstract class FilmsDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}