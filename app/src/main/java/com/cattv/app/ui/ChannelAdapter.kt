package com.cattv.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cattv.app.R
import com.cattv.app.databinding.ItemChannelBinding
import com.cattv.app.model.Channel

class ChannelAdapter(
    private var channels: List<Channel>,
    private val onClick: (Channel) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChannelBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = channels[position]
        holder.binding.apply {
            tvChannelName.text = channel.name
            tvCategory.text = channel.category
            Glide.with(ivLogo.context)
                .load(channel.logoUrl)
                .placeholder(R.drawable.ic_tv)
                .error(R.drawable.ic_tv)
                .into(ivLogo)
            root.setOnClickListener { onClick(channel) }
        }
    }

    override fun getItemCount() = channels.size

    fun updateList(newChannels: List<Channel>) {
        channels = newChannels
        notifyDataSetChanged()
    }
}
