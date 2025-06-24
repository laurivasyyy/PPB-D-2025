package com.example.sayurbox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sayurbox.data.dao.CartDao
import com.example.sayurbox.data.dao.OrderDao
import com.example.sayurbox.data.dao.ProductDao
import com.example.sayurbox.data.dao.UserDao
import com.example.sayurbox.data.models.CartItem
import com.example.sayurbox.data.models.Order
import com.example.sayurbox.data.models.Product
import com.example.sayurbox.data.models.User

@Database(
    entities = [Product::class, CartItem::class, User::class, Order::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sayurbox_database"
                )
                    .fallbackToDestructiveMigration() // For development only
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}