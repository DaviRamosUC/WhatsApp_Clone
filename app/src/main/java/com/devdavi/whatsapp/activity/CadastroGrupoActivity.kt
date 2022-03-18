package com.devdavi.whatsapp.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.adapter.GrupoSelecionadoAdapter
import com.devdavi.whatsapp.databinding.ActivityCadastroGrupoBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.RecyclerItemClickListener
import com.google.android.material.snackbar.Snackbar

class CadastroGrupoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroGrupoBinding
    private val listaMembrosSelecionados = ArrayList<Usuario>()
    private lateinit var grupoSelecionadoAdapter: GrupoSelecionadoAdapter
    private lateinit var recyclerMembrosSelecionados: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Novo Grupo"
        binding.toolbar.subtitle = "Defina o nome"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        recyclerMembrosSelecionados = binding.contentCadastroGrupo.recyclerMembrosGrupo

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