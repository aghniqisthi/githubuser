package com.example.githubuser.ui.main

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListFavoriteAdapter
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.room.Favorite
import com.example.githubuser.room.FavoriteViewModel
import com.example.githubuser.room.ViewModelFactory
import com.example.githubuser.room.FavDatabase

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val favVM: FavoriteViewModel by viewModels { factory }

        //set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = "Favorite User"

        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this, 2)
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this, 4)
            else -> GridLayoutManager(this, 2)
        }

        binding!!.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding!!.rvFavorite.addItemDecoration(itemDecoration)

        val databaseFavorite = FavDatabase.getInstance(this)
        binding!!.rvFavorite.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        val listFavorite = databaseFavorite.favDao().getFavorite()
        listFavorite.let {
            databaseFavorite.favDao().getFavorite().observe(this@FavoriteActivity){
                runOnUiThread {
                    binding!!.rvFavorite.adapter = ListFavoriteAdapter(it)
                }
            }
        }

        favVM.getFavorites().observe(this) {
            binding?.progressBar?.visibility = View.GONE
            setListFav(it)
        }
    }

    private fun setListFav(fav: List<Favorite>) {
        val listData = fav.map{it}
        if(listData.isEmpty()) Toast.makeText(this, "No Favorite Data", Toast.LENGTH_LONG).show()
        else {
            val adapter = ListFavoriteAdapter(listData)
            adapter.submitList(fav)
            binding!!.rvFavorite.adapter = adapter
        }
    }
}