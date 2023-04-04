package com.example.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.model.UserViewModel
import com.example.githubuser.adapter.ListSearchAdapter
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.datastore.PreferenceViewModel
import com.example.githubuser.datastore.SettingPreferences
import com.example.githubuser.datastore.ViewModelFactory
import com.example.githubuser.model.ItemSearch
import com.example.githubuser.model.ResponseDataUserItem

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    companion object{
        const val ARG_USERNAME = "USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getThemeSettings().observe(this) { darkActive : Boolean ->
            if (!darkActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        setData()

    }

    private fun setData(){
        val userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this, 2)
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this, 4)
            else -> GridLayoutManager(this, 2)
        }

        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        userVM.listUser.observe(this) { user ->
            setListUser(user)
        }

        userVM.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setListUser(user: List<ResponseDataUserItem>) {
        val listData = user.map{it}
        val adapter = ListUserAdapter(listData)
        binding.rvUser.adapter = adapter
    }

    private fun setListSearch(result: List<ItemSearch>) {
        val listData = result.map{it}
        val adapter = ListSearchAdapter(listData)
        adapter.changeData(listData)
        binding.rvUser.adapter = adapter
    }

    private lateinit var searchManager : SearchManager
    private lateinit var searchView : SearchView
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val pref = SettingPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getThemeSettings().observe(this) { darkActive : Boolean ->
            darkmode = if (!darkActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false
            }
//            switchTheme.isChecked = darkActive
        }

        return true
    }

    private var darkmode = true

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.enter_username)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    val userVM = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

                    override fun onQueryTextSubmit(query: String): Boolean {
                        val layoutManager = when (resources.configuration.orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this@MainActivity, 2)
                            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this@MainActivity, 4)
                            else -> GridLayoutManager(this@MainActivity, 2)
                        }

                        binding.rvUser.layoutManager = layoutManager

                        val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
                        binding.rvUser.addItemDecoration(itemDecoration)

                        userVM.searchUser(query)
                        userVM.listSearch.observe(this@MainActivity) { user ->
                            if(user.isEmpty()) Toast.makeText(this@MainActivity, R.string.no_user, Toast.LENGTH_SHORT).show()
                            else setListSearch(user)
                        }

                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })

            }
            R.id.dark_mode ->{
                val pref = SettingPreferences.getInstance(dataStore)
                val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

                if(!darkmode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefVM.saveThemeSetting(darkmode)
            }
            R.id.favorites ->{
                val moveDetail = Intent(this, FavoriteActivity::class.java)
                this.startActivity(moveDetail)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

