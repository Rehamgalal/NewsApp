package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines/")
    fun getArticles(@Query("country")country:String, @Query("category")category: String, @Query("q")searchKey:String):Single<NewsResponse>
}