package com.mkves.tacna.ui

import com.example.tacna.R
import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.mkves.tacna.db.AppDatabase
import com.mkves.tacna.model.Event
import com.mkves.tacna.model.EventViewModelFactory
import com.mkves.tacna.viewmodel.EventViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var eventViewModel : EventViewModel
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val database = AppDatabase.getInstance(applicationContext)

        val eventDao = database.eventDao()
        val userFundsDao = database.userFundsDao()

        val factory = EventViewModelFactory(eventDao, userFundsDao)
        eventViewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        listView = findViewById(R.id.listView)

        lifecycleScope.launch(Dispatchers.IO) {
            loadEvents()
        }
    }

    private suspend fun loadEvents() {
        val db = AppDatabase.getInstance(applicationContext)
        val events = db.eventDao().getAllEvents()

        withContext(Dispatchers.Main) {
            adapter = EventAdapter(this@HistoryActivity, events)
            listView.adapter = adapter
        }
    }
}
