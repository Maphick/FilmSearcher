package com.makashovadev.filmsearcher.data.Entity

import com.makashovadev.filmsearcher.data.dto.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// интерфейс, который отвечает за создание методов получения информации с сервера:
interface TmdbApi {
    @GET("3/movie/popular")
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>
}