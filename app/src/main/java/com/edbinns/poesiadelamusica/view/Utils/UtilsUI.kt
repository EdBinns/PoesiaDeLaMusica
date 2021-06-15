package com.edbinns.poesiadelamusica.view.Utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.edbinns.poesiadelamusica.R
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

fun String.showLongMessage(context: Context){
    Toast.makeText(context,this, Toast.LENGTH_LONG).show()
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