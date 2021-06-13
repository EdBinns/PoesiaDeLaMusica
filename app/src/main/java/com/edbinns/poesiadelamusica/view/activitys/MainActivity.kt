package com.edbinns.poesiadelamusica.view.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.edbinns.poesiadelamusica.R
import com.edbinns.poesiadelamusica.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configNav()
        lifecycleScope.launch {

        }

    }

    private fun configNav() {
        NavigationUI.setupWithNavController(binding.bnvMenu, Navigation.findNavController(this, R.id.fragContent))
//
//        val navController = findNavController(R.id.fragContent)
//        binding.bnvMenu.setupWithNavController(navController)
    }
}