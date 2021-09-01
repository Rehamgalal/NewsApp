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
import java.util.*
import javax.inject.Inject


class OnboardingActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        (application as NewsApp).getAppComponent().inject(this)

        val country = sharedPrefrences.getString("country", "")
        if (!country.equals("")) {
            moveToMain()
        }
        setSpinner(R.array.countries, country_spinner)
        setSpinner(R.array.categories, category_spinner)

        var cat1 = 0
        var cat2 = 0
        var cat3 = 0
        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                if (category_1.text.toString().isEmpty()) {
                    category_1.text = category_spinner.selectedItem.toString()
                    cat1 = category_spinner.selectedItemId.toInt()
                } else if (category_2.text.toString().isEmpty()) {
                    category_2.text = category_spinner.selectedItem.toString()
                    cat2 = category_spinner.selectedItemId.toInt()
                } else if (category_3.text.toString().isEmpty()) {
                    category_3.text = category_spinner.selectedItem.toString()
                    cat3 = category_spinner.selectedItemId.toInt()
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
            if (!category_1.text.toString().isEmpty() || !category_2.text.toString().isEmpty() || !category_3.text.toString().isEmpty()) {
                val editor = sharedPrefrences.edit()
                val res = resources
                val conf = res.configuration
                conf.setLocale(Locale("en_US"))
                val categories = res.getStringArray(R.array.categories)
                editor.putString("country", country_spinner.selectedItem.toString().substring(0, 2))
                editor.putString("category1", categories.get(cat1))
                editor.putString("category2", categories.get(cat2))
                editor.putString("category3", categories.get(cat3))
                editor.apply()
                conf.setLocale(Locale("ar_EG"))
                moveToMain()
            } else {
                Toast.makeText(this, "Please Choose 3 categories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun setSpinner(array: Int, view: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
                this,
                array, R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        view.adapter = adapter
    }
}