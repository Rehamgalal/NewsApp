package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.other.Constants.DATABASE_NAME
import com.example.newsapp.other.Constants.DATABASE_VERSION

@Database(entities = [ArticleEntity::class], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(SourceTypeConverter::class)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao

    companion object {
        fun buildDatabase(context: Context): ArticlesDatabase {
            return Room.databaseBuilder(context, ArticlesDatabase::class.java, DATABASE_NAME)
                    .build()
        }
    }
}