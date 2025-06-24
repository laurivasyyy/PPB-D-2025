package com.example.starbucksapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starbucksapp.databinding.ActivityStoreLocatorBinding

class StoreLocatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreLocatorBinding
    private lateinit var storeAdapter: StoreAdapter
    private val stores = mutableListOf<Store>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreLocatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        createStoreList()
        storeAdapter = StoreAdapter(stores)

        binding.recyclerViewStores.apply {
            layoutManager = LinearLayoutManager(this@StoreLocatorActivity)
            adapter = storeAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun createStoreList() {
        stores.addAll(listOf(
            Store("Starbucks Surabaya Central", "Jl. Tunjungan No.1, Surabaya", "0.2 km", "Open until 10:00 PM"),
            Store("Starbucks Galaxy Mall", "Jl. Dharmahusada Indah Timur, Surabaya", "1.5 km", "Open until 9:00 PM"),
            Store("Starbucks Pakuwon Mall", "Jl. Puncak Indah Lontar, Surabaya", "3.2 km", "Open until 10:00 PM"),
            Store("Starbucks Ciputra World", "Jl. Mayjend Sungkono, Surabaya", "4.1 km", "Open until 9:30 PM"),
            Store("Starbucks Lenmarc Mall", "Jl. Kusuma Bangsa, Surabaya", "2.8 km", "Open until 9:00 PM"),
            Store("Starbucks Suramadu Food Court", "Jl. Ahmad Yani, Surabaya", "5.1 km", "Open until 9:00 PM"),
            Store("Starbucks Supermall Pakuwon", "Jl. Puncak Indah, Surabaya", "3.8 km", "Open until 10:00 PM")
        ))
    }
}