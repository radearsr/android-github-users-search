package com.radea.githubuser.di

import android.content.Context
import com.radea.githubuser.data.FavoriteUserRepository
import com.radea.githubuser.data.local.room.GithubUserDatabase

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val database = GithubUserDatabase.getInstance(context)
        val dao = database.githubUserDao()
        return FavoriteUserRepository.getInstance(dao)
    }
}