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
import com.devdavi.whatsapp.model.Usuario
import com.google.android.material.imageview.ShapeableImageView

class GrupoSelecionadoAdapter(listaContatos: List<Usuario>, c: Context) :
    RecyclerView.Adapter<GrupoSelecionadoAdapter.ViewHolder>() {

    private var contatosSelecionados: List<Usuario> = listaContatos
    private var context: Context? = c

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto: ShapeableImageView = itemView.findViewById(R.id.imageViewFotoMembroSelecionado)
        val nome: TextView = itemView.findViewById(R.id.textNomeMembroSelecionado)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrupoSelecionadoAdapter.ViewHolder {
        val itemLista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_grupo_selecionado, parent, false)
        return GrupoSelecionadoAdapter.ViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario: Usuario = contatosSelecionados[position]

        holder.nome.text = usuario.nome

        if (usuario.foto != null) {
            val uri = Uri.parse(usuario.foto)
            Glide.with(context!!).load(uri).into(holder.foto)
        } else {
            holder.foto.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return contatosSelecionados.size
    }

}