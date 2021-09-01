package com.example.newsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleList: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleItem: ArticleEntity)

    @Query("SELECT * FROM article ORDER BY id")
    fun allArticlesEntities():List<ArticleEntity>

    @Query("SELECT COUNT(*) FROM article")
    fun getCount(): Int
}
