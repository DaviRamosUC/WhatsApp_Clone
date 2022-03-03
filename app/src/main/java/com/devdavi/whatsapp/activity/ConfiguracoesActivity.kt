package com.devdavi.whatsapp.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
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

        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                }
            }

        binding.imageButtonCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null)
                resultLauncher.launch(intent)
        }
        binding.imageButtonGaleria.setOnClickListener {

        }
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