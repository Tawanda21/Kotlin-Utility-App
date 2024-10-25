package com.example.utilityapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarCard = findViewById<CardView>(R.id.calendarCard)
        val calculatorCard = findViewById<CardView>(R.id.calculatorCard)
        val converterCard = findViewById<CardView>(R.id.converterCard)
        val settingsCard = findViewById<CardView>(R.id.settingsCard)
        val helpCard = findViewById<CardView>(R.id.helpCard)
        val developerCard = findViewById<CardView>(R.id.developerCard)

        calendarCard.setOnClickListener {
            startActivity(Intent(this, Calendar::class.java))
        }

        calculatorCard.setOnClickListener {
            startActivity(Intent(this, Calculator::class.java))
        }

        converterCard.setOnClickListener {
            startActivity(Intent(this, Converter::class.java))
        }

        settingsCard.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        helpCard.setOnClickListener {
            startActivity(Intent(this, Help::class.java))
        }

        developerCard.setOnClickListener {
            startActivity(Intent(this, Developer::class.java))
        }
    }
}