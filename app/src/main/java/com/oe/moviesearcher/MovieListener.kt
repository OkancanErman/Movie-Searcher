package com.oe.moviesearcher

interface MovieListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)

}