package com.paredesleandro.pmultimedia.ui.recorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paredesleandro.pmultimedia.R

class RecorderFragment : Fragment() {

    private lateinit var recorderViewModel: RecorderViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recorderViewModel =
                ViewModelProvider(this).get(RecorderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recorder, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        recorderViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}