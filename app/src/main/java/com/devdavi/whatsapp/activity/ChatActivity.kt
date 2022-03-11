package com.devdavi.whatsapp.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.adapter.MensagensAdapter
import com.devdavi.whatsapp.databinding.ActivityChatBinding
import com.devdavi.whatsapp.model.Mensagem
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.Base64Custom
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var textViewNome: TextView
    private lateinit var imageViewFoto: ShapeableImageView
    private lateinit var editMensagem: EditText
    private lateinit var usuarioDestinatario: Usuario
    private lateinit var database: DatabaseReference
    private lateinit var mensagensRef: DatabaseReference
    private lateinit var childEventListenerMensagens: ChildEventListener

    //identificador usuarios remetente e destinatario
    private lateinit var idUsuarioRemetente: String
    private lateinit var idUsuarioDestinatario: String

    private lateinit var recyclerMensagem: RecyclerView
    private lateinit var adapter: MensagensAdapter
    private val mensagens = ArrayList<Mensagem>()


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
        recyclerMensagem = binding.contentChat.recyclerMensagens

        //Recuperar id do usuário remetente
        idUsuarioRemetente = Helpers.getIdentificadoUsuario()


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

        //Configuração adapter
        adapter = MensagensAdapter(mensagens, applicationContext)

        //Configuração recyclerview
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerMensagem.layoutManager = layoutManager
        recyclerMensagem.setHasFixedSize(true)
        recyclerMensagem.adapter = adapter

        // Adicionando config para database
        database = FirebaseConfig.database.reference
        mensagensRef = database.child("mensagens")
            .child(idUsuarioRemetente)
            .child(idUsuarioDestinatario)
    }

    override fun onStart() {
        super.onStart()
        recuperaMensagens()
    }

    override fun onStop() {
        super.onStop()
        mensagensRef.removeEventListener(childEventListenerMensagens)
    }

    fun enviarMensagem(view: View) {
        val textoMensagem = editMensagem.text.toString()
        if (textoMensagem.isNotEmpty()) {
            val mensagem = Mensagem(idUsuarioRemetente, textoMensagem, null)
            //Salvar mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem)
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem)
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

    private fun recuperaMensagens() {
        childEventListenerMensagens =
            mensagensRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val mensagem = snapshot.getValue(Mensagem::class.java)
                    Log.i("MENSAGEM", "onChildAdded: $mensagem")
                    if (mensagem != null) {
                        mensagens.add(mensagem)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}