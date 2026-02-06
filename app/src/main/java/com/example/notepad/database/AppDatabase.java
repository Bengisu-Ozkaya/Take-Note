package com.example.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notepad.dao.NotesDao;

// entity ve dao nun bir araya geldiği sınıf

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract NotesDao notesDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "notes_database"
                    )
                    .allowMainThreadQueries()  // Sadece test için - production'da kullanmayın!
                    .build();
        }
        return instance;
    }
}