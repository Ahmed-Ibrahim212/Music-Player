package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FragmentPlayListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlayListFragment : Fragment() {
    //initializing exo player

    private val playbackStateListener: Player.Listener = playbackStateListener()
    private lateinit var player: ExoPlayer
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    //initializing argument fo save aregs
    private val args: PlayListFragmentArgs by navArgs()


    //initializing music trackItems
    private lateinit var musicPlayer: TracksItem

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dont want the bottom nav to show in this fragment so put the visibility as gone
        bottomNav = (activity as MainActivity).bottomNav
        bottomNav.visibility = View.GONE

        initializePlayer()
        //initializing args
        musicPlayer = args.musicId

        binding.arrowImage.setOnClickListener {
            findNavController().navigate(R.id.nowPlayingFragment2)

        }

        //binding the nowplaying to playlistfragment
        binding.apply {
            weekendTxt.text = musicPlayer.name
            titleName.text = musicPlayer.artist_name
            Glide.with(binding.root.context)
                .load(musicPlayer.artwork)
                .into(image)
            timeRemain.text = musicPlayer.duration
            timeRemainText.text = musicPlayer.duration
        }

        binding.playButton.setOnClickListener {

        }
        binding.nextBtn.setOnClickListener {

        }
        binding.prevBtn.setOnClickListener {
            if (playWhenReady) {
                musicPlayer.preview
            }
        }
    }



    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        player = ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build()
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri(getString(R.string.media_url_dash))
                    .setMimeType(MimeTypes.APPLICATION_MPD)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
    }
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }
     override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }
        private fun releasePlayer() {
            player?.let { exoPlayer ->
                playbackPosition = exoPlayer.currentPosition
                currentItem = exoPlayer.currentMediaItemIndex
                exoPlayer.removeListener(playbackStateListener)
                playWhenReady = exoPlayer.playWhenReady
                exoPlayer.release()
            }
        }
    }


private fun playbackStateListener() = object : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(ContentValues.TAG, "changed state to $stateString")
    }
}