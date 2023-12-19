package com.radea.githubuser.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radea.githubuser.data.response.ResponseDetailsUser
import com.radea.githubuser.data.response.ResponseUsersItem
import com.radea.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailUser = MutableLiveData<ResponseDetailsUser>()
    val detailUser: LiveData<ResponseDetailsUser> = _detailUser

    private val _isDetailLoading = MutableLiveData<Boolean>()
    val isDetailLoading: LiveData<Boolean> = _isDetailLoading

    private val _isDetailError = MutableLiveData<String>()
    val isDetailError: LiveData<String> = _isDetailError

    private val _followers = MutableLiveData<List<ResponseUsersItem>>()
    val followers: LiveData<List<ResponseUsersItem>> = _followers

    private val _isFollowerLoading = MutableLiveData<Boolean>()
    val isFollowerLoading: LiveData<Boolean> = _isFollowerLoading

    private val _isFollowerError = MutableLiveData<String>()
    val isFollowerError: LiveData<String> = _isFollowerError

    private val _following = MutableLiveData<List<ResponseUsersItem>>()
    val following: LiveData<List<ResponseUsersItem>> = _following

    private val _isFollowingLoading = MutableLiveData<Boolean>()
    val isFollowingLoading: LiveData<Boolean> = _isFollowingLoading

    private val _isFollowingError = MutableLiveData<String>()
    val isFollowingError: LiveData<String> = _isFollowingError

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }

    fun findDetailUser(username: String) {
        _isDetailLoading.value = true
        val client = ApiConfig.getApiService().getDetailsByUsername(username)
        client.enqueue(object : Callback<ResponseDetailsUser> {
            override fun onResponse(
                call: Call<ResponseDetailsUser>,
                response: Response<ResponseDetailsUser>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    Log.i(TAG, response.body().toString())
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseDetailsUser>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isDetailLoading.value = false
                _isDetailError.value = t.message
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isFollowerLoading.value = true
        val client = ApiConfig.getApiService().getFollowersByUsername(username)
        client.enqueue(object : Callback<List<ResponseUsersItem>> {
            override fun onResponse(
                call: Call<List<ResponseUsersItem>>,
                response: Response<List<ResponseUsersItem>>
            ) {
                _isFollowerLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.i(TAG, responseBody.toString())
                    if ((responseBody?.size ?: 0) < 1) {
                        _isFollowerError.value = "$username doesn't exist followers"
                    }
                    _followers.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ResponseUsersItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isFollowerLoading.value = false
                _isFollowerError.value = t.message
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isFollowingLoading.value = true
        val client = ApiConfig.getApiService().getFollowingByUsername(username)
        client.enqueue(object : Callback<List<ResponseUsersItem>> {
            override fun onResponse(
                call: Call<List<ResponseUsersItem>>,
                response: Response<List<ResponseUsersItem>>
            ) {
                _isFollowingLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.i(TAG, responseBody.toString())
                    if ((responseBody?.size ?: 0) < 1) {
                        _isFollowingError.value = "$username doesn't exist following"
                    }
                    _following.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ResponseUsersItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isFollowingLoading.value = false
                _isFollowingError.value = t.message
            }
        })
    }


}