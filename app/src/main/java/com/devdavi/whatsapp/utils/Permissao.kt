package com.devdavi.whatsapp.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devdavi.whatsapp.activity.ConfiguracoesActivity

object Permissao {

    fun validarPermissoes(permissoes: Array<String>, context: Activity, requestCode: Int): Boolean {

        //Verificar versão do Android no dispositivo
        if (Build.VERSION.SDK_INT >= 23) {
            val listaPermissoes = ArrayList<String>()

            /*
            * Percorre as permissões passadas,
            * verificando uma a uma
            * se já tem a permissão liberada */
            for (permissao in permissoes) {
                val temPermissao = ContextCompat.checkSelfPermission(
                    context,
                    permissao
                ) == PackageManager.PERMISSION_GRANTED

                if (!temPermissao) listaPermissoes.add(permissao)
            }

            if (listaPermissoes.isEmpty()) return true
            val novasPermissoes: Array<String> = listaPermissoes.toTypedArray()
            ActivityCompat.requestPermissions(context, novasPermissoes, requestCode)
        }

        return true
    }
}