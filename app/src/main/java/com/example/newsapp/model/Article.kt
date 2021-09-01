package com.example.newsapp.model

import android.os.Parcelable
import com.example.newsapp.db.ArticleEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val author: String? ,
    val content: String? ,
    val description: String? ,
    val publishedAt: String? ,
    val source: Source?,
    val title: String? ,
    val url: String? ,
    val urlToImage: String?
):Parcelable
{
    companion object {
        operator fun invoke(
             author: String? =null ,
             content: String? =null,
             description: String ? =null,
             publishedAt: String ? =null,
             source: Source?=null,
             title: String ? =null,
             url: String ? =null,
             urlToImage: String ? =null
        ) {Article(
            author ?: "" ,
         content ?:"",
         description ?:"",
         publishedAt ?:"",
            source ?: Source("",""),
         title ?:"",
         url ?:"",
         urlToImage ?:""
        )}
    }
}
fun Article.toArticleEntity() =
    ArticleEntity(0,author, content,description,publishedAt,source,title,url!!,urlToImage)
