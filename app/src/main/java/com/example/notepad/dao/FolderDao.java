package com.example.notepad.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.database.Folders;
import com.example.notepad.database.Notes;

import java.util.List;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM folders")
    List<Folders> getAll();

    @Query("SELECT * FROM folders WHERE fid IN (:folderIds)")
    List<Folders> loadAllByIds(int[] folderIds);

    @Query("SELECT * FROM folders WHERE fid = :folderId LIMIT 1")
    Folders findById(int folderId);
    @Insert
    void insert(Folders folder);

    @Insert
    void insertAll(Folders... folder);

    @Update
    void update(Folders folder);

    @Delete
    void delete(Folders folder);

    @Query("DELETE FROM folders")
    void deleteAll();
}
