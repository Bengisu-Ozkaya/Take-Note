package com.example.notepad.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "note_folder_cross_ref",
        primaryKeys = {"note_id", "folder_id"},
        foreignKeys = {
                @ForeignKey(
                        entity = Notes.class,
                        parentColumns = "nid",
                        childColumns = "note_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Folders.class,
                        parentColumns = "fid",
                        childColumns = "folder_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index("note_id"), @Index("folder_id")}
)
public class NoteFolder {
    @ColumnInfo(name = "note_id")
    public int noteId;

    @ColumnInfo(name = "folder_id")
    public int folderId;

    public NoteFolder(int noteId, int folderId) {
        this.noteId = noteId;
        this.folderId = folderId;
    }
}
