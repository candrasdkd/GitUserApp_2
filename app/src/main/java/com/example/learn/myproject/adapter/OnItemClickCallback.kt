package com.example.learn.myproject.adapter

import com.example.learn.myproject.datasource.UserResponse

interface OnItemClickCallback {
    fun onItemClicked(user: UserResponse)
}