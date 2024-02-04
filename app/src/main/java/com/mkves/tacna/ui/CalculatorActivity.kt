package com.mkves.tacna.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tacna.R

class CalculatorActivity : AppCompatActivity() {
    private lateinit var et500: EditText
    private lateinit var et200: EditText
    private lateinit var et100: EditText
    private lateinit var et50: EditText
    private lateinit var et20: EditText
    private lateinit var et10: EditText
    private lateinit var et5: EditText
    private lateinit var et2: EditText
    private lateinit var et1: EditText
    private lateinit var et0_5: EditText
    private lateinit var et0_2: EditText

    private lateinit var tvResult: TextView
    private lateinit var tvFali: TextView
    private var debt = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        // Initialize views
        et500 = findViewById(R.id.et_500)
        et200 = findViewById(R.id.et_200)
        et100 = findViewById(R.id.et_100)
        et50 = findViewById(R.id.et_50)
        et20 = findViewById(R.id.et_20)
        et10 = findViewById(R.id.et_10)
        et5 = findViewById(R.id.et_5)
        et2 = findViewById(R.id.et_2)
        et1 = findViewById(R.id.et_1)
        et0_5 = findViewById(R.id.et_0_5)
        et0_2 = findViewById(R.id.et_0_2)






        tvResult = findViewById(R.id.tv_result)
        tvFali = findViewById(R.id.tv_fali)

        debt = intent.getDoubleExtra("DEBT", 0.0)

        // Set TextWatchers
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateTotal()
            }
        }

        et500.addTextChangedListener(textWatcher)
        et200.addTextChangedListener(textWatcher)
        et100.addTextChangedListener(textWatcher)
        et50.addTextChangedListener(textWatcher)
        et20.addTextChangedListener(textWatcher)
        et10.addTextChangedListener(textWatcher)
        et5.addTextChangedListener(textWatcher)
        et2.addTextChangedListener(textWatcher)
        et1.addTextChangedListener(textWatcher)
        et0_5.addTextChangedListener(textWatcher)
        et0_2.addTextChangedListener(textWatcher)
    }

    private fun calculateTotal() {
        val total = getAmount(et500, 500.0) + getAmount(et200, 200.0) +
            getAmount(et100, 100.0) + getAmount(et50, 50.0) + getAmount(et20, 20.0) +
            getAmount(et10, 10.0) + getAmount(et5, 5.0) + getAmount(et2, 2.0) +
            getAmount(et1, 1.0) + getAmount(et0_5, 0.5) + getAmount(et0_2, 0.2)
        tvResult.text = "Total: $total"

        val fali = debt - total
        tvFali.text = "Fali: " + Math.abs(fali)

        if (total < debt) {
            tvResult.setTextColor(Color.RED)
            tvFali.setTextColor(Color.RED)
            tvFali.text = "Fali: +" + Math.abs(fali)
        } else {
            tvResult.setTextColor(Color.GREEN)
            tvFali.setTextColor(Color.GREEN)
            tvFali.text = "Fali: -" + Math.abs(fali)
        }
    }

    private fun getAmount(editText: EditText, value: Double): Double {
        val input = editText.text.toString()
        return if (input.isEmpty()) 0.0 else input.toDouble() * value
    }
}