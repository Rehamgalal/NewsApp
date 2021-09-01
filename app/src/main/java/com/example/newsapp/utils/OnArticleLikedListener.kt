package com.example.newsapp.utils

import com.example.newsapp.db.ArticleEntity

interface OnArticleLikedListener {
    fun onArticleLiked(article: ArticleEntity)
}