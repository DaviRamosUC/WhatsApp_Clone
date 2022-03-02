package com.devdavi.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.databinding.ActivityCadastroBinding
import com.devdavi.whatsapp.databinding.ActivityLoginBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.Base64Custom
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private var autenticacao = FirebaseConfig.autenticacao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun cadastrarUsuario(view: View) {
        val nomeUsuario: String = binding.editNomeCadastro.text.toString()
        val emailUsuario: String = binding.editEmailCadastro.text.toString()
        val senhaUsuario: String = binding.editSenhaCadastro.text.toString()

        if (verificaCampos(nomeUsuario, emailUsuario, senhaUsuario)) {
            val usuario = Usuario(null, nomeUsuario, emailUsuario, senhaUsuario)
            autenticacao.createUserWithEmailAndPassword(usuario.email, usuario.senha)
                .addOnSuccessListener {
                    try {
                        val identificadorUsuario: String =
                            Base64Custom.codificarBase64(usuario.email)
                        usuario.id = identificadorUsuario
                        usuario.salvar()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    Toast.makeText(this, "Sucesso ao cadastrar o usuário", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    val excecao: String
                    try {
                        throw it
                    } catch (it: FirebaseAuthWeakPasswordException) {
                        excecao = "Digite uma senha mais forte"
                    } catch (it: FirebaseAuthInvalidCredentialsException) {
                        excecao = "Por favor, digite um e-mail válido"
                    } catch (it: Exception) {
                        excecao = "Erro ao cadastrar usuário" + it.message
                        it.printStackTrace()
                    }
                    Toast.makeText(this, excecao, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun verificaCampos(
        nomeUsuario: String,
        emailUsuario: String,
        senhaUsuario: String
    ): Boolean {
        if (nomeUsuario.isEmpty() || nomeUsuario.isBlank()) {
            Toast.makeText(this, "Por favor, preencha o campo nome", Toast.LENGTH_LONG).show()
            return false
        }
        if (emailUsuario.isEmpty() || emailUsuario.isBlank()) {
            Toast.makeText(this, "Por favor, preencha o campo email", Toast.LENGTH_LONG).show()
            return false
        }
        if (senhaUsuario.isEmpty() || senhaUsuario.isBlank()) {
            Toast.makeText(this, "Por favor, preencha o campo senha", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

}