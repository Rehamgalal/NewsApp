package com.example.newsapp.utils

import com.example.newsapp.model.Article

interface OnClickListener {
    fun onArticleCLicked(article: Article)
}