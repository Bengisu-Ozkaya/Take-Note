package com.example.notepad.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.notepad.database.FolderWithNotes;
import com.example.notepad.database.NoteFolder;
import com.example.notepad.database.NoteWithFolders;

import java.util.List;

@Dao
public interface NoteFolderDao {

    // Notu klasöre ekle
    @Insert
    void addNoteToFolder(NoteFolder crossRef);

    // Notu klasörden çıkar
    @Delete
    void removeNoteFromFolder(NoteFolder crossRef);

    // Belirli bir notu belirli bir klasörden çıkar
    @Query("DELETE FROM note_folder_cross_ref WHERE note_id = :noteId AND folder_id = :folderId")
    void removeNoteFromSpecificFolder(int noteId, int folderId);

    // Bir notun tüm klasörlerini sil
    @Query("DELETE FROM note_folder_cross_ref WHERE note_id = :noteId")
    void removeNoteFromAllFolders(int noteId);

    // Bir klasördeki tüm notları sil (ilişkiyi)
    @Query("DELETE FROM note_folder_cross_ref WHERE folder_id = :folderId")
    void removeAllNotesFromFolder(int folderId);

    // Klasör ve içindeki notları getir
    @Transaction
    @Query("SELECT * FROM folders WHERE fid = :folderId")
    FolderWithNotes getFolderWithNotes(int folderId);

    // Not ve bulunduğu klasörleri getir
    @Transaction
    @Query("SELECT * FROM notes WHERE nid = :noteId")
    NoteWithFolders getNoteWithFolders(int noteId);

    // Bir notun hangi klasörlerde olduğunu kontrol et
    @Query("SELECT folder_id FROM note_folder_cross_ref WHERE note_id = :noteId")
    List<Integer> getFolderIdsForNote(int noteId);

    // Bir notun belirli bir klasörde olup olmadığını kontrol et
    @Query("SELECT COUNT(*) FROM note_folder_cross_ref WHERE note_id = :noteId AND folder_id = :folderId")
    int isNoteInFolder(int noteId, int folderId);
}