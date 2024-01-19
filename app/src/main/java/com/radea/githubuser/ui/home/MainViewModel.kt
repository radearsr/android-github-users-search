package com.radea.githubuser.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.radea.githubuser.data.remote.response.ResponseUsersItem
import com.radea.githubuser.data.remote.response.ResponseUsersSearch
import com.radea.githubuser.data.remote.retrofit.ApiConfig
import com.radea.githubuser.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _listUser = MutableLiveData<List<ResponseUsersItem>>()
    val listUser: LiveData<List<ResponseUsersItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    fun findGithubUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<ResponseUsersItem>> {
            override fun onResponse(call: Call<List<ResponseUsersItem>>, response: Response<List<ResponseUsersItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                    Log.i(TAG, response.body().toString())
                }
            }

            override fun onFailure(call: Call<List<ResponseUsersItem>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findGithubUserByUsername(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersByUsername(username)
        client.enqueue(object : Callback<ResponseUsersSearch> {
            override fun onResponse(
                call: Call<ResponseUsersSearch>,
                response: Response<ResponseUsersSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.i(TAG, responseBody.toString())
                    if ((responseBody?.totalCount ?: 0) < 1) {
                        _isError.value = "Username Not Found"
                    }
                    _listUser.value = response.body()?.items
                }
            }
            override fun onFailure(call: Call<ResponseUsersSearch>, t: Throwable) {
                _isLoading.value = false
                _isError.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}