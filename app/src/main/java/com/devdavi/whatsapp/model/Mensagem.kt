package com.devdavi.whatsapp.model

import java.io.Serializable

data class Mensagem(
    var idUsuario: String? = "",
    var mensagem: String? = "",
    var imagem: String? = null
) : Serializable
