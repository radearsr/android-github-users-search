package com.radea.githubuser.ui.favorite_user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radea.githubuser.data.local.entity.FavoriteUser
import com.radea.githubuser.databinding.ItemListFavoriteUserBinding

class FavoriteUserAdapter(private val listFavoriteUsers: List<FavoriteUser>) : RecyclerView.Adapter<FavoriteUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder(var binding: ItemListFavoriteUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListFavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavoriteUsers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, avatarUrl) = listFavoriteUsers[position]
        holder.binding.tvUsername.text = login
        Glide.with(holder.binding.root)
            .load(avatarUrl)
            .into(holder.binding.sivProfile)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFavoriteUsers[position]) }
        holder.binding.btnDelete.setOnClickListener { onItemClickCallback.onItemDelete(listFavoriteUsers[position]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
        fun onItemDelete(data: FavoriteUser)
    }
}