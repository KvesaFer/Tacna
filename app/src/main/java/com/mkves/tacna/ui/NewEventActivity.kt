package com.mkves.tacna.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.mkves.tacna.model.Event
import com.example.tacna.R
import com.mkves.tacna.db.AppDatabase
import com.mkves.tacna.model.EventViewModelFactory
import com.mkves.tacna.viewmodel.EventViewModel
import kotlin.math.log

class NewEventActivity : AppCompatActivity() {
    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)

        val database = AppDatabase.getInstance(applicationContext)

        val eventDao = database.eventDao()
        val userFundsDao = database.userFundsDao()

        val factory = EventViewModelFactory(eventDao, userFundsDao)
        eventViewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]


        val buttonSaveEvent = findViewById<Button>(R.id.buttonSaveEvent)
        buttonSaveEvent.setOnClickListener {
            val eventName = findViewById<EditText>(R.id.editTextEventName).text.toString()
            val beerPrice = findViewById<EditText>(R.id.editTextBeerPrice).text.toString().toDoubleOrNull() ?: 0.0
            val juicePrice = findViewById<EditText>(R.id.editTextJuicePrice).text.toString().toDoubleOrNull() ?: 0.0
            val shotsPrice = findViewById<EditText>(R.id.editTextShotsPrice).text.toString().toDoubleOrNull() ?: 0.0
            val payoutPercentage = findViewById<EditText>(R.id.editTextPayoutPercentage).text.toString().toDoubleOrNull() ?: 0.0
            val myDeposit = findViewById<EditText>(R.id.editTextMyDeposit).text.toString().toDoubleOrNull() ?: 0.0
            val givenDeposit = findViewById<EditText>(R.id.editTextGivenDeposit).text.toString().toDoubleOrNull() ?: 0.0

            // Create Event object
            val newEvent = Event(eventName, beerPrice, juicePrice, shotsPrice, payoutPercentage)

            // Save to database
            eventViewModel.createOrUpdateEvent(newEvent, myDeposit, givenDeposit)
            eventViewModel.eventIdLiveData.observe(this, Observer { eventId ->
                val startWorking = findViewById<Button>(R.id.buttonSaveEvent)
                startWorking.setOnClickListener {
                    val intent = Intent(this, UpdateDrinksActivity::class.java)
                    intent.putExtra("EVENT_ID", eventId)
                    startActivity(intent)
                }
            })
        }
    }
}