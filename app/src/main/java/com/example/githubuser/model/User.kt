package com.example.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:Int,
    val avatar_url: String,
    val username:String,
    val name : String,
    val public_repos:Int,
    val followers:Int,
    val following:Int,
) : Parcelable
