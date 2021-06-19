package com.edbinns.poesiadelamusica.view.Utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.edbinns.poesiadelamusica.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.M)
fun String.copyToClipboard(context: Context, view: View){
    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copy text", this)
    clipboard.setPrimaryClip(clip)
    val message = "${ConstansUI.SUCCESSFULLY_ADDED} al portapapeles"
    message.showMessage(view, R.color.ok_color)
}
fun String.showLongMessage(context: Context){
    Toast.makeText(context,this, Toast.LENGTH_LONG).show()
}

@RequiresApi(Build.VERSION_CODES.M)
fun String.showMessage(view:View, color: Int){
    val sb = Snackbar.make(view, this, Snackbar.LENGTH_LONG)
    sb.view.setBackgroundColor(view.resources?.getColor(color, null)!!)
    sb.show()
}


fun LottieAnimationView.showAnim(click: Boolean, animation : Int){
    val anim = this
    CoroutineScope(Dispatchers.IO).launch {
        delay(TimeUnit.SECONDS.toMillis(3))
        withContext(Dispatchers.Main) {
            Log.i("TAG", "this will be called after 3 seconds")
            anim.visibility = View.INVISIBLE
        }
    }
    anim.visibility = View.VISIBLE
    auxAnimation(anim, animation, click)
}

private fun auxAnimation(
        imageView: LottieAnimationView,
        animation: Int,
        click: Boolean
): Boolean {

    if(!click){

        imageView.setAnimation(animation)
        imageView.repeatCount = 2
        imageView.playAnimation()
    }else{
        imageView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(R.drawable.ic_favorite_border)
                        imageView.alpha = 1f;
                    }
                })
    }

    return !click
}