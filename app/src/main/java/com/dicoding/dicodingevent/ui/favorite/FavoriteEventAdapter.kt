package com.dicoding.dicodingevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemListFavoriteBinding
import androidx.recyclerview.widget.DiffUtil

class FavoriteEventAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<ListEventsItem , FavoriteEventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): EventViewHolder {
        val binding = ItemListFavoriteBinding.inflate(
            LayoutInflater.from(parent.context) , parent , false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder , position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            event.id.let { id -> onItemClicked(id.toString()) }
        }
    }

    class EventViewHolder(private val binding: ItemListFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            binding.tvNameFavorite.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.imgPhotoItemFavorite)
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<ListEventsItem>() {
        override fun areItemsTheSame(oldItem: ListEventsItem , newItem: ListEventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ListEventsItem ,
            newItem: ListEventsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
