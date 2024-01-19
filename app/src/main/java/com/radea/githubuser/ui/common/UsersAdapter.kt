package com.radea.githubuser.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radea.githubuser.data.remote.response.ResponseUsersItem
import com.radea.githubuser.databinding.ItemListUserBinding

class UsersAdapter(
    private val listUsers: List<ResponseUsersItem>
) : RecyclerView.Adapter<UsersAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, avatarUrl) = listUsers[position]
        holder.binding.tvUsername.text = login
        Glide.with(holder.binding.root)
            .load(avatarUrl)
            .into(holder.binding.sivProfile)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUsers[position]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResponseUsersItem)
    }

}