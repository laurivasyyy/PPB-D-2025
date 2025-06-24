package com.example.starbucksapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starbucksapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        createMenuItems()
        menuAdapter = MenuAdapter(menuItems) { menuItem ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("item_name", menuItem.name)
                putExtra("item_description", menuItem.description)
                putExtra("item_price", menuItem.price)
            }
            startActivity(intent)
        }

        binding.recyclerViewMenu.apply {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = menuAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // Category tab listeners
        binding.tabDrinks.setOnClickListener { filterMenuByCategory("drink") }
        binding.tabFood.setOnClickListener { filterMenuByCategory("food") }
        binding.tabCoffee.setOnClickListener { filterMenuByCategory("coffee") }
        binding.tabMerchandise.setOnClickListener { filterMenuByCategory("merchandise") }

        // Bottom navigation - direct access (no include)
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        binding.navOrder.setOnClickListener { /* Already on menu */ }
        binding.navStores.setOnClickListener {
            startActivity(Intent(this, StoreLocatorActivity::class.java))
        }
        binding.navRewards.setOnClickListener {
            startActivity(Intent(this, RewardsActivity::class.java))
        }
    }

    private fun createMenuItems() {
        menuItems.addAll(listOf(
            MenuItem("Caramel Macchiato", "Freshly steamed milk with vanilla-flavored syrup", "$5.45", "drink"),
            MenuItem("Pike Place Roast", "Our signature medium roast coffee", "$3.45", "coffee"),
            MenuItem("Frappuccino® Blended Beverages", "Rich and creamy blended beverage", "$6.45", "drink"),
            MenuItem("Green Tea Latte", "Premium matcha green tea", "$4.95", "drink"),
            MenuItem("Americano", "Espresso shots topped with hot water", "$3.65", "coffee"),
            MenuItem("Cappuccino", "Dark, rich espresso with steamed milk foam", "$4.25", "coffee"),
            MenuItem("White Chocolate Mocha", "Espresso with white chocolate and steamed milk", "$5.75", "drink"),
            MenuItem("Cold Brew Coffee", "Slow-steeped for 20 hours", "$3.25", "coffee"),
            MenuItem("Blueberry Muffin", "Fresh baked with real blueberries", "$2.95", "food"),
            MenuItem("Chicken & Hummus Protein Box", "Antibiotic-free chicken with hummus", "$7.45", "food"),
            MenuItem("Starbucks Tumbler", "Reusable 16oz tumbler", "$19.95", "merchandise"),
            MenuItem("Coffee Beans - Pike Place", "Whole bean coffee, 1lb bag", "$12.95", "merchandise")
        ))
    }

    private fun filterMenuByCategory(category: String) {
        val filteredItems = if (category == "drink") {
            menuItems // Show all for drinks (default)
        } else {
            menuItems.filter { it.category == category }
        }

        menuAdapter = MenuAdapter(filteredItems) { menuItem ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("item_name", menuItem.name)
                putExtra("item_description", menuItem.description)
                putExtra("item_price", menuItem.price)
            }
            startActivity(intent)
        }
        binding.recyclerViewMenu.adapter = menuAdapter

        // Update tab colors
        updateTabColors(category)
    }

    private fun updateTabColors(selectedCategory: String) {
        // Reset all tabs to gray
        binding.tabDrinks.setTextColor(getColor(R.color.gray))
        binding.tabFood.setTextColor(getColor(R.color.gray))
        binding.tabCoffee.setTextColor(getColor(R.color.gray))
        binding.tabMerchandise.setTextColor(getColor(R.color.gray))

        // Highlight selected tab
        when (selectedCategory) {
            "drink" -> binding.tabDrinks.setTextColor(getColor(R.color.starbucks_green))
            "food" -> binding.tabFood.setTextColor(getColor(R.color.starbucks_green))
            "coffee" -> binding.tabCoffee.setTextColor(getColor(R.color.starbucks_green))
            "merchandise" -> binding.tabMerchandise.setTextColor(getColor(R.color.starbucks_green))
        }
    }
}