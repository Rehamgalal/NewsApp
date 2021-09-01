package com.example.newsapp.ui

import com.example.newsapp.adapters.NewRecyclerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.db.ArticleEntity
import com.example.newsapp.utils.OnArticleLikedListener
import com.example.newsapp.utils.OnClickListener
import com.example.newsapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnClickListener, OnArticleLikedListener {

    private lateinit var recyclerAdapter: NewRecyclerAdapter
    private val viewModel: MainViewModel by viewModels()
    private var showingFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val searchView = item.actionView as SearchView
                searchView.queryHint = resources.getString(R.string.search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.setFilter(query)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.setFilter(newText)
                        return false
                    }

                })
            }
            R.id.like -> {
                if (showingFav) {
                    item.setIcon(R.drawable.like)
                    viewModel.setFilter("")
                    showingFav = false
                } else {
                    showingFav = true
                    item.setIcon(R.drawable.liked)
                    viewModel.getLikedArticles().observe(this, {
                        if (showingFav) {
                            recyclerAdapter.setDataList(it)
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    })
                }
            }
        }
        return true
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = NewRecyclerAdapter()
        recyclerAdapter.setListener(this, this)
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
        webView.visibility = View.VISIBLE
        webView.loadUrl(article.url)

    }

    override fun onArticleLiked(article: ArticleEntity) {
        viewModel.updateItem(article)
    }

    override fun onBackPressed() {
        if (webView.isVisible) {
            webView.visibility = View.GONE
        }
    }
}