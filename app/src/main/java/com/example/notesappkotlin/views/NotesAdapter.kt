package com.example.notesappkotlin.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappkotlin.R
import com.example.notesappkotlin.model.Notes
import com.example.notesappkotlin.views.interfaces.NotesClickListener
import java.util.Locale

class NotesAdapter(private val notesClickListener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val notesList = mutableListOf<Notes>()  // The list that holds your notes

    // ViewHolder to bind views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val note: TextView = view.findViewById(R.id.note)
    }

    // Update the data in the adapter
    fun updateData(newNotesList: List<Notes>) {
        // Iterate through the new list of notes
        for (newNote in newNotesList) {
            // Check if the note already exists in the list
            val existingNoteIndex = notesList.indexOfFirst { it.id == newNote.id }

            if (existingNoteIndex != -1) {
                // If the note exists, update it
                notesList[existingNoteIndex] = newNote
                notifyItemChanged(existingNoteIndex)  // Notify the adapter about the update
            } else {
                // If the note doesn't exist, add it to the list
                notesList.add(newNote)
                notifyItemInserted(notesList.size - 1)
            }
        }
    }

    fun filterNotes(newNotesList: List<Notes>) {
        // Directly update the notesList by clearing it and adding the new filtered notes
        notesList.clear()
        notesList.addAll(newNotesList)  // Add all the new notes to the list
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }


    // Creating new view holders for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return ViewHolder(view)
    }

    // Get the total count of items in the list
    override fun getItemCount(): Int {
        return notesList.size
    }

    // Bind data to each view in the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.title.text = note.title
        holder.note.text = note.note

        // Click listener for the note item
        holder.itemView.setOnClickListener {
            notesClickListener.updateNotesClick(note)
        }

        // Long click listener to delete the note
        holder.itemView.setOnLongClickListener {
            notesClickListener.deleteNote(note, position)
            true
        }
    }

    // Filter the notes based on the search query and update the list in the adapter
    fun getSearchFilter(query: String) :List<Notes> {
        val filteredList = ArrayList<Notes>()
        for (note in notesList) {
            // Check if the title or note contains the query (case-insensitive)
            if (note.title.contains(query, ignoreCase = true) ||
                note.note.contains(query, ignoreCase = true)) {
                filteredList.add(note)
            }
        }
        // Now, update the adapter's list with the filtered list and notify
        return filteredList
    }


    // Remove a note from the list
    fun deleteNotesFromList(position: Int) {
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }
}


/*
fun updateData(newNotesList: List<Notes>) {

    for (newList in newNotesList) {
        var isExist = false
        for (oldList in notesList) {
            if (newList.id == oldList.id) {
                val index = notesList.indexOf(oldList)
                notesList[index] = newList
                notifyItemChanged(index)
                isExist = true
                break
            }
        }

        if (!isExist) {
            notesList.add(newList)
            notifyItemChanged(notesList.size - 1)
        }

    }

}*/
