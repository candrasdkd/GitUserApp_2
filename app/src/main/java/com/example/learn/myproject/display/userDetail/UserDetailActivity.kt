package com.example.learn.myproject.display.userDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.learn.myproject.R
import com.example.learn.myproject.adapter.SectionPagerAdapter
import com.example.learn.myproject.databinding.ActivityUserDetailBinding
import com.example.learn.myproject.datasource.UserResponse
import com.example.learn.myproject.networking.NetworkConnection
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailDataLayout.visibility = View.GONE
        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        if (user != null) {
            user.login?.let {
                checkInternetConnection(it)
            }
        }
    }

    private fun checkInternetConnection(username: String) {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                showNoInternetAnimation(false)
                val detailViewModel: UserDetailViewModel by viewModels {
                    UserDetailViewModelFactory(username)
                }
                detailViewModel.isLoading.observe(this) {
                    showProgressBar(it)
                }
                detailViewModel.isNoInternet.observe(this) {
                    showNoInternetAnimation(it)
                }
                detailViewModel.isDataFailed.observe(this) {
                    showNoInternetAnimation(it)
                }
                detailViewModel.detailUser.observe(this@UserDetailActivity) { userResponse ->
                    if (userResponse != null) {
                        setData(userResponse)
                        setTabLayoutAdapter(userResponse)
                    }
                }
            } else {
                binding.detailDataLayout.visibility = View.GONE
                binding.detailAnimationLayout.visibility = View.VISIBLE
                showNoInternetAnimation(false)
            }
        }
    }

    private fun setTabLayoutAdapter(user: UserResponse) {
        val sectionPagerAdapter = SectionPagerAdapter(this@UserDetailActivity)
        sectionPagerAdapter.model = user
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setData(userResponse: UserResponse?) {
        if (userResponse != null) {
            with(binding) {
                detailDataLayout.visibility = View.VISIBLE
                detailImage.visibility = View.VISIBLE
                Glide.with(root)
                    .load(userResponse.avatarUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .circleCrop()
                    .into(binding.detailImage)
                detailName.visibility = View.VISIBLE
                detailUsername.visibility = View.VISIBLE
                detailName.text = userResponse.name
                detailUsername.text = userResponse.login
                if (userResponse.bio != null) {
                    detailBio.visibility = View.VISIBLE
                    detailBio.text = userResponse.bio
                } else {
                    detailBio.visibility = View.GONE
                }
                if (userResponse.company != null) {
                    detailCompany.visibility = View.VISIBLE
                    detailCompany.text = userResponse.company
                } else {
                    detailCompany.visibility = View.GONE
                }
                if (userResponse.location != null) {
                    detailLocation.visibility = View.VISIBLE
                    detailLocation.text = userResponse.location
                } else {
                    detailLocation.visibility = View.GONE
                }
                if (userResponse.blog != null) {
                    detailBlog.visibility = View.VISIBLE
                    detailBlog.text = userResponse.blog
                } else {
                    detailBlog.visibility = View.GONE
                }
                if (userResponse.followers != null) {
                    detailFollowersValue.visibility = View.VISIBLE
                    detailFollowersValue.text = userResponse.followers
                } else {
                    detailFollowersValue.visibility = View.GONE
                }
                if (userResponse.followers != null) {
                    detailFollowers.visibility = View.VISIBLE
                } else {
                    detailFollowers.visibility = View.GONE
                }
                if (userResponse.following != null) {
                    detailFollowingValue.visibility = View.VISIBLE
                    detailFollowingValue.text = userResponse.following
                } else {
                    detailFollowingValue.visibility = View.GONE
                }
                if (userResponse.following != null) {
                    detailFollowing.visibility = View.VISIBLE
                } else {
                    detailFollowing.visibility = View.GONE
                }
                if (userResponse.publicRepo != null) {
                    detailRepoValue.visibility = View.VISIBLE
                    detailRepoValue.text = userResponse.publicRepo
                } else {
                    detailRepoValue.visibility = View.GONE
                }
                if (userResponse.publicRepo != null) {
                    detailRepo.visibility = View.VISIBLE
                } else {
                    detailRepo.visibility = View.GONE
                }
            }
        } else {
            Log.i(TAG, "setData fun is error")
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.detailLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoInternetAnimation(isNoInternet: Boolean) {
        binding.detailNoInternet.visibility = if (isNoInternet) View.VISIBLE else View.GONE
    }


    companion object {
        const val KEY_USER = "user"
        private const val TAG = "UserDetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}