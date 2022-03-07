package com.devdavi.whatsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.adapter.ContatosAdapter
import com.devdavi.whatsapp.databinding.FragmentContatosBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.FirebaseConfig
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContatosBinding.inflate(inflater, container, false)
        usuariosRef = FirebaseConfig.database.reference.child("usuarios")
        recyclerView = binding.recyclerViewListaContatos
        //Configurar o adapter
        adapter = ContatosAdapter(listaContatos, activity)

        //Configurar o recyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recuperarContatos()
    }

    override fun onStop() {
        super.onStop()
        usuariosRef.removeEventListener(valueEventListener)
    }

    fun recuperarContatos() {
        valueEventListener = usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dados in snapshot.children) {
                    val usuario = dados.getValue(Usuario::class.java)
                    listaContatos.add(usuario!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}