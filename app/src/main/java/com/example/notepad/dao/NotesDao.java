package com.example.notepad.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.database.Notes;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes")
    List<Notes> getAll();

    @Query("SELECT * FROM notes WHERE nid IN (:noteIds)")
    List<Notes> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM notes WHERE nid = :noteId LIMIT 1")
    Notes findById(int noteId);

    // Klasörde olmayan notları getir (hiçbir klasörde bulunmayan)
    @Query("SELECT * FROM notes WHERE nid NOT IN (SELECT DISTINCT note_id FROM note_folder_cross_ref)")
    List<Notes> getNotesWithoutFolder();

    // Belirli bir klasördeki notları getir - TABLO ADI DÜZELTİLDİ!
    @Query("SELECT notes.* FROM notes " +
            "INNER JOIN note_folder_cross_ref ON notes.nid = note_folder_cross_ref.note_id " +
            "WHERE note_folder_cross_ref.folder_id = :folderId " +
            "ORDER BY notes.nid DESC")
    List<Notes> getNotesByFolder(int folderId);

    @Insert
    void insert(Notes note);

    @Insert
    long insertAndGetId(Notes note);

    @Insert
    void insertAll(Notes... notes);

    @Update
    void update(Notes note);

    @Delete
    void delete(Notes note);

    @Query("DELETE FROM notes")
    void deleteAll();
}