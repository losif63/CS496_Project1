package com.cs496.project1.player.ui.player

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs496.project1.player.MusicItem

import com.cs496.project1.player.getInstance
import java.util.*

class MusicPlayerViewModel : ViewModel() {

    private val TAG = "MusicPlayerViewModel"

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String>
    get() = _title

    private val _artist: MutableLiveData<String> = MutableLiveData()
    val artist: LiveData<String>
    get() = _artist

    private val _state: MutableLiveData<String> = MutableLiveData()
    val state: LiveData<String>
    get() = _state

    private val _duration: MutableLiveData<Int> = MutableLiveData()
    val duration: LiveData<Int>
    get() = _duration

    private val _albumArtUri: MutableLiveData<String> = MutableLiveData()
    val albumArtUri: LiveData<String>
    get() = _albumArtUri

    val currentPosition: MutableLiveData<Int> = MutableLiveData()

    private var updateTimer: Timer? = null

    init {
        Log.d(TAG, "Start")

        val instance = getInstance()
        instance.onTrackEnded += { track: MusicItem? ->
            updateMetadataFromPlayer()
            updatePlayStateFromPlayer()
        }
        instance.onTrackStarted += { track: MusicItem? ->
            updateMetadataFromPlayer()
            updatePlayStateFromPlayer()
        }

        updateMetadataFromPlayer()
        updatePlayStateFromPlayer()

        this.updateTimer = kotlin.concurrent.timer(period = 1000) {
            updatePlayStateFromPlayer()
        }

    }

    fun updateMetadataFromPlayer() {
        Log.d(TAG, "updateMetadataFromPlayer")
        val instance = getInstance()
        if(instance.isPlaying) {
            val track = instance.currentMusicItem!!
            Log.d(TAG, track.filePath)
            this._title.value = track.metadata.title
            Log.d(TAG, this._title.value!!)
            this._artist.value = track.metadata.artist
            Log.d(TAG, this._artist.value!!)
            this._albumArtUri.value = track.metadata.art_uri.toString()
            Log.d(TAG, this._albumArtUri.value!!)
        } else {
            _title.value = "Press Play..."
        }
    }

    fun updatePlayStateFromPlayer() {
        val player = getInstance()

        if(player.isPlaying) {
            if(player.isPaused) {
                this._state.postValue("|>")
            } else {
                this._state.postValue("||")
            }
        } else {
            this._state.postValue("|>")
        }

        this.currentPosition.postValue(player.currentPosition)
        this._duration.postValue(player.duration)
    }

    fun pauseResume() {
        val player = getInstance()
        if(player.isPlaying) {
            if(player.isPaused) {
                player.resume()
            } else {
                player.pause()
            }
        } else {
            player.play()
        }

        updatePlayStateFromPlayer()
    }

    fun updatePosition() {
        if(this.currentPosition.value == null) return
        val player = getInstance()
        val newPos = this.currentPosition.value!!
        if(Math.abs(newPos - player.currentPosition) > 1500 && !(player.currentPosition < 100)) {
            Log.d(TAG, "seek ${newPos} from ${player.currentPosition}")
            player.seek(newPos)
        }
    }

    fun prev() {
        getInstance().previous()
    }

    fun next() {
        getInstance().next()
    }

    override fun onCleared() {
        Log.d(TAG, "Cleared")
        super.onCleared()

        this.updateTimer?.cancel()
    }
}