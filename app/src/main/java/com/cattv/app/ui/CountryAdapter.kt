package com.cattv.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cattv.app.databinding.ItemCountryBinding
import com.cattv.app.model.Country

class CountryAdapter(
    private var countries: List<Country> = emptyList(),
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.binding.apply {
            tvFlag.text = country.flag
            tvCountryName.text = country.name
            tvChannelCount.text = "${country.channels.size} canales"
            root.setOnClickListener { onClick(country) }
        }
    }

    override fun getItemCount() = countries.size

    fun updateList(newList: List<Country>) {
        countries = newList
        notifyDataSetChanged()
    }
}
