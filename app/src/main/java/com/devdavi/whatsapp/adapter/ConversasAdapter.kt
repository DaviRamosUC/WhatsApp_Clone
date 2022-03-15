package com.devdavi.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.model.Conversa
import com.google.android.material.imageview.ShapeableImageView

class ConversasAdapter(lista: List<Conversa>, c: Context?) :
    RecyclerView.Adapter<ConversasAdapter.MyViewHolder>() {

    private var convesas: List<Conversa> = lista
    private var context: Context? = c

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto: ShapeableImageView = itemView.findViewById(R.id.imageViewFotoContato)
        val nome: TextView = itemView.findViewById(R.id.textNomeContato)
        val ultimaMensagem: TextView = itemView.findViewById(R.id.textEmailContato)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val conversa = convesas[position]
        val usuario = conversa.usuarioExibicao
        holder.ultimaMensagem.text = conversa.ultimaMensagem
        holder.nome.text = usuario?.nome

        if (usuario?.foto != null) {
            val uri = Uri.parse(usuario.foto)
            Glide.with(context!!).load(uri).into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return convesas.size
    }


}