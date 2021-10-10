package com.edbinns.poesiadelamusica.view.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.ActivityMainBinding
import com.edbinns.poesiadelamusica.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    override fun onResume() {
        super.onResume()

        val animIcon = AnimationUtils.loadAnimation(this, R.anim.splash_screen_image)
        binding.imvSplash.startAnimation(animIcon)

        val animText1 = AnimationUtils.loadAnimation(this, R.anim.splash_screen_text)
        binding.tvSplash.startAnimation(animText1)

        val animText2 = AnimationUtils.loadAnimation(this, R.anim.splash_screen_text_2)
        binding.tvSplash2.startAnimation(animText2)
        val intent = Intent(this, MainActivity::class.java)
        animIcon.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intent)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
    }
}