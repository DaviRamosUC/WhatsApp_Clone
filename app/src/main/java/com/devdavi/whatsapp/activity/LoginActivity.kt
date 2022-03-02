package com.devdavi.whatsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devdavi.whatsapp.databinding.ActivityLoginBinding
import com.devdavi.whatsapp.model.Usuario
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val autenticacao = FirebaseConfig.autenticacao
    private val database = FirebaseConfig.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cadastroTextView.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (autenticacao.currentUser != null) {
            abrirTelaPrincipal()
        }
    }

    fun realizaLogin(view: View) {
        val emailUsuario: String = binding.editEmail.text.toString()
        val senhaUsuario: String = binding.editSenha.text.toString()
        if (verificaCampos(emailUsuario, senhaUsuario)) {
            val usuario = Usuario(emailUsuario, senhaUsuario)
            autenticacao.signInWithEmailAndPassword(usuario.email, usuario.senha)
                .addOnSuccessListener {
                    abrirTelaPrincipal()
                }
                .addOnFailureListener {
                    val excecao: String
                    try {
                        throw it
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        excecao = "Email e/ou senha errados"
                    } catch (e: FirebaseAuthInvalidUserException) {
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado"
                    } catch (e: Exception) {
                        excecao = "Erro ao efetuar login: " + e.message
                        e.printStackTrace()
                    }
                    Toast.makeText(applicationContext, excecao, Toast.LENGTH_LONG)
                        .show()
                }
        }

    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun verificaCampos(
        emailUsuario: String,
        senhaUsuario: String
    ): Boolean {
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