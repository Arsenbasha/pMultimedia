package com.paredesleandro.pmultimedia.ui.camara

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paredesleandro.pmultimedia.R

class CamaraFragment : Fragment() {

    private lateinit var camaraViewModel: CamaraViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        camaraViewModel =
                ViewModelProvider(this).get(CamaraViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        camaraViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}