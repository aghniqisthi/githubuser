package com.example.githubuser.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FavoriteViewModel (private val favRepository: FavRepository /*favoriteDao: FavoriteDao*/) : ViewModel() {

    fun getFavorites() = favRepository.getFavorites()

    fun saveFav(favorite: Favorite) {
        viewModelScope.launch {
            favRepository.setFavorite(favorite, 1)
        }
    }

    fun deleteFav(favorite: Favorite) {
        viewModelScope.launch {
            favRepository.setFavorite(favorite, 0)
        }
    }
}