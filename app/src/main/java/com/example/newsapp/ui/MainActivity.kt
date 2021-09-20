package com.example.newsapp.ui

import com.example.newsapp.adapters.NewRecyclerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
        val  searchItem = menu?.findItem(R.id.search)
        val tv =  TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        val searchView =  searchItem?.actionView as SearchView
        val params =  LinearLayout.LayoutParams(actionBarHeight*2/3, actionBarHeight *2/3)
        params.marginEnd = 20
        val button =  Button(this)
        button.setBackgroundResource(R.drawable.like)
        (searchView.getChildAt(0) as LinearLayout).addView(button,params)
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
        button.setOnClickListener {
        if (showingFav) {
            button.setBackgroundResource(R.drawable.like)
            viewModel.setFilter("")
            showingFav = false
        } else {
            showingFav = true
            button.setBackgroundResource(R.drawable.liked)
            viewModel.getLikedArticles().observe(this, {
                if (showingFav) {
                    recyclerAdapter.setDataList(it)
                    recyclerAdapter.notifyDataSetChanged()
                }
            })
        }}

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