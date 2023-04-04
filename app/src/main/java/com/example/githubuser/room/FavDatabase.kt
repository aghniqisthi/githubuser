package com.example.githubuser.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 3, exportSchema = false)
abstract class FavDatabase : RoomDatabase() {

    abstract fun favDao (): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavDatabase {
            if (INSTANCE == null) {
                synchronized(FavDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavDatabase::class.java, "favorite_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as FavDatabase
        }
    }
}