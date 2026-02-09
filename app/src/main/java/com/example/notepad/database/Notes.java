package com.example.notepad.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.security.PublicKey;

// veritabanı tablosu gibi

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "note_title")
    public String noteTitle;

    @ColumnInfo(name = "note_content")
    public String noteContent;

    public Notes(String noteTitle, String noteContent){
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }
}
