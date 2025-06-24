package com.example.starbucksapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.starbucksapp.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private var selectedSize = "tall" // Default selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        loadProductData()
        setupSizeSelection()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show()
        }

        // Size selection listeners
        binding.btnSizeTall.setOnClickListener { selectSize("tall") }
        binding.btnSizeGrande.setOnClickListener { selectSize("grande") }
        binding.btnSizeVenti.setOnClickListener { selectSize("venti") }
    }

    private fun loadProductData() {
        val itemName = intent.getStringExtra("item_name")
        val itemDescription = intent.getStringExtra("item_description")
        val itemPrice = intent.getStringExtra("item_price")

        binding.tvProductName.text = itemName ?: getString(R.string.product_name_placeholder)
        binding.tvProductDescription.text = itemDescription ?: getString(R.string.product_description_placeholder)
        binding.tvProductPrice.text = itemPrice ?: getString(R.string.price_placeholder)
    }

    private fun setupSizeSelection() {
        // Set initial selection
        selectSize("tall")
    }

    private fun selectSize(size: String) {
        selectedSize = size

        // Reset all buttons to default state
        resetSizeButtons()

        // Highlight selected button
        when (size) {
            "tall" -> {
                binding.btnSizeTall.setBackgroundResource(R.drawable.button_green)
                binding.btnSizeTall.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "grande" -> {
                binding.btnSizeGrande.setBackgroundResource(R.drawable.button_green)
                binding.btnSizeGrande.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            "venti" -> {
                binding.btnSizeVenti.setBackgroundResource(R.drawable.button_green)
                binding.btnSizeVenti.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }

    private fun resetSizeButtons() {
        val grayColor = ContextCompat.getColor(this, R.color.gray)

        binding.btnSizeTall.setBackgroundResource(R.drawable.card_background)
        binding.btnSizeTall.setTextColor(grayColor)

        binding.btnSizeGrande.setBackgroundResource(R.drawable.card_background)
        binding.btnSizeGrande.setTextColor(grayColor)

        binding.btnSizeVenti.setBackgroundResource(R.drawable.card_background)
        binding.btnSizeVenti.setTextColor(grayColor)
    }
}