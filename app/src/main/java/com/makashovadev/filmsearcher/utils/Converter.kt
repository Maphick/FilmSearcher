package com.makashovadev.filmsearcher.utils

import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.data.dto.TmdbFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// с API приходит один объект, а в RV мы кладем список с другими объектами.
// Конвертер для преобразования списка оъектов от API в список объектов для RV
// Просто получаем список с API и из его полей составляем новый список в нужном нам формате
object Converter {
    suspend fun convertApiListToDtoListFlow(list: List<TmdbFilm>?): Flow<List<Film>>
    // flow — это оператор, который позволяет создавать поток данных. Мы используем его, чтобы в
    // асинхронной среде создать и вернуть List<Film>.
            = flow {
        // Преобразуем list в Flow
        val convertedList = list?.map {
            Film(
                title = it.title,
                poster = it.posterPath,
                description = it.overview,
                rating = it.voteAverage,
                isInFavorites = false
            )
        } ?: emptyList()

        // Эмитируем конвертированный список в Flow
        //  эмиттируем результат в поток. Этот результат будет доступен для подписчиков на Flow.
        emit(convertedList)
    }
}