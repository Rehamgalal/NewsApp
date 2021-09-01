package com.example.newsapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
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

        (application as NewsApp).getAppComponent().inject(this)

        val country = sharedPrefrences.getString("country","")
        if (!country.equals("")) {
            moveToMain()
        }
        setSpinner(R.array.countries,country_spinner)
        setSpinner(R.array.categories,category_spinner)

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(category_1.text.toString().isEmpty()) {
                    category_1.text = category_spinner.selectedItem.toString()
                } else if(category_2.text.toString().isEmpty()) {
                    category_2.text = category_spinner.selectedItem.toString()
                } else if(category_3.text.toString().isEmpty()) {
                    category_3.text = category_spinner.selectedItem.toString()
                } else {
                    category_1.text = ""
                    category_2.text = ""
                    category_3.text = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        save.setOnClickListener {
            if (!category_1.text.toString().isEmpty() ||!category_2.text.toString().isEmpty() ||!category_3.text.toString().isEmpty() ) {
            val editor = sharedPrefrences.edit()
            editor.putString("country", country_spinner.selectedItem.toString().substring(0,2))
            editor.putString("category1", category_1.text.toString())
            editor.putString("category2", category_2.text.toString())
            editor.putString("category3", category_3.text.toString())
            editor.apply()
            moveToMain()}
            else {
                Toast.makeText(this,"Please Choose 3 categories",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun moveToMain() {
        val  intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    fun setSpinner(array: Int,view : Spinner) {
        val adapter = ArrayAdapter.createFromResource(
                this,
                array, R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        view.adapter = adapter
    }
}