package com.devdavi.whatsapp.activity

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.devdavi.whatsapp.databinding.ActivityConfiguracoesBinding
import com.devdavi.whatsapp.utils.Permissao

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracoesBinding
    private var permissoesNecessarias = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Permissao.validarPermissoes(permissoesNecessarias, this, 1)

        val toolbar = binding.toolbar.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        /*
        Configura a ação de voltar na activity
        é necessário configurar a activity mother desta
        ou seja -> a MainActivity é mãe da Configurações activity */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao()
            }
        }
    }

    private fun alertaValidacaoPermissao() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Permissões Negadas")
        builder.setCancelable(false)
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões")
        builder.setPositiveButton(
            "Confirmar"
        ) { dialogInterface, i -> finish() }.create().show()
    }

}