package com.example.notepad.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folders")
public class Folders {
    @PrimaryKey(autoGenerate = true)
    public int fid;
    @ColumnInfo(name = "folder_name")
    public String folderName;

    public Folders(String folderName) {
        this.folderName = folderName;
    }
}
