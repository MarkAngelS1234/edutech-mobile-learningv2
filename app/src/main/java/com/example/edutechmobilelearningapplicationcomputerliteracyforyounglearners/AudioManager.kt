package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * BGMManager - Singleton to handle Background Music playback, playlist, and volume.
 */
object BGMManager {
    private var mediaPlayer: MediaPlayer? = null
    
    // --- TRACK LIST ---
    private val track1Res = R.raw.wreckitralph
    private val track2Res = R.raw.takeyouhometonightt // TEMPORARY PLACEHOLDER for takeyouhometonightt
    private val track3Res = R.raw.classicsoundquizziz
    private val track4Res = R.raw.celestial
    private val playlist = listOf(track1Res, track2Res, track3Res,track4Res,)
    private var currentTrackIndex = 0
    
    // --- STATE ---
    var baseVolume by mutableFloatStateOf(0.8f)
    private var _isLessonMode by mutableStateOf(false)
    private var _isForcedSilence by mutableStateOf(false) // For video playback or assessments
    
    val isLessonMode: Boolean get() = _isLessonMode
    val isForcedSilence: Boolean get() = _isForcedSilence
    
    private var isPausedManually = false
    private var isAppInBackground = false

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            playTrack(context, playlist[currentTrackIndex])
        }
    }

    private fun playTrack(context: Context, resId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            setVolume(calculateFinalVolume(), calculateFinalVolume())
            setOnCompletionListener {
                currentTrackIndex = (currentTrackIndex + 1) % playlist.size
                playTrack(context, playlist[currentTrackIndex])
            }
            start()
        }
        applyVolume()
    }

    private fun calculateFinalVolume(): Float {
        if (_isForcedSilence) return 0f
        val multiplier = if (_isLessonMode) 0.3f else 1.0f
        return baseVolume * multiplier
    }

    fun applyVolume() {
        val volume = calculateFinalVolume()
        mediaPlayer?.setVolume(volume, volume)
    }

    fun setAppBackgrounded(backgrounded: Boolean) {
        isAppInBackground = backgrounded
        updatePlaybackState()
    }

    fun setForcedSilence(silence: Boolean) {
        _isForcedSilence = silence
        updatePlaybackState()
        applyVolume()
    }

    fun setLessonMode(enabled: Boolean) {
        _isLessonMode = enabled
        applyVolume()
    }

    private fun updatePlaybackState() {
        val shouldBePlaying = !isAppInBackground && !isPausedManually
        
        if (shouldBePlaying) {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        } else {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
