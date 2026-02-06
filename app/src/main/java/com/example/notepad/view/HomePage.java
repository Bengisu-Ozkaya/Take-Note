package com.example.notepad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.dao.NotesDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Notes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    FloatingActionButton fabAdd;
    RecyclerView recyclerViewNotes;
    NoteAdapter noteAdapter;
    List<Notes> notesList;
    NotesDao notesDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // dao
        notesDao = AppDatabase.getInstance(this).notesDao();

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        // recyclerview
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);

        setupRecyclerView();
    }
    private void setupRecyclerView() {
        // liste nullsa liste oluştur
        if (notesList == null) {
            notesList = new ArrayList<>();
        }

        notesList = notesDao.getAll();

        noteAdapter = new NoteAdapter(notesList);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(noteAdapter);

    }

    private void openMenu() {
        // BottomSheetDialog => ekranın altından çıkan pencere
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePage.this); // açılır pencereyi oluşturuyor
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fab_menu, null); // oluşan pencerenin içinde ne olacak
        bottomSheetDialog.setContentView(bottomSheetView); // pencere/dialog açıldığında fab_menu yu göster

        // not ve klasör oluşturma layoutları
        LinearLayout layoutCreateNote = bottomSheetView.findViewById(R.id.layoutCreateNote);
        LinearLayout layoutCreateFolder = bottomSheetView.findViewById(R.id.layoutCreateFolder);

        //Not oluşturma listenerı
        layoutCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(HomePage.this,TakingNotes.class);
                startActivity(intent);
            }
        });

        //Klasör oluşturma Listenerı
        layoutCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(HomePage.this,CreateFolder.class);
                startActivity(intent);
            }
        });

        bottomSheetDialog.show(); // dialogu göster
    }

    @Override
    protected void onResume() {
        // Not kaydedip geri dönüldüğünde güncelleme burada da gözüksün
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        notesList = notesDao.getAll();

        if (notesList == null){
            notesList = new ArrayList<>();
        }

        noteAdapter = new NoteAdapter(notesList);
        recyclerViewNotes.setAdapter(noteAdapter);
    }
}