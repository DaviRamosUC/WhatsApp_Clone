package com.devdavi.whatsapp.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.databinding.ActivityChatBinding
import com.devdavi.whatsapp.model.Mensagem
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.Base64Custom
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.google.android.material.imageview.ShapeableImageView

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var textViewNome: TextView
    private lateinit var imageViewFoto: ShapeableImageView
    private lateinit var usuarioDestinatario: Usuario
    private lateinit var editMensagem: EditText

    //identificador usuarios remetente e destinatario
    private lateinit var idUsuarioRementente: String
    private lateinit var idUsuarioDestinatario: String


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
        editMensagem = binding.contentChat.editMensagem

        //Recuperar id do usuário remetente
        idUsuarioRementente = Helpers.getIdentificadoUsuario()


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
            //Recuperar id do usuário destinatario
            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.email!!)

        }
    }

    fun enviarMensagem(view: View) {
        val textoMensagem = editMensagem.text.toString()
        if (!textoMensagem.isEmpty()) {
            val mensagem = Mensagem(idUsuarioRementente, textoMensagem, null)
            //Salvar mensagem para o remetente
            salvarMensagem(idUsuarioRementente, idUsuarioDestinatario, mensagem)
            editMensagem.setText("")
        } else {
            Toast.makeText(
                this,
                "Digite uma mensagem para enviar",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun salvarMensagem(idRementente: String, idDestinatario: String, mensagem: Mensagem) {
        val database = FirebaseConfig.database.reference
        val mensagemRef = database.child("mensagens")

        mensagemRef.child(idRementente).child(idDestinatario).push().setValue(mensagem)

    }

}