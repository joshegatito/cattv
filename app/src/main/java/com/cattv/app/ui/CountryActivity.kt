package com.cattv.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cattv.app.data.ChannelRepository
import com.cattv.app.databinding.ActivityCountryBinding

class CountryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COUNTRY_CODE = "extra_country_code"
        const val EXTRA_COUNTRY_NAME = "extra_country_name"
        const val EXTRA_COUNTRY_FLAG = "extra_country_flag"
    }

    private lateinit var binding: ActivityCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val code = intent.getStringExtra(EXTRA_COUNTRY_CODE) ?: return
        val name = intent.getStringExtra(EXTRA_COUNTRY_NAME) ?: ""
        val flag = intent.getStringExtra(EXTRA_COUNTRY_FLAG) ?: ""

        val country = ChannelRepository.getCountries().find { it.code == code } ?: return

        binding.tvTitle.text = "$flag $name"
        binding.tvSubtitle.text = "${country.channels.size} canales disponibles"
        binding.btnBack.setOnClickListener { finish() }

        val adapter = ChannelAdapter(country.channels) { channel ->
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.EXTRA_URL, channel.url)
                putExtra(PlayerActivity.EXTRA_NAME, channel.name)
                putExtra(PlayerActivity.EXTRA_LOGO, channel.logoUrl)
            })
        }

        binding.rvChannels.layoutManager = LinearLayoutManager(this)
        binding.rvChannels.adapter = adapter
    }
}
