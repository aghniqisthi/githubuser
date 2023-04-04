package com.example.githubuser.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class Favorite(
    @field:ColumnInfo(name = "username")
    @PrimaryKey(autoGenerate = false)
    var username: String = "",

    @field:ColumnInfo(name = "avatar_url")
    var avatar_url: String = "",

    @field:ColumnInfo(name = "repos_url")
    var reposUrl: String = "",

    @field:ColumnInfo(name = "is_favorite")
    var is_favorite: Int
)
