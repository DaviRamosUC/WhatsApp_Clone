package com.devdavi.whatsapp.model

data class Usuario(var nome: String?, var email: String, var senha: String) {
    constructor(emailUsuario: String, senhaUsuario: String) : this(null, emailUsuario, senhaUsuario)
}