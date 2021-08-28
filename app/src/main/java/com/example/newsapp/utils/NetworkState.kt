package com.example.newsapp.utils

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(val status: Status,
val message: String ?= null){

    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.LOADING)
        fun error(msg: String?) = NetworkState(Status.ERROR, msg)
    }

}