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

class ContatosAdapter(listaContatos: ArrayList<Usuario>, c: Context?) :
    RecyclerView.Adapter<ContatosAdapter.MyViewHolder>() {

    private var listaContatos: List<Usuario> = listaContatos
    private var context: Context? = c

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto: ShapeableImageView = itemView.findViewById(R.id.imageViewFotoContato)
        val nome: TextView = itemView.findViewById(R.id.textNomeContato)
        val email: TextView = itemView.findViewById(R.id.textEmailContato)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_contatos, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usuario: Usuario = listaContatos[position]
        val cabecalho = usuario.email?.isEmpty()


        holder.nome.text = usuario.nome
        holder.email.text = usuario.email

        if (usuario.foto != null) {
            val uri = Uri.parse(usuario.foto)
            Glide.with(context!!).load(uri).into(holder.foto)
        } else {
            if (cabecalho!!) {
                holder.foto.setImageResource(R.drawable.icone_grupo)
                holder.email.visibility = View.GONE
            } else {
                holder.foto.setImageResource(R.drawable.padrao)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }
}