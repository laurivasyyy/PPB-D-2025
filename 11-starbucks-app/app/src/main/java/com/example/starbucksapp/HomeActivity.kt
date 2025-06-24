package com.example.starbucksapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starbucksapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Quick action buttons
        binding.btnOrder.setOnClickListener { openMenuActivity() }
        binding.btnStores.setOnClickListener { openStoreLocator() }
        binding.btnRewards.setOnClickListener { openRewards() }

        // Bottom navigation - access through the included layout binding
        binding.navOrder.setOnClickListener { openMenuActivity() }
        binding.navStores.setOnClickListener { openStoreLocator() }
        binding.navRewards.setOnClickListener { openRewards() }
        binding.navHome.setOnClickListener { /* Already on home */ }
    }

    private fun openMenuActivity() {
        startActivity(Intent(this, MenuActivity::class.java))
    }

    private fun openStoreLocator() {
        startActivity(Intent(this, StoreLocatorActivity::class.java))
    }

    private fun openRewards() {
        startActivity(Intent(this, RewardsActivity::class.java))
    }
}