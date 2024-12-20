package com.example.notesappkotlin.views.interfaces

import com.example.notesappkotlin.model.Notes

interface NotesClickListener {
    fun updateNotesClick(notes: Notes)
    fun deleteNote(notes: Notes, position: Int)
}