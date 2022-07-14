package com.example.learn.myproject.display.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserDetailViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}