package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.ui.main.DetailActivity
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.model.ResponseDataUserItem

class ListFollsAdapter (private val listFolls: List<ResponseDataUserItem>) : RecyclerView.Adapter<ListFollsAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.txtUsernameList.text = listFolls[position].login
        holder.binding.txtNameList.text = listFolls[position].reposUrl
        Glide.with(holder.itemView.context).load(listFolls[position].avatarUrl)
            .into(holder.binding.imgUserList)

        holder.binding.cardUserList.setOnClickListener {
            val moveDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveDetail.putExtra(MainActivity.ARG_USERNAME, listFolls[position].login)
            holder.itemView.context.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = listFolls.size
}