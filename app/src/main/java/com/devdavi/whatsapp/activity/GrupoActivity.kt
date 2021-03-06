package com.devdavi.whatsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.R
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
import java.io.Serializable

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
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = binding.toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.fab.setOnClickListener {
            if (listaMembrosSelecionados.size >= 3) {
                val intent = Intent(applicationContext, CadastroGrupoActivity::class.java)
                intent.putExtra("membros", listaMembrosSelecionados as Serializable)
                startActivity(intent)
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "Adicione ao menos 3 pessoas para o grupo",
                    Toast.LENGTH_LONG
                ).show()
            }
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
                        atualizarMembrosToolbar()
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

        recyclerMembrosSelecionados.addOnItemTouchListener(
            RecyclerItemClickListener(
                applicationContext,
                recyclerMembrosSelecionados,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val usuario = listaMembrosSelecionados[position]
                        listaMembrosSelecionados.remove(usuario)
                        grupoSelecionadoAdapter.notifyDataSetChanged()

                        listaMembros.add(usuario)
                        contatosAdapter.notifyDataSetChanged()
                        atualizarMembrosToolbar()
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

    }

    fun atualizarMembrosToolbar() {
        val totalSelecionados = listaMembrosSelecionados.size
        val total = listaMembros.size + totalSelecionados
        toolbar.title = "$totalSelecionados de $total selecionados"
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
                atualizarMembrosToolbar()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}