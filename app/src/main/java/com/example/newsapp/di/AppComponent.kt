package com.example.newsapp.di

import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.OnboardingActivity
import com.example.newsapp.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(mainActivity: MainActivity)
}