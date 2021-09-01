package com.example.newsapp.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.newsapp.api.NewsApi
import com.example.newsapp.db.ArticlesDatabase
import com.example.newsapp.other.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun getRetrofitInstance(okHttpClient: OkHttpClient):Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOKHttpClient():OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("apiKey", Constants.API_KEY)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun getRetrofitService(retrofit: Retrofit):NewsApi {
        return retrofit.create(NewsApi::class.java)
    }


    @Singleton
    @Provides
    fun getSharedPrefrences():SharedPreferences{
        return application.getSharedPreferences("userSelection", MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providesArticlesDatabase(): ArticlesDatabase {
       return ArticlesDatabase.buildDatabase(application.applicationContext)}

}