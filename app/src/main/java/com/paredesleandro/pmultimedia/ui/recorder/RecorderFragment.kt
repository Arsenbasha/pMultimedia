package com.paredesleandro.pmultimedia.ui.recorder

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paredesleandro.pmultimedia.FicherosAdapter
import com.paredesleandro.pmultimedia.R
import com.paredesleandro.pmultimedia.databinding.FragmentRecorderBinding
import java.io.File
import java.io.IOException

class RecorderFragment : Fragment() {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var recorderBinding: FragmentRecorderBinding
    private lateinit var start: ImageButton
    private lateinit var stop: ImageButton
    private lateinit var pause: ImageButton
    private var permisosOk: Boolean = false
    private var isPause: Boolean = false
    private var cronoMetro: Long = 0
    private lateinit var dir: File
    private lateinit var crono: Chronometer
    var output: String? = null
    private var isStop: Boolean = false
    private var listFicheros = mutableListOf<String>()
    private lateinit var ficherosAdapter: FicherosAdapter

    private lateinit var recorder: MediaRecorder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recorderViewModel =
            ViewModelProvider(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)
        recorderBinding = FragmentRecorderBinding.bind(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkpermison(view)
        start = recorderBinding.start
        stop = recorderBinding.stop
        pause = recorderBinding.pause
        crono = recorderBinding.chronometer
        fullList()
        if (permisosOk) {
            prepare()
            start.setOnClickListener {
                if (!isPause) start()
                else reanudar()
            }
            stop.setOnClickListener {

                if (!isStop) stop()
            }
            pause.setOnClickListener { if (!isPause) pause() }
        } else {
            Toast.makeText(context, "No tenemos perimsos ", Toast.LENGTH_LONG).show()
            checkpermison(view)
        }
    }

    private fun prepare() {
        try {
            val recorderDirectory = getOutputDirectory()
            recorderDirectory.mkdirs()
        } catch (e: IOException) {
            Toast.makeText(context, "Error ioexception ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        dir = getOutputDirectory()
        if (dir.exists()) {
            val count = dir.listFiles().size
            output = getOutputDirectory().absolutePath + "/grabacion$count.mp3"
        }
        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        Toast.makeText(context, "$output", Toast.LENGTH_SHORT).show()
        recorder.setOutputFile(output)


    }

    private fun fullList() {
        listFicheros.clear()
        val dir = getOutputDirectory()
        dir.walkTopDown().forEach {
            if (it.isFile) listFicheros.add(it.name)
        }
        listFicheros.forEach {
            Log.d("INFO", "lista de ficherosd  ${it}")
        }
        lista()

    }

    private fun start() {
        try {
            Log.d("INFO", "Starting recording!")
            recorder.prepare()
            Log.d("INFO", "PREPARE OK!")
            recorder.start()
            Log.d("INFO", " START OK!")
            isStop = false
            isPause = false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        crono.visibility = View.VISIBLE
        crono.base = SystemClock.elapsedRealtime();
        crono.start()
    }


    fun stop() {
        Log.d("INFO", "PAUSE EN ESTADO $isPause y estop en estado $isStop")
        recorder.stop()
        recorder.release()
        crono.stop()
        cronoMetro = 0
        isStop = true
        isPause = false
        fullList()
        prepare()


    }


    @TargetApi(Build.VERSION_CODES.N)
    fun pause() {

        Log.d("INFO", "PAUSE EN ESTADO $isPause y estop en estado $isStop")
        recorder.pause()
        cronoMetro = crono.base - SystemClock.elapsedRealtime();
        crono.stop()
        isPause = true
        Toast.makeText(context, "Grabación Pausada", Toast.LENGTH_SHORT).show()
    }


    @TargetApi(Build.VERSION_CODES.N)
    fun reanudar() {
        recorder.resume()
        crono.base = SystemClock.elapsedRealtime() + cronoMetro;
        crono.start();
    }

    private fun lista() {
        ficherosAdapter = FicherosAdapter(listFicheros)
        val recycleView = view?.findViewById<RecyclerView>(R.id.listF)
        recycleView!!.adapter = ficherosAdapter
        recycleView.layoutManager = LinearLayoutManager(context)
    }

    private fun checkpermison(view: View) {
        if (ActivityCompat.checkSelfPermission(
                view.context,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                view.context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)

        } else {
            permisosOk = true
        }
    }

    fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + "/grabaciones").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }

}