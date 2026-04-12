package com.cattv.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.cattv.app.R
import com.cattv.app.data.ChannelLogger
import com.cattv.app.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL  = "extra_url"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_LOGO = "extra_logo"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null
    private var channelName = ""
    private var channelUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        channelUrl  = intent.getStringExtra(EXTRA_URL)  ?: run { finish(); return }
        channelName = intent.getStringExtra(EXTRA_NAME) ?: "Canal"
        val logoUrl = intent.getStringExtra(EXTRA_LOGO) ?: ""

        binding.tvChannelName.text = channelName
        binding.btnBack.setOnClickListener { finish() }

        if (logoUrl.isNotBlank()) {
            Glide.with(this).load(logoUrl)
                .placeholder(R.drawable.ic_tv)
                .into(binding.ivChannelLogo)
        }

        initPlayer(channelUrl)
    }

    private fun initPlayer(url: String) {
        binding.progressBar.visibility = View.VISIBLE

        player = ExoPlayer.Builder(this).build().also { exo ->
            binding.playerView.player = exo
            exo.setMediaItem(MediaItem.fromUri(url))
            exo.prepare()
            exo.playWhenReady = true

            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                            ChannelLogger.logSuccessChannel(applicationContext, channelName)
                        }
                        Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
                        else -> binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.progressBar.visibility = View.GONE
                    ChannelLogger.logFailedChannel(
                        applicationContext,
                        channelName,
                        url,
                        error.message ?: "Unknown error"
                    )
                    Toast.makeText(
                        this@PlayerActivity,
                        "Canal no disponible: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    override fun onPause()   { super.onPause();   player?.pause() }
    override fun onResume()  { super.onResume();  player?.play()  }
    override fun onDestroy() { super.onDestroy(); player?.release(); player = null }
}
