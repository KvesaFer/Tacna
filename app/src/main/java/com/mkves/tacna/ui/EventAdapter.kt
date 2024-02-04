package com.mkves.tacna.ui

// EventAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tacna.R
import com.mkves.tacna.model.Event
import kotlin.math.round

class EventAdapter(context: Context, private val events: List<Event>) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.list_item_event, parent, false)

        val event = getItem(position)
        if (event != null) {
            view.findViewById<TextView>(R.id.textViewEventName).text = event?.eventName
            view.findViewById<TextView>(R.id.textViewSales).text = "Sales: ${event.sales}"
            view.findViewById<TextView>(R.id.textViewPayout).text = "Payout: ${round(event.payout * 100) / 100}"
        }
        return view
    }
}
