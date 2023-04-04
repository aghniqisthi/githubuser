package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.ui.main.DetailActivity
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.model.ItemSearch

class ListSearchAdapter(private val listSearch: List<ItemSearch>) : RecyclerView.Adapter<ListSearchAdapter.ListViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<ItemSearch>(){
        override fun areItemsTheSame(oldItem: ItemSearch, newItem: ItemSearch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemSearch, newItem: ItemSearch): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun changeData(value: List<ItemSearch>){
        differ.submitList(value)
    }

    class ListViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.binding.txtUsernameList.text = result.login
        holder.binding.txtNameList.text = result.reposUrl
        Glide.with(holder.itemView.context).load(result.avatarUrl).into(holder.binding.imgUserList)

        holder.binding.cardUserList.setOnClickListener{
            val moveDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveDetail.putExtra(MainActivity.ARG_USERNAME, listSearch[position].login)
            holder.itemView.context.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}