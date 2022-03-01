package com.devdavi.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devdavi.whatsapp.R
import com.devdavi.whatsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbarPrincipal)
    }
}