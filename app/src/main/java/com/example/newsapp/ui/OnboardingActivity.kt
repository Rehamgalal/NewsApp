package com.example.newsapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.NewsApp
import com.example.newsapp.R
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject


class OnboardingActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefrences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val adapterCountry = ArrayAdapter.createFromResource(
            this,
            R.array.countries, R.layout.simple_spinner_item
        )
        adapterCountry.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = adapterCountry

        val adapterCategory = ArrayAdapter.createFromResource(
            this,
            R.array.categories, R.layout.simple_spinner_item
        )
        adapterCategory.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = adapterCategory
        (application as NewsApp).getAppComponent().inject(application)
        val editor = sharedPrefrences.edit()
        editor.putString("country", "")
        editor.putString("category", "")
        editor.putString("lang", "")
        editor.apply()


    }
}