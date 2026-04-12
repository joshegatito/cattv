package com.cattv.app.data

import android.content.Context
import com.cattv.app.model.Channel
import com.cattv.app.model.Country

object ChannelRepository {

    private var allChannels: List<Channel> = emptyList()
    private var countries: List<Country> = emptyList()

    fun init(context: Context) {
        if (allChannels.isNotEmpty()) return
        allChannels = M3uParser.parse(context)
        buildCountries()
    }

    private fun buildCountries() {
        // Orden preferido: Perú primero, luego por cantidad de canales
        val preferred = listOf("pe", "us", "br", "uk", "fr", "de", "it", "ru", "jp", "kr", "au", "ca")

        val grouped = allChannels.groupBy { it.countryCode }

        val sortedCodes = (preferred.filter { grouped.containsKey(it) } +
                grouped.keys.filter { it !in preferred }.sortedBy {
                    M3uParser.getCountryName(it)
                })

        countries = sortedCodes.mapNotNull { code ->
            grouped[code]?.let { channels ->
                Country(
                    code = code,
                    name = M3uParser.getCountryName(code),
                    flag = M3uParser.getCountryFlag(code),
                    channels = channels.sortedBy { it.name }
                )
            }
        }
    }

    fun getCountries(): List<Country> = countries

    fun getAllChannels(): List<Channel> = allChannels

    fun search(query: String): List<Channel> {
        if (query.isBlank()) return allChannels
        val q = query.lowercase().trim()
        return allChannels.filter {
            it.name.lowercase().contains(q) ||
            it.category.lowercase().contains(q) ||
            it.countryName.lowercase().contains(q)
        }
    }
}
