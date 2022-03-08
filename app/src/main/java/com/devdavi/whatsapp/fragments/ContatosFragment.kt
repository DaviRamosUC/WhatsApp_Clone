package com.devdavi.whatsapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.activity.ChatActivity
import com.devdavi.whatsapp.adapter.ContatosAdapter
import com.devdavi.whatsapp.databinding.FragmentContatosBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.devdavi.whatsapp.utils.RecyclerItemClickListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ContatosFragment : Fragment() {

    private lateinit var binding: FragmentContatosBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContatosAdapter
    private val listaContatos: ArrayList<Usuario> = ArrayList()
    private lateinit var usuariosRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var usuarioAtual: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuariosRef = FirebaseConfig.database.reference.child("usuarios")
        usuarioAtual = Helpers.getUsuarioAtual()!!
        recuperarContatos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatosBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewListaContatos

        //Configurar o adapter
        adapter = ContatosAdapter(listaContatos, activity)

        //Configurar o recyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val usuarioSelecionado = listaContatos[position]
                        val intent = Intent(activity, ChatActivity::class.java)
                        intent.putExtra("chatContato", usuarioSelecionado)
                        startActivity(intent)
                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {

                    }
                }


            )
        )

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        usuariosRef.removeEventListener(valueEventListener)
    }

    private fun recuperarContatos() {
        valueEventListener = usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dados in snapshot.children) {
                    val usuario = dados.getValue(Usuario::class.java)

                    val emailUsuarioAtual = usuarioAtual.email
                    if (!emailUsuarioAtual.equals(usuario!!.email)) {
                        listaContatos.add(usuario)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}