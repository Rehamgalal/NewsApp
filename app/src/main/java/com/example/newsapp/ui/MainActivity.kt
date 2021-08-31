package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.NewRecyclerAdapter
import com.example.newsapp.model.Article
import com.example.newsapp.utils.OnClickListener
import com.example.newsapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , OnClickListener{

    private lateinit var recyclerAdapter: NewRecyclerAdapter
    private val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        getData()
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = NewRecyclerAdapter()
        recyclerAdapter.setListener(this)
        recycler_view.adapter = recyclerAdapter
    }

    private fun getData() {
         viewModel.setFilter("")
        viewModel.getRecyclerListObserver().observe(this, {
            recyclerAdapter.setDataList(it.articles)
            recyclerAdapter.notifyDataSetChanged()
        })
        viewModel.getNetworkState().observe(this, {
            recyclerAdapter.setNetworkState(it)
            recyclerAdapter.notifyDataSetChanged()
        })
    }

    override fun onArticleCLicked(article: Article) {

    }

}