package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.utils.NetworkState
import com.example.newsapp.utils.OnClickListener
import kotlinx.android.synthetic.main.article_item.view.*
import kotlinx.android.synthetic.main.networkstate_item.view.*

class NewRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private  var networkState:NetworkState?=null
    private  var articles: List<Article> ? =null
    private lateinit var listener: OnClickListener
     fun setDataList(articleList: List<Article>) {
        this.articles = articleList
    }

     fun setNetworkState(networkState: NetworkState) {
        this.networkState = networkState
    }

    fun setListener(listener: OnClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.article_item -> ArticleItemViewHolder.create(parent,listener)
            R.layout.networkstate_item -> NetworkStateViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.article_item -> (holder as ArticleItemViewHolder).bind(articles!![position])
            R.layout.networkstate_item -> (holder as NetworkStateViewHolder).bind(networkState!!)
        }
    }

    override fun getItemCount(): Int {
        return if(articles!=null) {
            articles!!.size
        } else {
            0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount-1) {
            R.layout.networkstate_item
        }else{
            R.layout.article_item
        }

    }

    private fun hasExtraRow(): Boolean{
        return networkState !=null && networkState != NetworkState.LOADED
    }
    class ArticleItemViewHolder(view: View, private val listener: OnClickListener): RecyclerView.ViewHolder(view) {
        fun bind(article:Article) {
            itemView.text_view.text = article.title
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder)
                .into(itemView.image_view)
            itemView.setOnClickListener{
                listener.onArticleCLicked(article)
            }
        }
        companion object {
            fun create(parent: ViewGroup, listener: OnClickListener): ArticleItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.article_item,parent,false)
                return ArticleItemViewHolder(view, listener)
            }
        }
    }

    class NetworkStateViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState) {
            if (networkState.message!=null) {
                itemView.error_txt.visibility = View.VISIBLE
                itemView.error_txt.text = networkState.message
            } else {
                itemView.error_txt.visibility = View.GONE
            }
        }

        companion object {
            fun create(parent: ViewGroup): NetworkStateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.networkstate_item, parent,false)
                return NetworkStateViewHolder(view)
            }
        }
    }
}