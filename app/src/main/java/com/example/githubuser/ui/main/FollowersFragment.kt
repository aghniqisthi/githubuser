package com.example.githubuser.ui.main

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListReposAdapter
import com.example.githubuser.model.UserViewModel
import com.example.githubuser.adapter.ListFollsAdapter
import com.example.githubuser.databinding.FragmentFollowersBinding
import com.example.githubuser.model.ResponseDataReposItem
import com.example.githubuser.model.ResponseDataUserItem

class FollowersFragment : Fragment() {

    private lateinit var binding : FragmentFollowersBinding

    companion object{
        var position : Int = 0
        var username : String = ""
        const val ARG_USERNAME = "USERNAME"
        const val ARG_POSITION = "POSITION"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username = ""

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(context, 2)
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(context, 4)
            else -> GridLayoutManager(context, 2)
        }

        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        when(position){
            1 ->{
                userVM.getRepos(username)
                userVM.listRepo.observe(viewLifecycleOwner) { repo ->
                    if(repo.isEmpty()) Toast.makeText(context, R.string.no_repo, Toast.LENGTH_SHORT).show()
                    else setListRepo(repo)
                }
            }
            2 ->{
                userVM.getFollowers(username)
                userVM.listFollowers.observe(viewLifecycleOwner) { user ->
                    if(user.isEmpty()) Toast.makeText(context, R.string.no_followers, Toast.LENGTH_SHORT).show()
                    else setListFolls(user)
                }
            }
            3 -> {
                userVM.getFollowing(username)
                userVM.listFollowing.observe(viewLifecycleOwner) { user ->
                    if(user.isEmpty()) Toast.makeText(context, R.string.no_following, Toast.LENGTH_SHORT).show()
                    else setListFolls(user)
                }
            }
        }

        userVM.isLoading.observe(viewLifecycleOwner) {
            loadingStatus(it)
        }
    }

    private fun loadingStatus(load:Boolean) {
        binding.progressBar.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun setListRepo(user: List<ResponseDataReposItem>) {
        val listData = user.map{it}
        val adapter = ListReposAdapter(listData)
        binding.rvFollowers.adapter = adapter
    }

    private fun setListFolls(user: List<ResponseDataUserItem>) {
        val listData = user.map{it}
        val adapter = ListFollsAdapter(listData)
        binding.rvFollowers.adapter = adapter
    }
}