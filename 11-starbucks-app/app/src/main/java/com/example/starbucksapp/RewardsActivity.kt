package com.example.starbucksapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starbucksapp.databinding.ActivityRewardsBinding

class RewardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setupRewardsData()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupRewardsData() {
        // Set current star count and progress
        binding.tvStarsCount.text = getString(R.string.current_stars_count)
        binding.tvNextReward.text = getString(R.string.stars_until_next_reward)
        binding.progressBar.progress = 75 // 125 out of 150 stars (83%)
    }
}