package com.example.githubuser.room

import androidx.lifecycle.*
import com.example.githubuser.model.ApiService

class FavRepository private constructor(private val apiService: ApiService, private val favDao: FavoriteDao) {

    fun getFavorites(): LiveData<List<Favorite>> {
        return favDao.getFavorite()
    }

    suspend fun setFavorite(favs: Favorite, isFav: Int) {
        favs.is_favorite = isFav
        favDao.updateFavs(favs)
    }

    companion object {
        @Volatile
        private var instance: FavRepository? = null
        fun getInstance(
            apiService: ApiService,
            favsDao: FavoriteDao
        ): FavRepository =
            instance ?: synchronized(this) {
                instance ?: FavRepository(apiService, favsDao)
            }.also { instance = it }
    }

/*

private val favoriteDao: FavoriteDao
priva val executorService: ExecutorService = Executors.newSingleThreadExecutor()

init {
    val db = FavDatabase.getInstance(context)
    favoriteDao = db.favDao()
}

fun getAllFavorite(username: String): LiveData<Result<List<Favorite>>> = liveData {
    emit(Result.Loading)
    try {
        val response = apiService.getDetailUsers(username).execute().body()
        val favList = response!!.login.map {
            val isFav = favDao.isFavorite(response!!.login)
            if(response.name != null) Favorite(0, response.avatarUrl, response.login, response.name.toString(), response.publicRepos, response.followers, response.following, isFav)
            else Favorite(0, response.avatarUrl, response.login, "", response.publicRepos, response.followers, response.following, isFav)
        }
        favDao.deleteAll()
        favDao.insert(favList)
    } catch (e: Exception) {
        Log.d("FavRepository", "getData: ${e.message.toString()} ")
        emit(Result.Error(e.message.toString()))
    }
    val localData: LiveData<Result<List<Favorite>>> = favDao.getAllFav().map { Result.Success(it) }
    emitSource(localData)
}

fun getFav(username: String): LiveData<Favorite> = favDao.getFav(username)

    fun getAllFav(): LiveData<List<Favorite>> = favDao.getAllFav()

suspend fun stateFav(username: String): Boolean = favDao.isFavorite(username)

suspend fun setFav(favorite: Favorite, favState: Boolean) {
    favorite.is_favorite = favState
    favDao.update(favorite)
    Log.e("TAG", "setfav repo username: ${favorite.username} state: ${favorite.is_favorite}")
}

    suspend fun setFav(username: String, favState: Boolean) {
        favDao.update(username, favState)
    favDao.setFavorite(username, favState)
    }

    companion object {
    @Volatile
    private var instance: FavRepository? = null
    fun getInstance(apiService: ApiService, newsDao: FavoriteDao): FavRepository =
        instance ?: synchronized(this) {
            instance ?: FavRepository(apiService, newsDao)
        }.also { instance = it }
}

suspend fun cekFav(username : String) = favoriteDao.checkFav(username)

fun insertFav(favorite: Favorite) {
    executorService.execute { favoriteDao.insert(favorite) }
}

fun deleteFav(favorite: Favorite) {
    executorService.execute { favoriteDao.delete(favorite) }
}

 */
}