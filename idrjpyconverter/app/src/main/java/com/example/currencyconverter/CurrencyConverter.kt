package com.example.currencyconverter

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class CurrencyConverter {
    companion object {
        private const val IDR_TO_JPY_RATE = 0.0067 // 1 IDR = 0.0067 JPY
        private const val FROM_CURRENCY = "IDR"
        private const val TO_CURRENCY = "JPY"
    }

    private val decimalFormat = DecimalFormat("#,##0.00")
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    fun getExchangeRate(): ExchangeRate {
        return ExchangeRate(
            fromCurrency = FROM_CURRENCY,
            toCurrency = TO_CURRENCY,
            rate = IDR_TO_JPY_RATE,
            lastUpdated = Date()
        )
    }

    fun convertCurrency(amountInIDR: Double): ConversionResult {
        val convertedAmount = amountInIDR * IDR_TO_JPY_RATE

        return ConversionResult(
            originalAmount = amountInIDR,
            convertedAmount = convertedAmount,
            fromCurrency = FROM_CURRENCY,
            toCurrency = TO_CURRENCY,
            exchangeRate = IDR_TO_JPY_RATE
        )
    }

    fun formatCurrency(amount: Double, currencyCode: String): String {
        return when (currencyCode) {
            "IDR" -> "Rp ${decimalFormat.format(amount)}"
            "JPY" -> "¥ ${decimalFormat.format(amount)}"
            else -> "${decimalFormat.format(amount)} $currencyCode"
        }
    }

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun isValidAmount(amount: String): Boolean {
        return try {
            val value = amount.toDouble()
            value > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}