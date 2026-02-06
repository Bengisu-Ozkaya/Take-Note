package com.example.notepad.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notepad.dao.FolderDao;

@Database(entities = {Folders.class}, version = 1, exportSchema = false)
public abstract class FolderAppDatabase extends RoomDatabase {
    private static FolderAppDatabase instance;

    public abstract FolderDao folderDao();

    public static synchronized FolderAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FolderAppDatabase.class,
                            "folders_database"
                    )
                    .allowMainThreadQueries()  // Sadece test için - production'da kullanmayın!
                    .build();
        }
        return instance;
    }

}
