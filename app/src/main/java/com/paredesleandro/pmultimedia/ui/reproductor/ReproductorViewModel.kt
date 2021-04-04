package com.paredesleandro.pmultimedia.ui.reproductor

import android.app.Application
import android.net.Uri
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paredesleandro.pmultimedia.R


class ReproductorViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var _play: LinearLayout
    private lateinit var _stop: LinearLayout
    var isstop = true
    var ispause = true
    private lateinit var _status: ImageView
    private lateinit var _pause: LinearLayout
    private lateinit var _video: VideoView

    private val _text = MutableLiveData<String>().apply {
        value = getApplication<Application>().resources.getString(R.string.pelicula)
    }
    val text: LiveData<String> = _text
    fun inicio(
        play: LinearLayout,
        stop: LinearLayout,
        status: ImageView,
        pause: LinearLayout,
        video: VideoView
    ) {
        _play = play
        _stop = stop
        _status = status
        _pause = pause
        _video = video
    }

    fun stop() {
        _video.stopPlayback()
        _video.seekTo(0)
        _pause.visibility = View.INVISIBLE
        _play.visibility = View.VISIBLE
        isstop = true
        ispause = false
    }

     fun hideicon() {
        object : CountDownTimer(3000, 1000) {
            override fun onFinish() { _status.visibility = View.INVISIBLE }
            override fun onTick(millisUntilFinished: Long) {}
        }.start()
    }

     fun pauseStartOnVideo() = if (ispause) play() else pause()

    private fun changeIconStatus() = if (ispause) _status.setImageResource(R.drawable.play_24)
    else _status.setImageResource(R.drawable.pause_24)


    fun play() {
        if (isstop) {
            _video.setVideoURI(Uri.parse(ruta()))
            isstop = false
            _video.start()
        } else _video.start()
        _play.visibility = View.INVISIBLE
        _pause.visibility = View.VISIBLE
        ispause = false
        changeIconStatus()
        hideicon()
    }

    fun pause() {
        _video.pause()
        ispause = true
        _pause.visibility = View.INVISIBLE
        _play.visibility = View.VISIBLE
        changeIconStatus()
        hideicon()
    }

    private fun ruta(): String =
        "android.resource://" + getApplication<Application>().packageName + "/" + R.raw.video
}