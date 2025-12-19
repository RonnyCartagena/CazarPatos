package com.cartagena.ronny.cazarpatos

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferencesManager(context: Context) : FileHandler {
    // 1. Crear la Master Key
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    // 2. Crear la instancia de EncryptedSharedPreferences
    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs", // Nombre del archivo encriptado
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        encryptedPrefs.edit().apply {
            // Guardamos el email (primero) y el password (segundo) encriptados
            putString("EMAIL", datosAGrabar.first)
            putString("PASSWORD", datosAGrabar.second)
            apply()
        }
    }
    override fun ReadInformation(): Pair<String, String> {
        val email = encryptedPrefs.getString("EMAIL", "") ?: ""
        val password = encryptedPrefs.getString("PASSWORD", "") ?: ""
        return Pair(email, password)
    }
}


