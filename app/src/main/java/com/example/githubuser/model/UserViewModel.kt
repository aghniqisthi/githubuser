package com.example.githubuser.model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ResponseDataUserItem>>()
    val listUser: LiveData<List<ResponseDataUserItem>> = _listUser

    private val _user = MutableLiveData<ResponseDataDetail>()
    val user: LiveData<ResponseDataDetail> = _user

    private val _listSearch = MutableLiveData<List<ItemSearch>>()
    val listSearch: LiveData<List<ItemSearch>> = _listSearch

    private val _listRepo = MutableLiveData<List<ResponseDataReposItem>>()
    val listRepo: LiveData<List<ResponseDataReposItem>> = _listRepo

    private val _listFollowers = MutableLiveData<List<ResponseDataUserItem>>()
    val listFollowers: LiveData<List<ResponseDataUserItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ResponseDataUserItem>>()
    val listFollowing: LiveData<List<ResponseDataUserItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init{
        getUser()
    }

    private fun getUser() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListUsers()
        client.enqueue(object : Callback<List<ResponseDataUserItem>> {
            override fun onResponse(call: Call<List<ResponseDataUserItem>>, response: Response<List<ResponseDataUserItem>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listUser.postValue(response.body())
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    //menampilkan toast error ke user
                }
            }

            override fun onFailure(call: Call<List<ResponseDataUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailUser(username:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<ResponseDataDetail> {
            override fun onResponse(call: Call<ResponseDataDetail>, response: Response<ResponseDataDetail>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _user.postValue(response.body())
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDataDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getRepos(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailRepo(username)
        client.enqueue(object : Callback<List<ResponseDataReposItem>> {
            override fun onResponse(call: Call<List<ResponseDataReposItem>>, response: Response<List<ResponseDataReposItem>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listRepo.postValue(response.body())
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseDataReposItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailFollowers(username)
        client.enqueue(object : Callback<List<ResponseDataUserItem>> {
            override fun onResponse(call: Call<List<ResponseDataUserItem>>, response: Response<List<ResponseDataUserItem>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listFollowers.postValue(response.body())
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseDataUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailFollowing(username)
        client.enqueue(object : Callback<List<ResponseDataUserItem>> {
            override fun onResponse(call: Call<List<ResponseDataUserItem>>, response: Response<List<ResponseDataUserItem>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseDataUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(query:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object : Callback<ResponseDataSearch> {
            override fun onResponse(call: Call<ResponseDataSearch>, response: Response<ResponseDataSearch>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listSearch.postValue(response.body()?.items)
                }
                else {
                    Log.e(TAG, "Failure onResponse: $response")
                }
            }

            override fun onFailure(call: Call<ResponseDataSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: $t")
            }
        })
    }
}
