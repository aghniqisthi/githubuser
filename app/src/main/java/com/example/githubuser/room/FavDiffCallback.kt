package com.example.githubuser.room

import androidx.recyclerview.widget.DiffUtil

class FavDiffCallback(private val oldFavList: List<Favorite>, private val newFavList: List<Favorite>) : DiffUtil.Callback()
{
    override fun getOldListSize(): Int  = oldFavList.size

    override fun getNewListSize(): Int = newFavList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldFavList[oldItemPosition].username == newFavList[newItemPosition].username

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFav = oldFavList[oldItemPosition]
        val newFav = newFavList[newItemPosition]
        return oldFav.avatar_url == newFav.avatar_url && oldFav.reposUrl == newFav.reposUrl
    }
}