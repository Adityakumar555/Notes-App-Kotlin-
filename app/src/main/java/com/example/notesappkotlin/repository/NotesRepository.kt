package com.example.notesappkotlin.repository

import com.example.notesappkotlin.model.Notes
import com.example.notesappkotlin.repository.roomDatabase.NotesDao

// Repository ek class hai jo database ya API ke data ko access karke ViewModel ko deta hai, aur data handling ka pura kaam manage karta hai.
class NotesRepository(private val notesDao: NotesDao) {

    suspend fun addNote(notes: Notes){
        notesDao.insertNote(notes)
    }

    suspend fun getNotes():List<Notes>{
        return notesDao.getAllNotes()
    }

    suspend fun updateNotes(notes: Notes){
        notesDao.updateNote(notes)
    }

    suspend fun deleteNote(id:Int){
        notesDao.deleteNote(id)
    }



}