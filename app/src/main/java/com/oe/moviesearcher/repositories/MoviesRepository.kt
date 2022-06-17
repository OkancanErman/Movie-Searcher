package com.oe.moviesearcher.repositories

import com.oe.moviesearcher.network.MovieResponse
import com.oe.moviesearcher.network.OMDBApi
import com.oe.moviesearcher.network.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository @Inject constructor(val api: OMDBApi) {

    suspend fun searchMovies(searchText: String) : Response<SearchResponse> {
        return api.searchMovies(searchText)
    }

    suspend fun getMovie(movie_id: String) : Response<MovieResponse> {
        return api.getMovie(movie_id)
    }
}