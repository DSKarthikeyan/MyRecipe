package com.caavo.myrecipe.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caavo.myrecipe.data.model.RecipeDetails

@Database(
    entities = [RecipeDetails::class],
    version = 1, exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun getRecipeDetailsDAO(): RecipeDAO

    companion object {
        @Volatile
        private var instance: RecipeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipes.db"
            ).build()
    }


}