package com.radea.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.radea.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao

    companion object {
        @Volatile
        private var instance: GithubUserDatabase? = null
        fun getInstance(context: Context): GithubUserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserDatabase::class.java, "GithubUser.db"
                ).build()
            }
    }
}