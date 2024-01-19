package com.radea.githubuser.data

import androidx.lifecycle.LiveData
import com.radea.githubuser.data.local.entity.FavoriteUser
import com.radea.githubuser.data.local.room.GithubUserDao

class FavoriteUserRepository(
    private val githubUserDao: GithubUserDao
) {
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return githubUserDao.getFavoriteUsers()
    }
    fun getFavoriteByUsername(username: String): LiveData<FavoriteUser> {
        return githubUserDao.getFavoriteUserByUsername(username)
    }

    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        return githubUserDao.insertFavoriteUser(favoriteUser)
    }

    suspend fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        return githubUserDao.deleteFavoriteUser(favoriteUser)
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null
        fun getInstance(
            githubUserDao: GithubUserDao,
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(githubUserDao)
            }.also { instance = it }
    }
}