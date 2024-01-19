package com.radea.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.radea.githubuser.data.local.entity.FavoriteUser

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser)

    @Delete
    suspend fun deleteFavoriteUser(favoriteUser: FavoriteUser)
}