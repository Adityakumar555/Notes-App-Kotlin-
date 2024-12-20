package com.example.notesappkotlin.repository.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesappkotlin.model.Notes

// RoomDatabase provides direct access to the underlying database implementation but you should prefer using Dao classes.
@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {

        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {

            // this: Refers to the current companion object.
            // synchronized(this) ensures that only one thread can execute the block of code at a time
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "NOTES_DB"
                ).build()
                INSTANCE = instance
                instance
            }

        }


    }


}