package com.example.newsapp.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.model.Source
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "article")
data class ArticleEntity(
    val id: Long,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    @PrimaryKey val url: String,
    val urlToImage: String?,
    var fav:Int
    ): Parcelable