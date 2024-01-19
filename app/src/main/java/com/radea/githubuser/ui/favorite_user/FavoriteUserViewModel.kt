package com.radea.githubuser.ui.favorite_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radea.githubuser.data.FavoriteUserRepository
import com.radea.githubuser.data.local.entity.FavoriteUser
import kotlinx.coroutines.launch

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository): ViewModel() {
    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.insertFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.deleteFavoriteUser(favoriteUser)
        }
    }

    fun getFavoriteUserByUsername(username: String) : LiveData<FavoriteUser> {
        return favoriteUserRepository.getFavoriteByUsername(username)
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserRepository.getFavoriteUsers()
    }
}