package com.mkves.tacna.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.tacna.R
import com.mkves.tacna.db.AppDatabase
import com.mkves.tacna.model.Event
import com.mkves.tacna.model.EventViewModelFactory
import com.mkves.tacna.viewmodel.EventViewModel
import java.lang.StringBuilder
import kotlinx.coroutines.launch

class UpdateDrinksActivity : AppCompatActivity() {
    private lateinit var eventViewModel: EventViewModel

    private var numBeers = 0
    private var numJuices = 0
    private var numShots = 0

    private var beerAdditions = mutableListOf<Int>()
    private var juiceAdditions = mutableListOf<Int>()
    private var shotAdditions = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_drinks)

        val database = AppDatabase.getInstance(applicationContext)

        val eventDao = database.eventDao()
        val userFundsDao = database.userFundsDao()

        val factory = EventViewModelFactory(eventDao, userFundsDao)
        eventViewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        val eventId = intent.getIntExtra("EVENT_ID", -1)

        loadEventFromDB(eventId)

        val textViewNumBeers = findViewById<TextView>(R.id.textViewNumBeers)
        val textViewNumJuices = findViewById<TextView>(R.id.textViewNumJuices)
        val textViewNumShots = findViewById<TextView>(R.id.textViewNumShots)

        textViewNumBeers.text = "Pive: $numBeers"
        textViewNumJuices.text = "Sokovi: $numJuices"
        textViewNumShots.text = "Kratka pića: $numShots"

        // Addition of beers
        findViewById<Button>(R.id.buttonAddBeer).setOnClickListener {
            showAddDrinksDialog("Pivo") { count ->
                numBeers += count
                beerAdditions.add(count)
                textViewNumBeers.text = "Pive: $numBeers"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        findViewById<Button>(R.id.buttonAddJuice).setOnClickListener {
            showAddDrinksDialog("Sokove") { count ->
                numJuices += count
                juiceAdditions.add(count)
                textViewNumJuices.text = "Sokovi: $numJuices"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        findViewById<Button>(R.id.buttonAddShots).setOnClickListener {
            showAddDrinksDialog("Kratka Pića") { count ->
                numShots += count
                shotAdditions.add(count)
                textViewNumShots.text = "Kratka pića: $numShots"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        // Handling remove of drinks

        findViewById<Button>(R.id.buttonRemoveBeer).setOnClickListener {
            showRemoveDrinksDialog("Pivo") { count ->
                numBeers -= count
                beerAdditions.add(-count)
                textViewNumBeers.text = "Pive: $numBeers"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        findViewById<Button>(R.id.buttonRemoveJuice).setOnClickListener {
            showRemoveDrinksDialog("Sokove") { count ->
                numJuices -= count
                juiceAdditions.add(-count)
                textViewNumJuices.text = "Sokovi: $numJuices"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        findViewById<Button>(R.id.buttonRemoveShots).setOnClickListener {
            showRemoveDrinksDialog("Kratka pića") { count ->
                numShots -= count
                shotAdditions.add(-count)
                textViewNumShots.text = "Kratka pića: $numShots"
                lifecycleScope.launch{
                    updateEvent(eventId)
                }
            }
        }

        findViewById<Button>(R.id.buttonViewHistory).setOnClickListener {
            showHistoryOfAdditions()
        }


        val finishEvent = findViewById<Button>(R.id.buttonFinish)
        finishEvent.setOnClickListener {
            val intent = Intent(this, FinishEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
        }
    }

    private fun showHistoryOfAdditions() {
        val messageBuilder = StringBuilder()

        // Show beers
        messageBuilder.append("Pivo: ")
        if (beerAdditions.isNotEmpty()) {
            messageBuilder.append(beerAdditions.joinToString(", ", "[", "]"))
        } else {
            messageBuilder.append("Nije dodana niti jedna piva")
        }
        messageBuilder.append("\n\n")

        // Show juices
        messageBuilder.append("Sokovi: ")
        if (juiceAdditions.isNotEmpty()) {
            messageBuilder.append(juiceAdditions.joinToString(", ", "[", "]"))
        } else {
            messageBuilder.append("Nije dodan niti jedan sok")
        }
        messageBuilder.append("\n\n")

        // Show shots
        messageBuilder.append("Kratka pića: ")
        if (shotAdditions.isNotEmpty()) {
            messageBuilder.append(shotAdditions.joinToString(", ", "[", "]"))
        } else {
            messageBuilder.append("Nije dodano niti jedno kratko piće")
        }
        messageBuilder.append("\n\n")


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Lista")
        builder.setMessage(messageBuilder.toString())
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showAddDrinksDialog(drinkType: String, onDrinkAdded: (Int) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Dodaj $drinkType")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Dodaj") { dialog, _ ->
            val count = input.text.toString().toIntOrNull() ?: 0
            onDrinkAdded(count)
            dialog.dismiss()
        }

        builder.setNegativeButton("Odustani") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun showRemoveDrinksDialog(drinkType: String, onDrinkAdded: (Int) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Otpiši $drinkType")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Otpiši") { dialog, _ ->
            val count = input.text.toString().toIntOrNull() ?: 0
            onDrinkAdded(count)
            dialog.dismiss()
        }

        builder.setNegativeButton("Odustani") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun updateEvent(eventId: Int) {
        lifecycleScope.launch {
            val currentEvent = eventViewModel.getEventById(eventId) { currentEvent ->
                currentEvent.beerHistory = beerAdditions
                currentEvent.juiceHistory = juiceAdditions
                currentEvent.shotsHistory = shotAdditions

                currentEvent.numBeers = beerAdditions.sum()
                currentEvent.numJuices = juiceAdditions.sum()
                currentEvent.numShots = shotAdditions.sum()

                eventViewModel.updateEvent(currentEvent)
            }
        }
    }

    private fun loadEventFromDB(eventId: Int) {
        Log.d("Event to be reached from DB", eventId.toString())
        lifecycleScope.launch{
            val currentEvent = eventViewModel.getEventById(eventId) {currentEvent ->
                beerAdditions = currentEvent.beerHistory.toMutableList()
                juiceAdditions = currentEvent.juiceHistory.toMutableList()
                shotAdditions = currentEvent.shotsHistory.toMutableList()

                numBeers = beerAdditions.sum()
                numJuices = juiceAdditions.sum()
                numShots = shotAdditions.sum()

                val textViewNumBeers = findViewById<TextView>(R.id.textViewNumBeers)
                val textViewNumJuices = findViewById<TextView>(R.id.textViewNumJuices)
                val textViewNumShots = findViewById<TextView>(R.id.textViewNumShots)

                textViewNumBeers.text = "Pive: $numBeers"
                textViewNumJuices.text = "Sokovi: $numJuices"
                textViewNumShots.text = "Kratka pića: $numShots"
            }
        }
    }
}
