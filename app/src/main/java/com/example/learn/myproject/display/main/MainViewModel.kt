package com.example.learn.myproject.display.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn.myproject.datasource.SearchResponse
import com.example.learn.myproject.datasource.UserResponse
import com.example.learn.myproject.networking.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class MainViewModel : ViewModel() {
    private val _user = MutableLiveData<ArrayList<UserResponse>?>()
    val user: LiveData<ArrayList<UserResponse>?> = _user
    private val _searchUser = MutableLiveData<ArrayList<UserResponse>?>()
    val searchUser: LiveData<ArrayList<UserResponse>?> = _searchUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        viewModelScope.launch { getListUser() }
        Log.i(TAG, "MainViewModel is Created")
    }

    private suspend fun getListUser() {
        coroutineScope.launch {
            _isLoading.value = true
            val getUserDeferred = ApiConfig.getApiService()?.getListUsersAsync()
            try {
                _isLoading.value = false
                _user.postValue(getUserDeferred)
            } catch (e: Error) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message.toString()}")
            }   finally {
                Log.d("finally", "clear all resources")
            }
        }
    }


    fun getUserBySearch(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()?.getUserBySearch(user)
        client?.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.items != null) {
                            _searchUser.postValue(responseBody.items)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
