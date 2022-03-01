package com.devdavi.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.adapter.FragmentAdapter
import com.devdavi.whatsapp.databinding.ActivityMainBinding
import com.devdavi.whatsapp.utils.FirebaseConfig
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var autenticacao = FirebaseConfig.autenticacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbarPrincipal)

//        Adicionando TabLayout a tela e setando o viewPager com adapter
        setupTabLayout()

    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        viewPager.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Conversas"
                else -> tab.text = "Contatos"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                deslogarUsuario()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.menuConfiguracoes -> {
                startActivity(Intent(this, ConfiguracoesActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deslogarUsuario() {
        try {
            autenticacao.signOut()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao deslogar o usuário", Toast.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }

}