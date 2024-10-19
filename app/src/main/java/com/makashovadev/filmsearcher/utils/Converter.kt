package com.makashovadev.filmsearcher.utils

import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.data.dto.TmdbFilm

// с API приходит один объект, а в RV мы кладем список с другими объектами.
// Конвертер для преобразования списка оъектов от API в список объектов для RV
// Просто получаем список с API и из его полей составляем новый список в нужном нам формате
object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                title = it.title,
                poster = it.posterPath,
                description = it.overview,
                rating = it.voteAverage,
                isInFavorites = false
            )
            )
        }
        return result
    }
}