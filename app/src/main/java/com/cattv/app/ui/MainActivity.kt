package com.cattv.app.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.cattv.app.data.ChannelRepository
import com.cattv.app.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ChannelAdapter

    // Launcher para el resultado del reconocimiento de voz
    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val matches = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val text = matches?.firstOrNull() ?: return@registerForActivityResult
            binding.etSearch.setText(text)
            filterChannels(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        setupVoiceButton()
    }

    private fun setupRecyclerView() {
        adapter = ChannelAdapter(ChannelRepository.channels) { channel ->
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.EXTRA_URL, channel.url)
                putExtra(PlayerActivity.EXTRA_NAME, channel.name)
            }
            startActivity(intent)
        }
        binding.rvChannels.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { editable ->
            filterChannels(editable?.toString() ?: "")
        }
    }

    private fun setupVoiceButton() {
        binding.btnVoice.setOnClickListener {
            startVoiceRecognition()
        }
    }

    private fun filterChannels(query: String) {
        val filtered = ChannelRepository.search(query)
        adapter.updateList(filtered)
        if (filtered.isEmpty()) {
            Toast.makeText(this, "No se encontraron canales", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di el nombre del canal...")
        }
        try {
            voiceLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Tu dispositivo no soporta búsqueda por voz", Toast.LENGTH_SHORT).show()
        }
    }
}
