package com.radea.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.radea.githubuser.ui.DetailFollowFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       val fragment: Fragment = DetailFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailFollowFragment.ARG_POSITION, position + 1)
            putString(DetailFollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}