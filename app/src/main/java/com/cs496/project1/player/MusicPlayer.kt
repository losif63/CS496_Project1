package com.cs496.project1.player

import android.content.Context
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.CodeBoy.MediaFacer.MediaFacer
import com.CodeBoy.MediaFacer.mediaHolders.audioContent

private lateinit var instance : MusicPlayer
fun initializePlayerService(context: Context) {
    if(::instance.isInitialized) return
    instance = MusicPlayer(context)
}

fun getInstanceSafe() : MusicPlayer? {
    if(::instance.isInitialized) return instance
    return null
}

fun getInstance() : MusicPlayer {
    return instance
}

class MusicItem(context: Context, path: String) {
    var filePath: String
    private set

    var metadata: audioContent
    private set

    init {
        this.filePath = path

        val metadata = MediaFacer.withAudioContex(context).getMusicMetaData(path)
        this.metadata = metadata
    }
}

class MusicPlayer(val context: Context) {

    private val TAG = "MusicPlayer"

    var musicList: ArrayList<MusicItem> = ArrayList()
    private set

    val isPlaying
    get() = player.isPlaying || player.currentPosition > 1

    val isPaused
    get() = (!player.isPlaying) && player.currentPosition > 1

    val length : Int
    get() = musicList.count()

    val currentMusicItem : MusicItem?
    get() {
        if(currentIndex < 0 || currentIndex >= musicList.count()) return null
        return musicList.get(this.currentIndex)
    }

    val currentPosition : Int
    get() {
        if(this.isPlaying) return player.currentPosition
        return 0
    }

    val duration : Int
    get() {
        if(this.isPlaying) return player.duration
        return 0
    }

    var onTrackStarted = Event<MusicItem?>()
    var onTrackEnded = Event<MusicItem?>()

    private var lastPosition: Int = -1
    private var currentIndex: Int = -1
    private var player: MediaPlayer = MediaPlayer()
    private var playerInitialized = false

    init {
        this.player.setOnCompletionListener {
            if(!playerInitialized) {
                playerInitialized = true
                return@setOnCompletionListener
            }

            this.onTrackEnded.invoke(this.currentMusicItem)

            this.next()
        }
    }

    fun getPlayer() : MediaPlayer{
        return player
    }

    fun add(musicItem: MusicItem) {
        this.musicList.add(musicItem)
    }

    fun add(filePath: String) {
        this.add(MusicItem(this.context, filePath))
    }

    /**
     * Playing music with given index. Error when OOR
     */
    fun play() {
        if(this.isPlaying) this.stop()

        val item = this.currentMusicItem ?: return

        val player = this.player
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setDataSource(this.context, Uri.parse(item.filePath))
        player.prepare()
        player.start()

        this.onTrackStarted.invoke(this.currentMusicItem)
    }

    fun play(index: Int) {
        if(this.currentIndex != index || !this.isPlaying) {
            this.currentIndex = index
            if(index < 0) this.currentIndex = 0

            this.play()
        }
    }

    fun pause() {
        if(this.isPlaying){
            this.player.pause()
            this.lastPosition = this.player.currentPosition
        }
    }

    fun resume() {
        if(this.isPlaying){
            Log.d(TAG, "resume.seekTo ${this.lastPosition}")
            this.player.seekTo(this.lastPosition)
            this.player.start()
        }
    }

    fun stop() {
        val player = this.player

        player.stop()
        player.reset()
        this.lastPosition = 0

        //this.currentIndex = -1
    }

    fun next() {
        if(this.length <= 0) {
            currentIndex = -1
            return
        }
        currentIndex = (currentIndex + 1) % this.length

        this.play()

        Log.d(TAG, "next.played pos ${this.player.currentPosition}")
    }

    fun previous() {
        if(this.length <= 0) {
            currentIndex = -1
            return
        }
        currentIndex = (currentIndex - 1 + this.length) % this.length

        this.play()
    }

    fun seek(newPos: Int) {
        if(this.isPlaying) {
            Log.d(TAG, "seek.seekTo ${newPos}")
            this.player.seekTo(newPos)
            this.lastPosition = newPos
        }
    }
}