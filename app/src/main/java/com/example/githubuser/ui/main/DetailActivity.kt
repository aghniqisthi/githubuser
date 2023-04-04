package com.example.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.ResponseDataDetail
import com.example.githubuser.model.UserViewModel
import com.example.githubuser.room.FavDatabase
import com.example.githubuser.room.Favorite
import com.example.githubuser.room.FavoriteViewModel
import com.example.githubuser.room.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_0,
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewpager followers following repository
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.tabs.setTabTextColors(R.color.white, R.color.blue)

        //set action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = ""

        //data detail
        val data = intent.getStringExtra(MainActivity.ARG_USERNAME)
        detailData(data!!)

        sectionsPagerAdapter.username = data

        val favVM : FavoriteViewModel by viewModels { ViewModelFactory.getInstance(this) }
        val userVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]

        val database = FavDatabase.getInstance(this)
        val dao = database.favDao()

        data.let {
            showLoading(true)
            userVM.getDetailUser(data)
            userVM.user.observe(this) { user ->
                showSelectedItem(user)
            }
        }

        var isFavorited : Int? = 0

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                isFavorited = dao.isFavorited(data)

                runOnUiThread {
                    if(isFavorited == 1) binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                    else if(isFavorited == 0 || isFavorited == null) binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        }

        binding.btnFav.setOnClickListener {
            userVM.user.observe(this) { user ->
                GlobalScope.async {
                    if (isFavorited == 1) {
                        val favorites = Favorite(user.login, user.avatarUrl, user.reposUrl, 0)
                        favVM.deleteFav(favorites)
                        dao.deleteFavs(favorites)

                        runOnUiThread {
                            Toast.makeText(this@DetailActivity, "Favorite Deleted", Toast.LENGTH_SHORT).show()
                            Log.e("$favorites", "deleted from favorite")
                            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }
                    }
                    else if (isFavorited == 0 || isFavorited == null) {
                        val datafav = Favorite(user.login, user.avatarUrl, user.reposUrl, 1)
                        val favorites = dao.insertFavs(datafav)
                        favVM.saveFav(datafav)

                        if (favorites != 0L) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@DetailActivity,
                                        "Favorite Added",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e("$favorites", "added to favorite")
                                    binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                                }
                            }
                            else Toast.makeText(this@DetailActivity, "Failed added Favorite", Toast.LENGTH_SHORT).show()
                    }
                }
                showLoading(false)
            }
            val moveDetail = Intent(this, FavoriteActivity::class.java)
            this.startActivity(moveDetail)
        }
    }

    private fun saveData(){

    }

    private fun detailData(username:String){
        val userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

        userVM.getDetailUser(username)
        userVM.user.observe(this) { user ->
            showSelectedItem(user)
        }
        userVM.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(load:Boolean) {
        binding.progressBar.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun showSelectedItem(data: ResponseDataDetail) {
        binding.apply {
            txtUsernameDetail.text = data.login
            numbRepositoryDetail.text = "${data.publicRepos} Repository"
            numbFollowerDetail.text = "${data.followers} Followers"
            numbFollowingDetail.text = "${data.following} Following"
        }
        Glide.with(this).load(data.avatarUrl).into(binding.imageView)

        //data name has a possibility of null since it is not a required data
        if (data.name != null ) binding.txtNameDetail.text = data.name.toString()
        else binding.txtNameDetail.text = ""
    }
}
