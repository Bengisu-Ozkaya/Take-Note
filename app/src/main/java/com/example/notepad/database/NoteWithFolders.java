package com.example.notepad.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

// Bir not ve bulunduğu klasörler
public class NoteWithFolders {
    @Embedded
    public Notes note;

    @Relation(
            parentColumn = "nid",
            entityColumn = "fid",
            associateBy = @Junction(
                    value = NoteFolder.class,
                    parentColumn = "note_id",
                    entityColumn = "folder_id"
            )
    )
    public List<Folders> folders;
}