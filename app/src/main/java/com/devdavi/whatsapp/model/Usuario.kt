package com.devdavi.whatsapp.model

import com.devdavi.whatsapp.utils.FirebaseConfig
import com.devdavi.whatsapp.utils.Helpers
import com.google.firebase.database.Exclude
import java.io.Serializable
import kotlin.collections.HashMap

data class Usuario(
    @get:Exclude var id: String?,
    var nome: String?,
    var email: String?,
    @get:Exclude var senha: String?,
    var foto: String?
) : Serializable {
    constructor() : this(null, null, null, null, null)

    constructor(emailUsuario: String, senhaUsuario: String) : this(
        null,
        null,
        emailUsuario,
        senhaUsuario,
        null
    )

    fun salvar() {
        val databaseReferece = FirebaseConfig.database.reference
        id?.let {
            databaseReferece.child("usuarios")
                .child(it)
                .setValue(this)
        }
    }

    fun atualizar() {
        val identificadorUsuario = Helpers.getIdentificadoUsuario()
        val database = FirebaseConfig.database.reference

        val usuariosRef = database.child("usuarios").child(identificadorUsuario)
        val usuarioMap = converterParaMap()
        usuariosRef.updateChildren(usuarioMap)
    }

    @Exclude
    fun converterParaMap(): Map<String, Any?> {
        val usuarioMap = HashMap<String, Any?>()
        usuarioMap.put("email", email)
        usuarioMap.put("nome", nome)
        usuarioMap.put("foto", foto)
        return usuarioMap
    }


}