package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.NewsApp
import com.example.newsapp.R
import com.example.newsapp.adapters.NewRecyclerAdapter
import com.example.newsapp.db.ArticleEntity
import com.example.newsapp.db.ArticlesDatabase
import com.example.newsapp.utils.OnClickListener
import com.example.newsapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() , OnClickListener{

    private lateinit var recyclerAdapter: NewRecyclerAdapter
    private val viewModel:MainViewModel by viewModels()

    @Inject
    lateinit var dataBase: ArticlesDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as NewsApp).getAppComponent().inject(this)
        initRecyclerView()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setFilter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setFilter(newText)
                return false
            }

        })
        return true
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
            recyclerAdapter.setDataList(it)
            recyclerAdapter.notifyDataSetChanged()
        })
        viewModel.getNetworkState().observe(this, {
            recyclerAdapter.setNetworkState(it)
            recyclerAdapter.notifyDataSetChanged()
        })
    }

    override fun onArticleCLicked(article: ArticleEntity) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("article",article)
        startActivity(intent)
    }

}