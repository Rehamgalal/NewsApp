package com.example.newsapp.di

import android.app.Application
import com.example.newsapp.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(application: Application)
}