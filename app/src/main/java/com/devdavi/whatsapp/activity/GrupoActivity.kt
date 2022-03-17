package com.devdavi.whatsapp.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.adapter.ContatosAdapter
import com.devdavi.whatsapp.adapter.GrupoSelecionadoAdapter
import com.devdavi.whatsapp.databinding.ActivityGrupoBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.devdavi.whatsapp.utils.RecyclerItemClickListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class GrupoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGrupoBinding
    private lateinit var recyclerMembros: RecyclerView
    private lateinit var recyclerMembrosSelecionados: RecyclerView
    private lateinit var contatosAdapter: ContatosAdapter
    private lateinit var grupoSelecionadoAdapter: GrupoSelecionadoAdapter
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var usuariosRef: DatabaseReference
    private lateinit var usuarioAtual: FirebaseUser
    private val listaMembros = ArrayList<Usuario>()
    private val listaMembrosSelecionados = ArrayList<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        usuarioAtual = Helpers.getUsuarioAtual()!!
        usuariosRef = FirebaseConfig.database.reference.child("usuarios")
        recyclerMembros = binding.contentGrupo.recyclerMembros
        recyclerMembrosSelecionados = binding.contentGrupo.recyclerMembrosSelecionados

        //Configurando adapter
        contatosAdapter = ContatosAdapter(listaMembros, applicationContext)

        //configurando o recyclerView
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerMembros.layoutManager = layoutManager
        recyclerMembros.setHasFixedSize(true)
        recyclerMembros.adapter = contatosAdapter

        recyclerMembros.addOnItemTouchListener(
            RecyclerItemClickListener(
                applicationContext,
                recyclerMembros,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val usuarioSelecionado = listaMembros[position]

                        //Remover usuario selecionado da lista
                        listaMembros.remove(usuarioSelecionado)
                        contatosAdapter.notifyDataSetChanged()

                        listaMembrosSelecionados.add(usuarioSelecionado)
                        grupoSelecionadoAdapter.notifyDataSetChanged()
                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        TODO("Not yet implemented")
                    }
                }
            )
        )

        //Configurando recyclerview para os membros selecionados
        grupoSelecionadoAdapter =
            GrupoSelecionadoAdapter(listaMembrosSelecionados, applicationContext)

        val layoutManagerHorizontal =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerMembrosSelecionados.layoutManager = layoutManagerHorizontal
        recyclerMembrosSelecionados.setHasFixedSize(true)
        recyclerMembrosSelecionados.adapter = grupoSelecionadoAdapter

    }

    override fun onStop() {
        super.onStop()
        usuariosRef.removeEventListener(valueEventListener)
    }

    override fun onStart() {
        super.onStart()
        recuperarContatos()
    }

    private fun recuperarContatos() {
        valueEventListener = usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dados in snapshot.children) {
                    val usuario = dados.getValue(Usuario::class.java)

                    val emailUsuarioAtual = usuarioAtual.email
                    if (!emailUsuarioAtual.equals(usuario!!.email)) {
                        listaMembros.add(usuario)
                    }

                }
                contatosAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}