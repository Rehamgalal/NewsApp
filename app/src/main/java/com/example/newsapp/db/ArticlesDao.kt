package com.example.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(articleList: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleItem: ArticleEntity)

    @Query("SELECT * FROM article ORDER BY publishedAt")
    fun allArticlesEntities(): List<ArticleEntity>

    @Query("SELECT * FROM article WHERE  title LIKE '%' || :word  || '%'  ORDER BY publishedAt")
    fun searchArticles(word:String): LiveData<List<ArticleEntity>>

    @Query("SELECT * FROM article WHERE  fav = 1  ORDER BY publishedAt")
    fun favArticlesEntities(): LiveData<List<ArticleEntity>>

    @Query("SELECT COUNT(*) FROM article")
    fun getCount(): Int


}
