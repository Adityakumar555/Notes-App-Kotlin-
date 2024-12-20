package com.example.notesappkotlin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappkotlin.model.Notes
import com.example.notesappkotlin.repository.NotesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val addNotesList = MutableLiveData<List<Notes>>()

    // Public LiveData to list of notes
    val notesList: LiveData<List<Notes>> = addNotesList


    fun addNotes(notes: Notes) {
        //  launch is use When you don’t need the result of the background task.
        viewModelScope.launch {
            notesRepository.addNote(notes)
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            // async is use When you need to get a result from the background task.
            val allNotes = async {
                notesRepository.getNotes()
            }.await()
            addNotesList.postValue(allNotes)

        }
    }

    fun updateNote(notes: Notes){
        viewModelScope.launch {
            notesRepository.updateNotes(notes)
        }
    }

    fun deleteNote(id:Int){
        viewModelScope.launch {
            notesRepository.deleteNote(id)
            getNotes()
        }
    }


}