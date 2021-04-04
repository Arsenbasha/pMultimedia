package com.paredesleandro.pmultimedia.ui.reproductor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paredesleandro.pmultimedia.R
import com.paredesleandro.pmultimedia.databinding.FragmentReproductorBinding

class ReproductorFragment : Fragment() {

    private lateinit var reproductorViewModel: ReproductorViewModel
    private lateinit var reproductorBinding: FragmentReproductorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reproductorViewModel = ViewModelProvider(this).get(ReproductorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reproductor, container, false)
        reproductorBinding = FragmentReproductorBinding.bind(root)
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reproductorViewModel.inicio(
            reproductorBinding.play,
            reproductorBinding.stop,
            reproductorBinding.status,
            reproductorBinding.pausa,
            reproductorBinding.videoNav
        )
        val title: TextView = reproductorBinding.title
        reproductorViewModel.text.observe(viewLifecycleOwner, Observer { title.text = it })

        reproductorBinding.stop.setOnClickListener { reproductorViewModel.stop() }
        reproductorBinding.play.setOnClickListener { reproductorViewModel.play() }
        reproductorBinding.pausa.setOnClickListener { reproductorViewModel.pause() }
        reproductorBinding.video.setOnTouchListener { v, event ->
            reproductorBinding.status.visibility=View.VISIBLE
            reproductorViewModel.pauseStartOnVideo()
            reproductorViewModel.hideicon()
            false
        }
    }
}