package com.example.notepad.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

// Bir klasör ve içindeki notlar
public class FolderWithNotes {
    @Embedded
    public Folders folder;

    @Relation(
            parentColumn = "fid",
            entityColumn = "nid",
            associateBy = @Junction(
                    value = NoteFolder.class,
                    parentColumn = "folder_id",
                    entityColumn = "note_id"
            )
    )
    public List<Notes> notes;
}