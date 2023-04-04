package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.model.ResponseDataReposItem

class ListReposAdapter (private val listRepo: List<ResponseDataReposItem>) : RecyclerView.Adapter<ListReposAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.txtUsernameList.text = listRepo[position].name
        holder.binding.txtNameList.text = listRepo[position].description
        holder.binding.imgUserList.visibility = View.GONE
    }

    override fun getItemCount(): Int = listRepo.size
}