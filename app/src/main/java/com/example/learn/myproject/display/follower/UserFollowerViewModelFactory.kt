package com.example.learn.myproject.display.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserFollowerViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFollowerViewModel::class.java)) {
            return UserFollowerViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}