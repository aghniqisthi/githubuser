package com.example.githubuser.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorite where is_favorite = 1")
    fun getFavorite(): LiveData<List<Favorite>>

    @Insert
    fun insertFavs(fav: Favorite) : Long

    @Delete
    fun deleteFavs(fav: Favorite)

    @Update
    suspend fun updateFavs(fav: Favorite)

    @Query("SELECT is_favorite FROM Favorite WHERE username = :username")
    suspend fun isFavorited(username: String): Int
}