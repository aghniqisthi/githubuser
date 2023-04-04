package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.room.Favorite
import com.example.githubuser.ui.main.DetailActivity
import com.example.githubuser.ui.main.MainActivity

class ListFavoriteAdapter(private val listFav: List<Favorite>) : ListAdapter<Favorite, ListFavoriteAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtUsernameList.text = listFav[position].username
        holder.binding.txtNameList.text = listFav[position].reposUrl
        Glide.with(holder.itemView.context)
            .load(listFav[position].avatar_url)
//                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
            .into(holder.binding.imgUserList)
        holder.itemView.setOnClickListener {
            val moveDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveDetail.putExtra(MainActivity.ARG_USERNAME, listFav[position].username)
            holder.itemView.context.startActivity(moveDetail)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Favorite> =
            object : DiffUtil.ItemCallback<Favorite>() {
                override fun areItemsTheSame(oldUser: Favorite, newUser: Favorite): Boolean {
                    return oldUser.username == newUser.username
                }

                override fun areContentsTheSame(oldUser: Favorite, newUser: Favorite): Boolean {
                    return oldUser == newUser
                }
            }
    }

    override fun getItemCount(): Int = listFav.size
}