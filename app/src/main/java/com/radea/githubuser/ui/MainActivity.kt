package com.radea.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.radea.githubuser.adapter.UsersAdapter
import com.radea.githubuser.data.response.ResponseUsersItem
import com.radea.githubuser.data.viewmodel.MainViewModel
import com.radea.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    Log.i("MAIN", "${searchView.text}")
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
}