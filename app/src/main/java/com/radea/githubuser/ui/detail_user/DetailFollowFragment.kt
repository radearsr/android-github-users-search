package com.radea.githubuser.ui.detail_user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.radea.githubuser.ui.common.UsersAdapter
import com.radea.githubuser.data.remote.response.ResponseUsersItem
import com.radea.githubuser.databinding.FragmentDetailFollowBinding

class DetailFollowFragment : Fragment() {

    private var _binding: FragmentDetailFollowBinding? = null
    private val binding get() = _binding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val ARG_POSITION = "section_position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)

        detailViewModel.followers.observe(viewLifecycleOwner) { followers ->
            showRecyclerListFollow(followers)
        }

        detailViewModel.following.observe(viewLifecycleOwner) { following ->
            showRecyclerListFollow(following)
        }

        detailViewModel.isFollowerLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.isFollowingLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.isFollowerError.observe(viewLifecycleOwner) { error ->
            binding?.errorComp?.tvErrorMessage?.visibility = View.VISIBLE
            binding?.errorComp?.tvErrorMessage?.text = error
        }

        detailViewModel.isFollowingError.observe(viewLifecycleOwner) { error ->
            binding?.errorComp?.tvErrorMessage?.visibility = View.VISIBLE
            binding?.errorComp?.tvErrorMessage?.text = error
        }

        if (position == 1) {
            Log.i("DetailFollowFragment" , "PAGE $position $username")
            if (username != null) {
                detailViewModel.getUserFollowers(username)
            }
        } else {
            Log.i("DetailFollowFragment" , "PAGE $position $username")
            if (username != null) {
                detailViewModel.getUserFollowing(username)
            }
        }
    }

    private fun showRecyclerListFollow(users: List<ResponseUsersItem>) {
        binding?.rvFollow?.layoutManager = LinearLayoutManager(requireContext())
        val listFollowAdapter = UsersAdapter(users)
        binding?.rvFollow?.adapter = listFollowAdapter

        listFollowAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponseUsersItem) {
                val intentDetailUser = Intent(requireContext(), DetailsUserActivity::class.java)
                intentDetailUser.putExtra(DetailsUserActivity.EXTRA_DATA_USERNAME, data.login)
                startActivity(intentDetailUser)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.errorComp?.tvErrorMessage?.visibility  = View.GONE
                    binding?.loadingComp?.llLoading?.visibility = View.VISIBLE
        } else {
            binding?.loadingComp?.llLoading?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}