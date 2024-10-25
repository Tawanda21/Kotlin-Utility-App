package com.example.utilityapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Calendar : AppCompatActivity() {
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var addEventButton: FloatingActionButton
    private val eventsList = mutableListOf<Event>()
    private lateinit var eventsAdapter: EventsAdapter
    private var selectedDate: String = ""
    private val PREFS_NAME = "CalendarEvents"
    private val EVENTS_KEY = "events"
    private val gson = Gson()
    private val allEvents = mutableListOf<Event>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val eventText = findViewById<TextView>(R.id.calendarTitle)
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        addEventButton = findViewById(R.id.addEventButton)

        loadEvents()

        eventsAdapter = EventsAdapter(eventsList)
        eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Calendar)
            adapter = eventsAdapter
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            eventText.text = "Selected Date: $selectedDate"
            updateEventsList(selectedDate)
        }

        addEventButton.setOnClickListener {
            showAddEventDialog()
        }
    }

    private fun loadEvents() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val eventsJson = sharedPreferences.getString(EVENTS_KEY, null)
        if (eventsJson != null) {
            val type = object : TypeToken<List<Event>>() {}.type
            val loadedEvents = gson.fromJson<List<Event>>(eventsJson, type)
            allEvents.clear()
            allEvents.addAll(loadedEvents)
        }
    }

    private fun saveEvents() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val eventsJson = gson.toJson(allEvents)
        sharedPreferences.edit().putString(EVENTS_KEY, eventsJson).apply()
    }

    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null)
        val eventTitleEdit = dialogView.findViewById<EditText>(R.id.eventTitleEdit)
        val eventDescriptionEdit = dialogView.findViewById<EditText>(R.id.eventDescriptionEdit)

        AlertDialog.Builder(this)
            .setTitle("Add New Event")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val title = eventTitleEdit.text.toString()
                val description = eventDescriptionEdit.text.toString()
                if (title.isNotEmpty()) {
                    addNewEvent(Event(title, description, selectedDate))
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun addNewEvent(event: Event) {
        allEvents.add(event)
        if (event.date == selectedDate) {
            eventsList.add(event)
            eventsAdapter.notifyItemInserted(eventsList.size - 1)
        }
        saveEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateEventsList(date: String) {
        val filteredEvents = allEvents.filter { it.date == date }
        eventsList.clear()
        eventsList.addAll(filteredEvents)
        eventsAdapter.notifyDataSetChanged()
    }
}

data class Event(
    val title: String,
    val description: String,
    val date: String
)

class EventsAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.eventTitleText)
        val descriptionText: TextView = view.findViewById(R.id.eventDescriptionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.titleText.text = event.title
        holder.descriptionText.text = event.description
    }

    override fun getItemCount() = events.size
}