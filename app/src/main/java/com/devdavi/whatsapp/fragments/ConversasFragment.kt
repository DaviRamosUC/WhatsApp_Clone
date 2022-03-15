package com.devdavi.whatsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdavi.whatsapp.activity.ChatActivity
import com.devdavi.whatsapp.adapter.ConversasAdapter
import com.devdavi.whatsapp.databinding.FragmentConversasBinding
import com.devdavi.whatsapp.model.Conversa
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.devdavi.whatsapp.utils.RecyclerItemClickListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ConversasFragment : Fragment() {

    private lateinit var binding: FragmentConversasBinding
    private lateinit var recyclerViewConversas: RecyclerView
    private lateinit var adapter: ConversasAdapter
    private lateinit var conversasRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private val listaConversas: ArrayList<Conversa> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val identificadorUsuario = Helpers.getIdentificadoUsuario()
        database = FirebaseConfig.database.reference
        conversasRef = database.child("conversas").child(identificadorUsuario)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversasBinding.inflate(layoutInflater, container, false)

        recyclerViewConversas = binding.recyclerListaConversas

        //Configurando adapter
        adapter = ConversasAdapter(listaConversas, context)

        //Configurando recyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerViewConversas.layoutManager = layoutManager
        recyclerViewConversas.setHasFixedSize(true)
        recyclerViewConversas.adapter = adapter

        recyclerViewConversas.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                recyclerViewConversas,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val conversaSelecionada = listaConversas[position]
                        val intent = Intent(activity, ChatActivity::class.java)
                        intent.putExtra("chatContato", conversaSelecionada.usuarioExibicao)
                        startActivity(intent)
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

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        recuperarConversas()
    }

    override fun onStop() {
        super.onStop()
        conversasRef.removeEventListener(childEventListener)
    }

    fun recuperarConversas() {
        childEventListener = conversasRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val conversa = snapshot.getValue(Conversa::class.java)
                if (conversa != null) {
                    listaConversas.add(conversa)
                }
                adapter.notifyDataSetChanged()
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