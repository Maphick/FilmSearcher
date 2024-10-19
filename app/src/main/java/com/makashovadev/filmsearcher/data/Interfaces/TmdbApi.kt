package com.makashovadev.filmsearcher.data.Interfaces

import com.makashovadev.filmsearcher.data.dto.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


// интерфейс, который отвечает за создание методов получения информации с сервера:
interface TmdbApi {
    @GET("3/movie/{category}")
    fun getFilms(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>
}

