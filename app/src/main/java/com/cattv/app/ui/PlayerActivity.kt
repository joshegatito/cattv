package com.cattv.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.cattv.app.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_URL) ?: run {
            finish()
            return
        }
        val name = intent.getStringExtra(EXTRA_NAME) ?: "Canal"

        binding.tvChannelName.text = name
        binding.btnBack.setOnClickListener { finish() }

        initPlayer(url)
    }

    private fun initPlayer(url: String) {
        binding.progressBar.visibility = View.VISIBLE

        player = ExoPlayer.Builder(this).build().also { exo ->
            binding.playerView.player = exo

            val mediaItem = MediaItem.fromUri(url)
            exo.setMediaItem(mediaItem)
            exo.prepare()
            exo.playWhenReady = true

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        Player.STATE_BUFFERING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Player.STATE_ENDED -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        else -> {}
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@PlayerActivity,
                        "Error al reproducir: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
