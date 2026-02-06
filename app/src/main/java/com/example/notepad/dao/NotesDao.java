package com.example.notepad.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.notepad.database.Notes;

import java.util.List;

// Veritabanında yapılabilecek işlemler burada yapılıyor

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes")
    List<Notes> getAll();

    @Query("SELECT * FROM notes WHERE nid IN (:noteIds)")
    List<Notes> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM notes WHERE nid = :noteId LIMIT 1")
    Notes findById(int noteId);

    @Insert
    void insert(Notes note);

    @Insert
    void insertAll(Notes... notes);

    @Update
    void update(Notes note);

    @Delete
    void delete(Notes note);

    @Query("DELETE FROM notes")
    void deleteAll();
}
