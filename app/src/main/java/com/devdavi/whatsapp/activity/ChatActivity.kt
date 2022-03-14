package com.devdavi.whatsapp.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.adapter.MensagensAdapter
import com.devdavi.whatsapp.databinding.ActivityChatBinding
import com.devdavi.whatsapp.model.Conversa
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
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    //Componentes de interface
    private lateinit var binding: ActivityChatBinding
    private lateinit var textViewNome: TextView
    private lateinit var imageViewFoto: ShapeableImageView
    private lateinit var editMensagem: EditText
    private lateinit var imageCamera: ImageView

    // Referências Firebase
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var mensagensRef: DatabaseReference
    private lateinit var childEventListenerMensagens: ChildEventListener

    //identificador usuarios remetente e destinatario
    private lateinit var usuarioDestinatario: Usuario
    private lateinit var idUsuarioRemetente: String
    private lateinit var idUsuarioDestinatario: String

    //Recyclerview
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
        imageCamera = binding.contentChat.imageCamera

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
        storage = FirebaseConfig.storage
        mensagensRef = database.child("mensagens")
            .child(idUsuarioRemetente)
            .child(idUsuarioDestinatario)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imagem: Bitmap?
                    try {
                        imagem = data?.extras?.get("data") as Bitmap
                        val baos = ByteArrayOutputStream()
                        imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val dadosImagem = baos.toByteArray()

                        val nomeImagem = UUID.randomUUID().toString()

                        //Configurar as referências do Firebase
                        val imagemRef = storage.child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child("$nomeImagem.jpeg")

                        val uploadTask = imagemRef.putBytes(dadosImagem)
                        uploadTask.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Erro ao fazer o upload da imagem",
                                Toast.LENGTH_LONG
                            ).show()
                        }.addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Sucesso ao fazer o upload da imagem",
                                Toast.LENGTH_LONG
                            ).show()

                            imagemRef.downloadUrl.addOnCompleteListener {
                                val url: Uri? = it.result
                                val mensagem = Mensagem()
                                mensagem.idUsuario = idUsuarioRemetente
                                mensagem.mensagem = "imagem.jpeg"
                                mensagem.imagem = url.toString()
                                //Salva a mensagem para o remetente
                                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem)
                                //Salva a mensagem para o destinatário
                                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem)

                                //Salva a conversa
                                salvarConversa(mensagem)


                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        //Evento de clique na camera
        imageCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            packageManager?.let {
                if (intent.resolveActivity(it) != null)
                    resultLauncher.launch(intent)
            }
        }
    }

    private fun salvarConversa(mensagem: Mensagem) {
        val conversaRemetente =
            Conversa(
                idUsuarioRemetente,
                idUsuarioDestinatario,
                mensagem.mensagem,
                usuarioDestinatario
            ).salvar()
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