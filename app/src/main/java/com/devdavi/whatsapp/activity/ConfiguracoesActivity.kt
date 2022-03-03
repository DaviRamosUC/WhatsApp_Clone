package com.devdavi.whatsapp.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.devdavi.whatsapp.databinding.ActivityConfiguracoesBinding
import com.devdavi.whatsapp.utils.Permissao
import java.lang.Exception

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

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imagem: Bitmap?
                    try {
                        if (data?.data != null) {
                            val localImagemSelecionada = data.data
                            imagem = when {
                                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    localImagemSelecionada
                                )
                                else -> {
                                    val source = ImageDecoder.createSource(
                                        this.contentResolver,
                                        localImagemSelecionada!!
                                    )
                                    ImageDecoder.decodeBitmap(source)
                                }
                            }
                        } else {
                            imagem = result.data?.extras?.get("data") as Bitmap
                        }
                        if (imagem != null) this.binding.ImageViewUsuario.setImageBitmap(imagem)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        binding.imageButtonCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            packageManager?.let {
                if (intent.resolveActivity(it) != null)
                    resultLauncher.launch(intent)
            }

        }
        binding.imageButtonGaleria.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            packageManager?.let {
                if (intent.resolveActivity(it) != null)
                    resultLauncher.launch(intent)
            }
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