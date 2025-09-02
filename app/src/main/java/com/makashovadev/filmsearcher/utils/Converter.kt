package com.makashovadev.filmsearcher.utils

import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.data.dto.TmdbFilm

// с API приходит один объект, а в RV мы кладем список с другими объектами.
// Конвертер для преобразования списка оъектов от API в список объектов для RV
// Просто получаем список с API и из его полей составляем новый список в нужном нам формате
object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach { tmdbFilm ->
            // Проверяем на null все обязательные поля
            val title = tmdbFilm.title ?: "Unknown Title"
            val poster = tmdbFilm.posterPath ?: ""
            val overview = tmdbFilm.overview ?: "No description available"

            result.add(
                Film(
                    title = title,
                    poster = poster,
                    description = overview,
                    rating = tmdbFilm.voteAverage,
                    isInFavorites = false
                )
            )
        }
        return result
    }
}