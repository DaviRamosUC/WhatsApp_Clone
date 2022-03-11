package com.devdavi.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.Key.VISIBILITY
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.model.Mensagem
import com.devdavi.whatsapp.utils.Helpers

class MensagensAdapter(lista: List<Mensagem>, c: Context) :
    RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {

    private val mensagens: List<Mensagem> = lista
    private val context: Context = c

    companion object {
        @JvmStatic
        private val TIPO_REMETENTE = 0

        @JvmStatic
        private val TIPO_DESTINATARIO = 1
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensagem: TextView = itemView.findViewById(R.id.textMensagemTexto)
        val imagem: ImageView = itemView.findViewById(R.id.imageMensagemFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var item: View? = null
        if (viewType == TIPO_REMETENTE) {
            item = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_mensagem_remetente, parent, false)
        } else if (viewType == TIPO_DESTINATARIO) {
            item = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_mensagem_destinatario, parent, false)

        }
        return MyViewHolder(item!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem = mensagens[position]
        val msg = mensagem.mensagem
        val imagem = mensagem.imagem

        if (imagem != null && imagem.isNotEmpty()) {
            val url = Uri.parse(imagem)
            Glide.with(context).load(url).into(holder.imagem)
            holder.mensagem.visibility = View.GONE
        } else {
            holder.mensagem.text = msg
            holder.imagem.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        Log.i("SIZE", "getItemCount: ${mensagens.size}")
        return mensagens.size
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = mensagens[position]
        val idUsuario = Helpers.getIdentificadoUsuario()

        if (idUsuario == mensagem.idUsuario) {
            return TIPO_REMETENTE
        }
        return TIPO_DESTINATARIO
    }
}