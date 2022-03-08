package com.devdavi.whatsapp.activity

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.databinding.ActivityChatBinding
import com.devdavi.whatsapp.model.Usuario
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var textViewNome: TextView
    private lateinit var imageViewFoto: ShapeableImageView
    private lateinit var usuarioDestinatario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Configuracoes iniciais
        textViewNome = binding.textViewNomeChat
        imageViewFoto = binding.circleImageFotoChat

        //Recuperar dados do usuário destinatario
        val bundle = intent.extras
        if (bundle != null) {
            usuarioDestinatario = bundle.getSerializable("chatContato") as Usuario
            textViewNome.text = usuarioDestinatario.nome
            val foto = usuarioDestinatario.foto
            if (foto != null) {
                val url = Uri.parse(usuarioDestinatario.foto)
                Glide.with(this).load(url).into(imageViewFoto)
            } else {
                imageViewFoto.setImageResource(R.drawable.padrao)
            }

        }
    }

}