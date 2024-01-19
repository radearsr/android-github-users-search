package com.radea.githubuser.ui.favorite_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.radea.githubuser.R
import com.radea.githubuser.data.local.entity.FavoriteUser
import com.radea.githubuser.databinding.ActivityFavoriteUserBinding
import com.radea.githubuser.ui.detail_user.DetailsUserActivity

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: FavoriteUserViewModelFactory =
            FavoriteUserViewModelFactory.getInstance(applicationContext)
        val viewModel: FavoriteUserViewModel by viewModels { factory }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.getFavoriteUsers().observe(this) { favoriteUsers ->
            binding.tvNotFound.visibility = View.GONE
            if (favoriteUsers.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
            }
            val adapter = FavoriteUserAdapter(favoriteUsers)
            binding.rvFavoriteUser.layoutManager = LinearLayoutManager(applicationContext)
            binding.rvFavoriteUser.adapter = adapter
            adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback{
                override fun onItemClicked(data: FavoriteUser) {
                    val intentDetailUser = Intent(applicationContext, DetailsUserActivity::class.java)
                    intentDetailUser.putExtra(DetailsUserActivity.EXTRA_DATA_USERNAME, data.username)
                    startActivity(intentDetailUser)
                }

                override fun onItemDelete(data: FavoriteUser) {
                    val alertDialogBuilder = AlertDialog.Builder(this@FavoriteUserActivity)
                    with(alertDialogBuilder) {
                        setTitle(getString(R.string.dialog_confirm))
                        setMessage(getString(R.string.dialog_message, data.username))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.yes)) {_, _ ->
                            viewModel.deleteFavoriteUser(data)
                        }
                        setNegativeButton(getString(R.string.no)){ dialog, _ -> dialog.cancel() }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            })
        }
    }
}