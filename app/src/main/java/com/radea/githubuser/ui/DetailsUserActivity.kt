package com.radea.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.radea.githubuser.R
import com.radea.githubuser.adapter.SectionPagerAdapter
import com.radea.githubuser.data.response.ResponseDetailsUser
import com.radea.githubuser.data.viewmodel.DetailViewModel
import com.radea.githubuser.databinding.ActivityDetailsUserBinding

class DetailsUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel.isDetailLoading.observe(this){ isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.isDetailError.observe(this) { error ->
            binding.errorComp.tvErrorMessage.visibility = View.VISIBLE
            binding.errorComp.tvErrorMessage.text = error
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val username = intent.getStringExtra(EXTRA_DATA_USERNAME)

        val sectionsViewPager = SectionPagerAdapter(this)
        if (username != null) {
            sectionsViewPager.username = username
            binding.viewPager.adapter = sectionsViewPager
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        if (username != null) {
            detailViewModel.findDetailUser(username)
            detailViewModel.detailUser.observe(this) { detailUser ->
                setViewUserDetails(detailUser)
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

    private fun setViewUserDetails(data: ResponseDetailsUser) {
        Glide.with(this)
            .load(data.avatarUrl)
            .into(binding.sivProfile)
        binding.tvFullname.text = data.name
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
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }
}