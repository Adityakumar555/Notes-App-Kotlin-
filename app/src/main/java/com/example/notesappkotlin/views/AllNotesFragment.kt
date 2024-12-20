package com.example.notesappkotlin.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesappkotlin.R
import com.example.notesappkotlin.databinding.FragmentAllNotesBinding
import com.example.notesappkotlin.model.Notes
import com.example.notesappkotlin.repository.NotesRepository
import com.example.notesappkotlin.repository.roomDatabase.NotesDatabase
import com.example.notesappkotlin.viewModel.NotesViewModel
import com.example.notesappkotlin.viewModel.ViewModelFactory
import com.example.notesappkotlin.views.interfaces.NotesClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AllNotesFragment : Fragment(), NotesClickListener {
    private lateinit var binding: FragmentAllNotesBinding
    private lateinit var notesAdapter: NotesAdapter

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesRepository: NotesRepository
    private lateinit var notesDatabase: NotesDatabase
    private var notesList = listOf<Notes>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllNotesBinding.inflate(layoutInflater, container, false)

        notesDatabase = NotesDatabase.getInstance(requireActivity())
        notesRepository = NotesRepository(notesDatabase.notesDao())

        notesViewModel = ViewModelProvider(this, ViewModelFactory(notesRepository)).get(
            NotesViewModel::class.java
        )

        binding.addNotes.setOnClickListener {
            activity?.let {
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AddUpdateNoteFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        notesAdapter = NotesAdapter(this)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = notesAdapter

        // Get all notes when the fragment is created
        notesViewModel.getNotes()

        // Observe the notes list from the ViewModel
        notesViewModel.notesList.observe(viewLifecycleOwner, Observer { list ->
            notesList = list
            notesAdapter.updateData(list)

            // Check if the list is empty and update the UI
            if (list.isEmpty()) {
                binding.emptyImage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyImage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })

        // Set up the TextWatcher for the search functionality
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString().trim()
                if (query.isEmpty()) {
                    // Reset to show all notes if the search field is empty
                    notesAdapter.updateData(notesList)
                } else {
                    // Filter notes based on the search query
                    val filter = notesAdapter.getSearchFilter(query)
                    notesAdapter.filterNotes(filter)

                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.text?.clear()
    }

    override fun updateNotesClick(notes: Notes) {
        val fragment = AddUpdateNoteFragment()
        val bundle = Bundle()
        bundle.putParcelable("note", notes)
        fragment.arguments = bundle

        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun deleteNote(notes: Notes, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Delete Notes")
            .setMessage("Are you sure to delete this note?")
            .setPositiveButton("YES") { dialogInterface, i -> // Delete note
                notesViewModel.deleteNote(notes.id)
                notesAdapter.deleteNotesFromList(position)
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("CANCEL") { dialogInterface, i -> // Dismiss the dialog
                dialogInterface.dismiss()
            }

        val dialog = builder.create()

        // Change button colors in the dialog
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.black))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.black))
        }
        dialog.show()
    }
}
