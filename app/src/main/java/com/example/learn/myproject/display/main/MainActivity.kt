package com.example.learn.myproject.display.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.learn.myproject.R
import com.example.learn.myproject.adapter.OnItemClickCallback
import com.example.learn.myproject.adapter.UserAdapter
import com.example.learn.myproject.databinding.ActivityMainBinding
import com.example.learn.myproject.datasource.UserResponse
import com.example.learn.myproject.display.userDetail.UserDetailActivity
import com.example.learn.myproject.networking.NetworkConnection

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private val adapter: UserAdapter by lazy {
        UserAdapter()
    }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpSearchView()
        observeAnimationAndProgressBar()
        checkInternetConnection()
    }

    private fun setUpSearchView() {
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showProgressBar(true)
                    mainViewModel.getUserBySearch(query)
                    mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                        if (searchUserResponse != null) {
                            adapter.addDataToList(searchUserResponse)
                            setUserData()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

        }
    }

    private fun observeAnimationAndProgressBar() {
        mainViewModel.isLoading.observe(this) {
            showProgressBar(it)
        }
    }

    private fun checkInternetConnection() {
        mainViewModel.user.observe(this) { userResponse ->
            if (userResponse != null) {
                adapter.addDataToList(userResponse)
                setUserData()
            }
        }
        mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
            if (searchUserResponse != null) {
                adapter.addDataToList(searchUserResponse)
                binding.rvList.visibility = View.VISIBLE
            }
        }
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                mainViewModel.user.observe(this) { userResponse ->
                    if (userResponse != null) {
                        adapter.addDataToList(userResponse)
                        setUserData()
                    }
                }
                mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                    if (searchUserResponse != null) {
                        adapter.addDataToList(searchUserResponse)
                        binding.rvList.visibility = View.VISIBLE
                    }
                }
            } else {
                mainViewModel.user.observe(this) { userResponse ->
                    if (userResponse != null) {
                        adapter.addDataToList(userResponse)
                        setUserData()
                    }
                }
                mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                    if (searchUserResponse != null) {
                        adapter.addDataToList(searchUserResponse)
                        binding.rvList.visibility = View.VISIBLE
                    }
                }
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private fun hideUserList() {
        binding.rvList.layoutManager = null
        binding.rvList.adapter = null
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.animLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun setUserData() {
        val layoutManager =
            GridLayoutManager(this@MainActivity, 2, GridLayoutManager.VERTICAL, false)
        binding.rvList.layoutManager = layoutManager
        binding.rvList.setHasFixedSize(true)
        binding.rvList.adapter = adapter
        adapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                hideUserList()
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.KEY_USER, user)
                startActivity(intent)
            }
        })
    }
}
