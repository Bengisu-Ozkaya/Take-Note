package com.example.notepad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.dao.NotesDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FolderDetail extends AppCompatActivity {
    
    TextView tvFolderName, tvNoteCount;
    RecyclerView rvFolderNotes;

    private LinearLayout layoutEmptyState;
    private FloatingActionButton fabAddNote;

    //Notları göstermek için gerekli "Note" şeyleri
    private NoteAdapter noteAdapter;
    private List<Notes> notesList;
    private NotesDao notesDao;
    private int folderId;
    private String folderName;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_detail);
        //dao
        notesDao = AppDatabase.getInstance(this ).notesDao();

        //textview
        tvFolderName = findViewById(R.id.tvFolderName);
        tvNoteCount = findViewById(R.id.tvNoteCount);

        // button
        fabAddNote = findViewById(R.id.fabAddNote);

        //recyclerview
        rvFolderNotes = findViewById(R.id.rvFolderNotes);

        //layout
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FolderDetail.this,TakingNotes.class);
                intent.putExtra("FOLDER_ID", folderId);
                intent.putExtra("FOLDER_NAME", folderName);
                startActivity(intent);
            }
        });

        setupRecyclerView();

        // intentten verileri al
        checkIntent();

        tvFolderName.setText(folderName);

        loadNotes();
    }

    private void setupRecyclerView() {
        rvFolderNotes.setLayoutManager(new LinearLayoutManager(this));
        rvFolderNotes.setHasFixedSize(true);
    }

    private void loadNotes() {
        // Arka plan thread'de notları yükle
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Bu klasöre ait notları getir
                notesList = notesDao.getNotesByFolder(folderId);

                // Ana thread'de UI'ı güncelle
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (notesList != null && !notesList.isEmpty()) {
                            // Notlar varsa
                            layoutEmptyState.setVisibility(View.GONE);
                            rvFolderNotes.setVisibility(View.VISIBLE);

                            // Adapter'ı ayarla
                            noteAdapter = new NoteAdapter(notesList);
                            rvFolderNotes.setAdapter(noteAdapter);

                            // Not sayısını güncelle
                            tvNoteCount.setText(String.valueOf(notesList.size()));
                        } else {
                            // Klasör boşsa
                            layoutEmptyState.setVisibility(View.VISIBLE);
                            rvFolderNotes.setVisibility(View.GONE);
                            tvNoteCount.setText("0");
                        }
                    }
                });
            }
        }).start();
    }

    private void checkIntent() {
        // Intent'ten klasör bilgilerini al
        folderId = getIntent().getIntExtra("FOLDER_ID", -1);
        folderName = getIntent().getStringExtra("FOLDER_NAME");

        // Eğer veri gelmezse geri dön
        if (folderId == -1 || folderName == null) {
            Toast.makeText(this, "Klasör bilgisi bulunamadı!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
