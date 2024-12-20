package com.example.notesappkotlin.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesappkotlin.R
import com.example.notesappkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, AllNotesFragment())
            .commit()

    }
}