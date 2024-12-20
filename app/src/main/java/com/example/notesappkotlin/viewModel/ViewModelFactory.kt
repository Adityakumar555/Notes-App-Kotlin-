package com.example.notesappkotlin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesappkotlin.repository.NotesRepository

// ViewModel Factories helps you to create ViewModels with custom parameters, making it a more flexible
class ViewModelFactory(private val notesRepository: NotesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(notesRepository) as T
        }
        throw IllegalArgumentException("unknown View Model")
    }

}