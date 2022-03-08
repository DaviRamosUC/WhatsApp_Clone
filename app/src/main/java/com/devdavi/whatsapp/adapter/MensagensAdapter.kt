package com.devdavi.whatsapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.model.Mensagem

class MensagensAdapter(lista: List<Mensagem>, c: Context) :
    RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {

    private val mensagens: List<Mensagem> = lista
    private val context: Context = c

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
}