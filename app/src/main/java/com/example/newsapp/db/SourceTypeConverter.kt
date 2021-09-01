package com.example.newsapp.db

import androidx.room.TypeConverter
import com.example.newsapp.model.Source
import org.json.JSONObject

class SourceTypeConverter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return JSONObject().apply {
            put("id", source.id)
            put("name", source.name)
        }.toString()
    }

    @TypeConverter
    fun toSource(source: String): Source {
        val json = JSONObject(source)
        return Source("", json.getString("name"))
    }
}