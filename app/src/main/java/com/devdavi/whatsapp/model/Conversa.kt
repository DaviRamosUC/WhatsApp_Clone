package com.devdavi.whatsapp.model

import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.firebase.database.Exclude

data class Conversa(
    @Exclude
    val idRemetente: String,
    @Exclude
    val idDestinatario: String,
    val ultimaMensagem: String?,
    val usuarioExibicao: Usuario
) {
    fun salvar() {
        val database = FirebaseConfig.database.reference
        val conversaRef = database.child("conversas")
        conversaRef.child(idRemetente)
            .child(idDestinatario)
            .setValue(this)
    }
}
