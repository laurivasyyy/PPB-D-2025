package com.example.currencyconverter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class CurrencyConverterViewModel : ViewModel() {

    private val _exchangeRate = MutableStateFlow(
        ExchangeRate(
            fromCurrency = "IDR",
            toCurrency = "JPY",
            rate = 0.0067,
            lastUpdated = Date()
        )
    )
    val exchangeRate: StateFlow<ExchangeRate> = _exchangeRate.asStateFlow()

    private val _conversionResult = MutableStateFlow<ConversionResult?>(null)
    val conversionResult: StateFlow<ConversionResult?> = _conversionResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun convertCurrency(amountInIDR: Double) {
        _isLoading.value = true

        // Simulate API call delay (remove in production)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val convertedAmount = amountInIDR * _exchangeRate.value.rate

            _conversionResult.value = ConversionResult(
                originalAmount = amountInIDR,
                convertedAmount = convertedAmount,
                fromCurrency = "IDR",
                toCurrency = "JPY",
                exchangeRate = _exchangeRate.value.rate
            )

            _isLoading.value = false
        }, 500) // 500ms delay to show loading state
    }

    fun clearResult() {
        _conversionResult.value = null
    }

    fun updateExchangeRate(newRate: Double) {
        _exchangeRate.value = _exchangeRate.value.copy(
            rate = newRate,
            lastUpdated = Date()
        )
    }
}