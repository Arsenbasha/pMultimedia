package com.paredesleandro.pmultimedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FicherosAdapter(private val ficheroMutableList: MutableList<String>) :
    RecyclerView.Adapter<FicherosAdapter.FicheroViewHolder>() {
    inner class FicheroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nameAudio) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FicheroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemrecorder, parent, false)
        return FicheroViewHolder(view)
    }
    override fun getItemCount() = ficheroMutableList.size

    override fun onBindViewHolder(holder: FicheroViewHolder, position: Int) {
        holder.nombre.text = ficheroMutableList[position]
    }
}
