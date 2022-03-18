package com.devdavi.whatsapp.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdavi.whatsapp.adapter.GrupoSelecionadoAdapter
import com.devdavi.whatsapp.databinding.ActivityCadastroGrupoBinding
import com.devdavi.whatsapp.model.Grupo
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.devdavi.whatsapp.utils.RecyclerItemClickListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.lang.Exception

class CadastroGrupoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroGrupoBinding
    private val listaMembrosSelecionados = ArrayList<Usuario>()
    private lateinit var grupoSelecionadoAdapter: GrupoSelecionadoAdapter
    private lateinit var recyclerMembrosSelecionados: RecyclerView
    private lateinit var imageGrupo: ShapeableImageView
    private val storage: StorageReference = FirebaseConfig.storage
    private lateinit var grupo: Grupo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Novo Grupo"
        binding.toolbar.subtitle = "Defina o nome"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerMembrosSelecionados = binding.contentCadastroGrupo.recyclerMembrosGrupo
        imageGrupo = binding.contentCadastroGrupo.ImageGrupo
        grupo = Grupo()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imagem: Bitmap?
                    try {
                        val localImagemSelecionada = data!!.data
                        imagem = when {
                            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                localImagemSelecionada
                            )
                            else -> {
                                val source = ImageDecoder.createSource(
                                    this.contentResolver,
                                    localImagemSelecionada!!
                                )
                                ImageDecoder.decodeBitmap(source)
                            }
                        }
                        if (imagem != null) {
                            imageGrupo.setImageBitmap(imagem)
                            val baos = ByteArrayOutputStream()
                            imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val dadosImagem = baos.toByteArray()

                            //Salvando a imagem no FirebaseStorage
                            val imagemRef = storage.child("imagens")
                                .child("grupos")
                                .child("${grupo.id}.jpeg")

                            val uploadTask = dadosImagem.let { imagemRef.putBytes(it) }
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
                                    val url = it.result.toString()
                                    grupo.foto = url
                                    Glide.with(this).load(url).into(imageGrupo)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        imageGrupo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            packageManager?.let {
                if (intent.resolveActivity(it) != null)
                    resultLauncher.launch(intent)
            }
        }

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //Recuperar lista de membros passada
        if (intent.extras != null) {
            val membros = intent.extras!!.getSerializable("membros") as List<Usuario>
            listaMembrosSelecionados.addAll(membros)
            binding.contentCadastroGrupo.textTotalParticipantes.text =
                "Participantes: ${listaMembrosSelecionados.size}"
        }

        //Configurar o recyclerview
        //Configurando recyclerview para os membros selecionados
        grupoSelecionadoAdapter =
            GrupoSelecionadoAdapter(listaMembrosSelecionados, applicationContext)

        val layoutManagerHorizontal =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerMembrosSelecionados.layoutManager = layoutManagerHorizontal
        recyclerMembrosSelecionados.setHasFixedSize(true)
        recyclerMembrosSelecionados.adapter = grupoSelecionadoAdapter
    }
}