package com.example.githubuser.room

import android.content.Context
import com.example.githubuser.model.ApiConfig

object Injection {
    fun provideRepository(context: Context): FavRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavDatabase.getInstance(context)
        val dao = database.favDao()
        return FavRepository.getInstance(apiService, dao)
    }
}