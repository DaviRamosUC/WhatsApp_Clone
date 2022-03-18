package com.devdavi.whatsapp.model

import com.devdavi.whatsapp.utils.FirebaseConfig
import java.io.Serializable

data class Grupo(
    var id: String?,
    var nome: String?,
    var foto: String?,
    var membros: List<Usuario>?,
) : Serializable {
    constructor() : this(null, null, null, null)

    init {
        val database = FirebaseConfig.database.reference
        val grupoRef = database.child("grupos")
        val idFirebase = grupoRef.push().key
        this.id = idFirebase
    }

}
