package com.cartagena.ronny.cazarpatos


import android.os.Environment
import android.content.Context
import java.io.*
import kotlin.concurrent.write
import kotlin.io.path.exists
import kotlin.text.append
import kotlin.text.first
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty
import kotlin.text.split

class FileExternalManager (private val context: Context) : FileHandler {
    // Nombre de archivo fijo donde guardaremos todo
    private val fileName = "data_externa.txt"
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    // Guarda información.
    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (!isExternalStorageWritable()) return
        try {
            val folder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            // Usamos el nombre fijo, no el que viene en datosAGrabar.first
            val file = File(folder, fileName)
            val fileOutputStream = FileOutputStream(file)
            val writer = OutputStreamWriter(fileOutputStream)
            val contenidoAGuardar = "${datosAGrabar.first},${datosAGrabar.second}"
            writer.write(contenidoAGuardar)
            writer.flush()
            writer.close()
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    // Lee la información y la devuelve separada.
    override fun ReadInformation(): Pair<String, String> {
        val contenido = readSpecificExternalFile(fileName)
        // Si no hay contenido, devolvemos vacíos
        if (contenido.isEmpty()) return Pair("", "")
        // Separamos el contenido que guardamos (Email,Password)
        val partes = contenido.split(",")
        val email = if (partes.isNotEmpty()) partes[0] else ""
        val password = if (partes.size > 1) partes[1] else ""
        return Pair(email, password)
    }
    fun readSpecificExternalFile(fileName: String): String {
        if (!Environment.getExternalStorageState()
                .let { it == Environment.MEDIA_MOUNTED || it ==
                        Environment.MEDIA_MOUNTED_READ_ONLY }
        ) {
            return ""
        }
        return try {
            val folder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(folder, fileName)
            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val reader = BufferedReader(InputStreamReader(fileInputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                fileInputStream.close()
                stringBuilder.toString()
            } else {
                ""
            }
        }catch (e: java.lang.Exception){
            ""
        }
    }
}