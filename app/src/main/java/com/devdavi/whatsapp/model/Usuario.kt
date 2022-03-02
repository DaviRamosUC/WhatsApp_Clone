package com.devdavi.whatsapp.model

import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.firebase.database.Exclude

data class Usuario(
    @get:Exclude var id: String?,
    var nome: String?,
    var email: String,
    @get:Exclude var senha: String
) {
    constructor(emailUsuario: String, senhaUsuario: String) : this(
        null,
        null,
        emailUsuario,
        senhaUsuario
    )

    fun salvar() {
        val databaseReferece = FirebaseConfig.database.reference
        id?.let {
            databaseReferece.child("usuarios")
                .child(it)
                .setValue(this)
        }
    }


}