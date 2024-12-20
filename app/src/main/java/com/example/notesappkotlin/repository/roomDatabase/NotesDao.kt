package com.example.notesappkotlin.repository.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesappkotlin.model.Notes

@Dao
interface NotesDao {

    // nConflict = OnConflictStrategy.IGNORE is use if data is already exist on a id/ primary key that this IGNORE the current save data
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: Notes)

    @Update
    suspend fun updateNote(notes: Notes)

    @Query("select * from notes")
    suspend fun getAllNotes():List<Notes>

    @Query("delete from notes where id = :id")
    suspend fun deleteNote(id:Int)


}