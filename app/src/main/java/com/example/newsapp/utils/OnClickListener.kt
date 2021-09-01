package com.example.newsapp.utils

import com.example.newsapp.db.ArticleEntity

interface OnClickListener {
    fun onArticleCLicked(article: ArticleEntity)
}