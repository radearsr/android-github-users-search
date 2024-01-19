package com.radea.githubuser.ui.detail_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.radea.githubuser.R
import com.radea.githubuser.data.local.entity.FavoriteUser
import com.radea.githubuser.data.remote.response.ResponseDetailsUser
import com.radea.githubuser.databinding.ActivityDetailsUserBinding
import com.radea.githubuser.ui.favorite_user.FavoriteUserViewModel
import com.radea.githubuser.ui.favorite_user.FavoriteUserViewModelFactory

class DetailsUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    private var avatarUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: FavoriteUserViewModelFactory =
            FavoriteUserViewModelFactory.getInstance(applicationContext)
        val favoriteUserViewModel: FavoriteUserViewModel by viewModels {
            factory
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        initObserveViewModel()

        val username = intent.getStringExtra(EXTRA_DATA_USERNAME)
        username?.let {
            initViewPagerContent(username)
        }

        if (username != null) {
            favoriteUserViewModel.getFavoriteUserByUsername(username).observe(this){ favoriteUser ->
                if (favoriteUser != null) {
                    IS_FAVORITE = true
                    binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24))
                } else {
                    IS_FAVORITE = false
                    binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_24))
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            if (username != null) {
                val favoriteUser = FavoriteUser(
                    username,
                    avatarUrl
                )
                if (IS_FAVORITE) {
                    favoriteUserViewModel.deleteFavoriteUser(favoriteUser)
                } else {
                    favoriteUserViewModel.insertFavoriteUser(favoriteUser)
                }
            }
        }
    }

    private fun initObserveViewModel() {
        detailViewModel.isDetailLoading.observe(this){ isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.isDetailError.observe(this) { error ->
            binding.errorComp.tvErrorMessage.visibility = View.VISIBLE
            binding.errorComp.tvErrorMessage.text = error
        }

    }

    private fun initViewPagerContent(username: String) {
        val sectionsViewPager = SectionPagerAdapter(this)
        sectionsViewPager.username = username
        binding.viewPager.adapter = sectionsViewPager
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.findDetailUser(username)
        detailViewModel.detailUser.observe(this) { detailUser ->
            setViewUserDetails(detailUser)
            avatarUrl = detailUser.avatarUrl
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

    private fun setViewUserDetails(data: ResponseDetailsUser) {
        Glide.with(this)
            .load(data.avatarUrl)
            .into(binding.sivProfile)
        binding.tvFullName.text = data.name
        binding.tvUsername.text = data.login

        val newTextFollower = this@DetailsUserActivity.resources.getString(R.string.followers, data.followers)
        val newTextFollowing = this@DetailsUserActivity.resources.getString(R.string.following, data.following)
        val newTextRepositories = this@DetailsUserActivity.resources.getString(R.string.repositories, data.publicRepos)

        binding.tvFollowers.text = newTextFollower
        binding.tvFollowing.text = newTextFollowing
        binding.tvRepositories.text = newTextRepositories
    }

    companion object {
        const val EXTRA_DATA_USERNAME = "extra_data_username"
        private var IS_FAVORITE = false
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }
}