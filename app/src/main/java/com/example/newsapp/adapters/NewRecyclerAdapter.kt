package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.db.ArticleEntity
import com.example.newsapp.utils.NetworkState
import com.example.newsapp.utils.OnArticleLikedListener
import com.example.newsapp.utils.OnClickListener
import com.example.newsapp.utils.Status
import kotlinx.android.synthetic.main.article_item.view.*
import kotlinx.android.synthetic.main.networkstate_item.view.*

class NewRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var networkState: NetworkState? = null
    private var articles: List<ArticleEntity>? = null
    private lateinit var listener: OnClickListener
    private lateinit var likeListener: OnArticleLikedListener
    fun setDataList(articleList: List<ArticleEntity>) {
        this.articles = articleList
    }

    fun setNetworkState(networkState: NetworkState) {
        this.networkState = networkState
    }

    fun setListener(listener: OnClickListener, listenerLike: OnArticleLikedListener) {
        this.listener = listener
        this.likeListener = listenerLike
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.article_item -> ArticleItemViewHolder.create(parent, listener,likeListener)
            R.layout.networkstate_item -> NetworkStateViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.article_item -> (holder as ArticleItemViewHolder).bind(articles!![position])
            R.layout.networkstate_item -> (holder as NetworkStateViewHolder).bind(networkState!!)
        }
    }

    override fun getItemCount(): Int {
        return when {
            articles != null -> {
                articles!!.size
            }
            networkState != null -> {
                1
            }
            else -> {
                0
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.networkstate_item
        } else {
            R.layout.article_item
        }

    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    class ArticleItemViewHolder(view: View, private val listener: OnClickListener, private val likeListener: OnArticleLikedListener) :
            RecyclerView.ViewHolder(view) {
        fun bind(article: ArticleEntity) {

            itemView.title.text = article.title
            Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.placeholder)
                    .into(itemView.image_view)
            itemView.description.text = article.description
            if (article.publishedAt!=null) {
                itemView.date.text = article.publishedAt.replace("T", itemView.context.resources.getString(R.string.at)).replace("Z", "")
            }
            itemView.source.text = article.source?.name
            itemView.setOnClickListener {
                listener.onArticleCLicked(article)
            }
            itemView.like.setOnClickListener {
                likeListener.onArticleLiked(article)
                if (article.fav == 0) {
                    article.fav = 1
                    itemView.like.setImageResource(R.drawable.liked)
                } else {
                    article.fav = 0
                    itemView.like.setImageResource(R.drawable.like)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: OnClickListener, likeListenr: OnArticleLikedListener): ArticleItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.article_item, parent, false)
                return ArticleItemViewHolder(view, listener,likeListenr)
            }
        }
    }

    class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState) {
            itemView.progress_bar.isVisible = networkState.status == Status.LOADING
            if (networkState.message != null) {
                itemView.error_txt.visibility = View.VISIBLE
                itemView.error_txt.text = networkState.message
            } else {
                itemView.error_txt.visibility = View.GONE
            }
        }

        companion object {
            fun create(parent: ViewGroup): NetworkStateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.networkstate_item, parent, false)
                return NetworkStateViewHolder(view)
            }
        }
    }


}