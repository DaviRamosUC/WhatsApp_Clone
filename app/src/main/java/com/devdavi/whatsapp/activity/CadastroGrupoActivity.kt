package com.devdavi.whatsapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devdavi.whatsapp.databinding.ActivityCadastroGrupoBinding
import com.devdavi.whatsapp.model.Usuario
import com.google.android.material.snackbar.Snackbar

class CadastroGrupoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroGrupoBinding
    private val listaMembrosSelecionados = ArrayList<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //Recuperar lista de membros passada
        if (intent.extras != null) {
            val membros = intent.extras!!.getSerializable("membros") as List<Usuario>
            listaMembrosSelecionados.addAll(membros)
            binding.contentCadastroGrupo.textTotal.text = listaMembrosSelecionados.size.toString()
        }
    }
}