package com.example.utilityapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Converter : AppCompatActivity() {

    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView

    private val currencies = arrayOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD")
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.84,
        "GBP" to 0.72,
        "JPY" to 109.65,
        "CAD" to 1.25,
        "AUD" to 1.31
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        amountEditText = findViewById(R.id.amountEditText)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amount = amountEditText.text.toString().toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()

        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0

        val convertedAmount = amount / fromRate * toRate
        val formattedResult = String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency)

        resultTextView.text = formattedResult
    }
}