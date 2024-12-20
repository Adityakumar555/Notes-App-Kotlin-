package com.example.notesappkotlin.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesappkotlin.repository.roomDatabase.NotesDatabase
import com.example.notesappkotlin.viewModel.ViewModelFactory
import com.example.notesappkotlin.databinding.FragmentAddUpdateNoteBinding
import com.example.notesappkotlin.model.Notes
import com.example.notesappkotlin.repository.NotesRepository
import com.example.notesappkotlin.viewModel.NotesViewModel


class AddUpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddUpdateNoteBinding

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesRepository: NotesRepository
    private lateinit var notesDatabase: NotesDatabase
    private var noteId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUpdateNoteBinding.inflate(layoutInflater, container, false)

        notesDatabase = NotesDatabase.getInstance(requireActivity())
        notesRepository = NotesRepository(notesDatabase.notesDao())

        notesViewModel = ViewModelProvider(this, ViewModelFactory(notesRepository)).get(
            NotesViewModel::class.java)

        if (arguments != null) {

            val notes: Notes? = arguments?.getParcelable("note")

            notes?.let {
                binding.note.setText(notes.note)
                binding.title.setText(notes.title)
                noteId = notes.id
            }

            binding.saveNotes.setOnClickListener {

                val title = binding.title.text.toString().trim()
                val note = binding.note.text.toString().trim()

                if (title.isEmpty()) {
                    Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                } else {
                    val notes = Notes(noteId!!, title, note)
                    notesViewModel.updateNote(notes)
                    Toast.makeText(context, "Note Update", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                }
            }

        } else {

            binding.saveNotes.setOnClickListener {
                val title = binding.title.text.toString().trim()
                val note = binding.note.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                } else {
                    val notes = Notes(0, title, note)
                    notesViewModel.addNotes(notes)
                    Toast.makeText(context, "Note Save", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                }
            }

        }

        binding.backIcon.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return binding.root
    }

}