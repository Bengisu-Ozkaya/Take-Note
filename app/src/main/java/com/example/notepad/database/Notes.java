package com.example.notepad.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.security.PublicKey;

// veritabanı tablosu gibi

@Entity(
        tableName = "notes",
        foreignKeys = @ForeignKey( //foldersları buraya FK olarak ekliyoruz
                entity = Folders.class,
                parentColumns = "fid",
                childColumns = "folder_id",
                onDelete = ForeignKey.SET_NULL //Klasör silinirse notun folderİd si null
        )
)
public class Notes {

    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "note_title")
    public String noteTitle;

    @ColumnInfo(name = "note_content")
    public String noteContent;

    @ColumnInfo(name = "folder_id")
    public Integer folderId; // Hangi klasörde olduğu (null ise klasörde değil)

    public Notes(String noteTitle, String noteContent){
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }

    /*public Notes(String noteTitle, String noteContent, Integer folderId) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.folderId = folderId;
    }*/
}
