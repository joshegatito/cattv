package com.cattv.app.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.cattv.app.data.ChannelRepository
import com.cattv.app.databinding.ActivityMainBinding
import com.cattv.app.model.Channel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var channelAdapter: ChannelAdapter

    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val text = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull() ?: return@registerForActivityResult
            binding.etSearch.setText(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        loadChannels()
    }

    private fun setupUI() {
        countryAdapter = CountryAdapter { country ->
            val intent = Intent(this, CountryActivity::class.java).apply {
                putExtra(CountryActivity.EXTRA_COUNTRY_CODE, country.code)
                putExtra(CountryActivity.EXTRA_COUNTRY_NAME, country.name)
                putExtra(CountryActivity.EXTRA_COUNTRY_FLAG, country.flag)
            }
            startActivity(intent)
        }

        channelAdapter = ChannelAdapter(emptyList()) { channel -> openPlayer(channel) }

        binding.rvCountries.layoutManager = LinearLayoutManager(this)
        binding.rvCountries.adapter = countryAdapter

        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = channelAdapter

        binding.etSearch.addTextChangedListener { editable ->
            val query = editable?.toString() ?: ""
            if (query.isBlank()) showCountryList() else showSearchResults(query)
        }

        binding.btnVoice.setOnClickListener { startVoiceSearch() }
        binding.btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun loadChannels() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvCountries.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            ChannelRepository.init(applicationContext)
            val countries = ChannelRepository.getCountries()
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                binding.rvCountries.visibility = View.VISIBLE
                countryAdapter.updateList(countries)
                binding.tvSubtitle.text = "${ChannelRepository.getAllChannels().size} canales · ${countries.size} países"
            }
        }
    }

    private fun showCountryList() {
        binding.rvCountries.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
    }

    private fun showSearchResults(query: String) {
        binding.rvCountries.visibility = View.GONE
        binding.rvSearch.visibility = View.VISIBLE
        channelAdapter.updateList(ChannelRepository.search(query))
    }

    private fun openPlayer(channel: Channel) {
        startActivity(Intent(this, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_URL, channel.url)
            putExtra(PlayerActivity.EXTRA_NAME, channel.name)
            putExtra(PlayerActivity.EXTRA_LOGO, channel.logoUrl)
        })
    }

    private fun startVoiceSearch() {
        try {
            voiceLauncher.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Di el nombre del canal...")
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Búsqueda por voz no disponible", Toast.LENGTH_SHORT).show()
        }
    }
}
