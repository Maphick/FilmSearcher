package com.makashovadev.filmsearcher.data.Entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// класс Film, который мы кладем в интерактор
@Parcelize
// Для нашей таблицы мы задали имя cached_films, а также здесь мы задали индекс по названию фильма,
// чтобы у нас не было двух одинаковых фильмов в БД.
@Entity(tableName = "cached_films", indices = [Index(value = ["title"], unique = true)])
data class Film(
    // Мы задали дополнительное поле id, в котором будут храниться Primary Key-значения, они
    // автогенерируемые, поэтому мы выставляем значение по умолчанию, чтобы явно не указывать его
    // при создании объектов — всё равно оно будет перезаписано при добавлении в таблицу.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Через эту аннотацию задаём имена для колонок. Это не обязательно, но так будет правильнее:
    // если мы потом захотим поменять какие-то поля объекта Film, нам не придётся менять таблицу и
    // делать миграцию (если бы приложение было уже в продакшене).
    @ColumnInfo(name = "title") val title: String,
    //У нас будет приходить ссылка на картинку, так что теперь это String
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    //Приходит нецелое число с API
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0,
    var isInFavorites: Boolean = false
) : Parcelable