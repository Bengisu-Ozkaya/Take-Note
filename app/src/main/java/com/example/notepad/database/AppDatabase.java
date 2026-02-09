package com.example.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notepad.dao.FolderDao;
import com.example.notepad.dao.NoteFolderDao;
import com.example.notepad.dao.NotesDao;

@Database(
        entities = {Notes.class, Folders.class, NoteFolder.class},
        version = 3,  // Version'u artırın!
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract NotesDao notesDao();
    public abstract FolderDao folderDao();
    public abstract NoteFolderDao noteFolderDao(); // YENİ!

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}