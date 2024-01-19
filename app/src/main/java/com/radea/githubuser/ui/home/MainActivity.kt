package com.radea.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.radea.githubuser.R
import com.radea.githubuser.ui.common.UsersAdapter
import com.radea.githubuser.data.remote.response.ResponseUsersItem
import com.radea.githubuser.databinding.ActivityMainBinding
import com.radea.githubuser.utils.SettingPreferences
import com.radea.githubuser.utils.dataStore
import com.radea.githubuser.ui.detail_user.DetailsUserActivity
import com.radea.githubuser.ui.favorite_user.FavoriteUserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.errorComp.tvErrorMessage.visibility = View.GONE

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            val changeThemeItemMenu = binding.toolbar.menu.findItem(R.id.action_change_theme)
            if (isDarkModeActive) {
                DARK_MODE = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                changeThemeItemMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_brightness_high_24))
            } else {
                DARK_MODE = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                changeThemeItemMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_brightness_4_24))
            }
        }

        mainViewModel.findGithubUsers()

        mainViewModel.listUser.observe(this) { listUsers ->
            showRecyclerListUsers(listUsers)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.isError.observe(this) { error ->
            binding.errorComp.tvErrorMessage.visibility = View.VISIBLE
            binding.errorComp.tvErrorMessage.text = error
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_page_favorite -> {
                    val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_change_theme -> {
                    mainViewModel.saveThemeSetting(!DARK_MODE)
                }
            }
            return@setOnMenuItemClickListener true
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    if (searchView.text.toString().isNotEmpty()) {
                        mainViewModel.findGithubUserByUsername(searchView.text.toString())
                    } else {
                        mainViewModel.findGithubUsers()
                    }
                    false
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.errorComp.tvErrorMessage.visibility = View.GONE
            binding.loadingComp.llLoading.visibility = View.VISIBLE
        } else {
            binding.loadingComp.llLoading.visibility = View.GONE
        }
    }


    private fun showRecyclerListUsers(users: List<ResponseUsersItem>) {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUsersAdapter = UsersAdapter(users)
        binding.rvUsers.adapter = listUsersAdapter

        listUsersAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponseUsersItem) {
                val intentDetailUser = Intent(this@MainActivity, DetailsUserActivity::class.java)
                intentDetailUser.putExtra(DetailsUserActivity.EXTRA_DATA_USERNAME, data.login)
                startActivity(intentDetailUser)
            }
        })
    }

    companion object {
        private var DARK_MODE = false
    }
}