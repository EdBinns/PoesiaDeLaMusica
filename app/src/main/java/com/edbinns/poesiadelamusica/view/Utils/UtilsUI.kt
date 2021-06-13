package com.edbinns.poesiadelamusica.view.Utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun String.copyToClipboard( context: Context){
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copy text", this)
    clipboard.setPrimaryClip(clip)
    val message = "Se ha copiado en el portapapeles"
    message.showLongMessage(context)
}

fun String.showLongMessage(context: Context){
    Toast.makeText(context,this, Toast.LENGTH_LONG).show()
}
