package com.oe.moviesearcher.fragment

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oe.moviesearcher.network.MovieResponse
import com.oe.moviesearcher.network.Search
import com.oe.moviesearcher.repositories.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    val repository: MoviesRepository
) : ViewModel() {

    private val eventChannel = Channel<Status>()

    val eventFlow = eventChannel.receiveAsFlow()

    var searchText: String? = "batman"

    private lateinit var job: Job

    private val _movies = MutableLiveData<List<Search>>()

    private val _details = MutableLiveData<List<MovieResponse>>()
    val details: LiveData<List<MovieResponse>>
        get() = _details


    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized)
            job.cancel()
    }

    private fun getDetails(){
        viewModelScope.launch(Dispatchers.IO){
            val mutableList = mutableListOf<MovieResponse>()
            try {
                _movies.value?.forEach{
                    val _movieresponse = repository.getMovie(it.imdbID!!)
                    mutableList.add(_movieresponse.body()!!)
                }
                withContext(Dispatchers.Main) {
                    _details.value = mutableList
                    eventChannel.send(Status.Success(null))
                }
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    _details.value = mutableList
                    eventChannel.send(Status.Error(throwable.toString()))
                }
            }
        }
    }


    fun onSearchButtonClick(view: View){

        viewModelScope.launch(Dispatchers.IO){
            eventChannel.send(Status.Loading(null))
            try {

                val response = repository.searchMovies(searchText!!)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _movies.value = response.body()?.search!!
                        getDetails()
                    } else
                        eventChannel.send(Status.Error("Error Code: ${response.code()}"))
                }

            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    eventChannel.send(Status.Error(throwable.toString()))
                }
            }
        }

    }

    sealed class Status {
        data class Loading(val message: String?) : Status()
        data class Success(val message: String?) : Status()
        data class Error(val message: String?) : Status()
    }

}