package com.example.currencyconverter

import  java.text.SimpleDateFormat
import java.util.*

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val lastUpdated: Date = Date()
)

data class ConversionResult(
    val originalAmount: Double,
    val convertedAmount: Double,
    val fromCurrency: String,
    val toCurrency: String,
    val exchangeRate: Double
)