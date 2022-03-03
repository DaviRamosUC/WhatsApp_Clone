package com.devdavi.whatsapp.utils

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.lang.Exception

object Helpers {

    fun getIdentificadoUsuario(): String {
        var resultado: String = ""
        if (FirebaseConfig.autenticacao.currentUser != null) {
            val email: String? = FirebaseConfig.autenticacao.currentUser?.email
            resultado = Base64Custom.codificarBase64(email!!)
        }
        return resultado
    }

    fun getUsuarioAtual(): FirebaseUser? {
        val usuario: FirebaseAuth = FirebaseConfig.autenticacao
        return usuario.currentUser
    }

    fun atualizarNomeUsuario(nome: String): Boolean {
        try {
            val user = getUsuarioAtual()
            val profile: UserProfileChangeRequest =
                UserProfileChangeRequest.Builder().setDisplayName(nome).build()
            user!!.updateProfile(profile).addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("PERFIL", "Erro ao atualizar nome de perfil")
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun atualizarFotoUsuario(url: Uri): Boolean {
        try {
            val user = getUsuarioAtual()
            val profile: UserProfileChangeRequest =
                UserProfileChangeRequest.Builder().setPhotoUri(url).build()
            user!!.updateProfile(profile).addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d("PERFIL", "Erro ao atualizar foto de perfil")
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}