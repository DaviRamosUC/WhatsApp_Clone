package com.devdavi.whatsapp.utils

import com.devdavi.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseConfig {
    var autenticacao: FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var storage: StorageReference = FirebaseStorage.getInstance().reference

}