package com.caavo.myrecipe.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    private val gson = Gson()

//    @TypeConverter
//    fun stringToList(data: String?): List<BuiltBy> {
//        if (data == null) {
//            return Collections.emptyList()
//        }
//        val listType = object : TypeToken<List<BuiltBy>>() {}.type
//        return gson.fromJson(data, listType)
//    }
//
//    @TypeConverter
//    fun listToString(someObjects: List<BuiltBy>): String {
//        return gson.toJson(someObjects)
//    }
}