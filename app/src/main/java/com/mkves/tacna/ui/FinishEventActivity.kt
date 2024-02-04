package com.mkves.tacna.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tacna.R
import com.mkves.tacna.db.AppDatabase
import com.mkves.tacna.model.EventViewModelFactory
import com.mkves.tacna.viewmodel.EventViewModel
import kotlin.math.round
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class FinishEventActivity : AppCompatActivity() {
    private lateinit var eventViewModel : EventViewModel
    private var debt = -1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_event)

        val database = AppDatabase.getInstance(applicationContext)

        val eventDao = database.eventDao()
        val userFundsDao = database.userFundsDao()

        val factory = EventViewModelFactory(eventDao, userFundsDao)
        eventViewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        val textViewSales = findViewById<TextView>(R.id.textViewSales)
        val textViewDebt = findViewById<TextView>(R.id.textViewDebt)
        val textViewTotalAmount = findViewById<TextView>(R.id.textViewTotal)
        val textViewPayout = findViewById<TextView>(R.id.textViewPayout)


        lifecycleScope.launch {
            val currentUserFunds = eventViewModel.getUserFundsByEventId(eventId) { userFunds ->
                val currentEvent = eventViewModel.getEventById(eventId) { event ->
                    var total = event.sales + userFunds.givenDeposit + userFunds.myDeposit
                    debt = event.sales + userFunds.givenDeposit

                    textViewTotalAmount.text = "Total (sve): ${ round(total * 100) / 100 }"
                    textViewDebt.text = "Njihov novac: ${ round(debt * 100) / 100 }"
                    textViewSales.text = "Promet: ${ round(event.sales * 100) / 100 }"
                    textViewPayout.text = "Isplata: ${ round(event.payout * 100) / 100 }"
                }
            }
        }

        val calculatorEvent = findViewById<Button>(R.id.buttonOpenCalculator)
        calculatorEvent.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            intent.putExtra("DEBT", debt)
            startActivity(intent)
        }
    }
}