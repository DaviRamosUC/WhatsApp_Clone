package com.devdavi.whatsapp.model

import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.firebase.database.Exclude
import java.io.Serializable

data class Conversa(
    @get:Exclude
    val idRemetente: String?,
    @get:Exclude
    val idDestinatario: String?,
    val ultimaMensagem: String?,
    val usuarioExibicao: Usuario?
) : Serializable {


    constructor() : this(null, null, null, null)

    fun salvar() {
        val database = FirebaseConfig.database.reference
        val conversaRef = database.child("conversas")
        conversaRef.child(idRemetente!!)
            .child(idDestinatario!!)
            .setValue(this)
    }
}
