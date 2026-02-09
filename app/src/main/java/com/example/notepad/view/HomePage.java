package com.example.notepad.view;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.adapter.FolderAdapter;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.dao.FolderDao;
import com.example.notepad.dao.NotesDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Folders;
import com.example.notepad.database.Notes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    FloatingActionButton fabAdd;
    RecyclerView recyclerViewNotes, recyclerViewFolders;
    ImageButton btnSearch;
    EditText etSearch;
    NoteAdapter noteAdapter;
    FolderAdapter folderAdapter;
    List<Notes> notesList;
    List<Folders> foldersList;
    NotesDao notesDao;
    FolderDao folderDao;
    Boolean isSearch = false; // arama açık mı

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // dao
        notesDao = AppDatabase.getInstance(this).notesDao();
        folderDao = AppDatabase.getInstance(this).folderDao();

        //button
        btnSearch = findViewById(R.id.btnSearch);
        fabAdd = findViewById(R.id.fabAdd);

        //edittext
        etSearch = findViewById(R.id.etSearch);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearch();
            }
        });

        // EditText'e yazı yazılıyorsa arama yap
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }
        });

        // recyclerview
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewFolders = findViewById(R.id.recyclerViewFolders);

        setupRecyclerView();
    }

    private void filterNotes(String query) {
        if (query.isEmpty()){ // aramaya bir şey girilmediyse tüm notları göster
            noteAdapter = new NoteAdapter(notesList);
            recyclerViewNotes.setAdapter(noteAdapter);

            folderAdapter = new FolderAdapter(foldersList);
            recyclerViewFolders.setAdapter(folderAdapter);
            return;
        }

        List<Notes> filteredList = new ArrayList<>(); //filtrenen notlar listesi
        List<Folders> filteredFolderList = new ArrayList<>(); // filtrelenen klasörler listesi

        // tüm notlar içerisinde arama yap
        for (Notes note : notesList) {
            // Başlıkta veya içerikte ara
            boolean titleMatches = note.noteTitle != null &&
                    note.noteTitle.toLowerCase().contains(query.toLowerCase());

            boolean contentMatches = note.noteContent != null &&
                    note.noteContent.toLowerCase().contains(query.toLowerCase());

            // Eşleşme varsa listeye ekle
            if (titleMatches || contentMatches) {
                filteredList.add(note);
            }
        }

        for (Folders folder : foldersList){
            boolean nameMatches = folder.folderName != null &&
                    folder.folderName.toLowerCase().contains(query.toLowerCase());

            if(nameMatches){
                filteredFolderList.add(folder);
            }
        }
        // Adapter'ı güncelle
        noteAdapter = new NoteAdapter(filteredList);
        recyclerViewNotes.setAdapter(noteAdapter);

        folderAdapter = new FolderAdapter(filteredFolderList);
        recyclerViewFolders.setAdapter(folderAdapter);
    }

    private void toggleSearch() {
        if (!isSearch){
            isSearch = true;
            etSearch.setVisibility(VISIBLE);
            etSearch.requestFocus();

            // Klavyeyi aç
            etSearch.post(new Runnable() {
                @Override
                public void run() {
                    android.view.inputmethod.InputMethodManager imm =
                            (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                }
            });

        }else {
            isSearch = false;
            etSearch.setVisibility(INVISIBLE);
            filterNotes(""); // tüm notları göstersin
        }
    }
    private void setupRecyclerView() {
        // liste nullsa liste oluştur
        if (notesList == null) {
            notesList = new ArrayList<>();
        }
        if (foldersList == null){
            foldersList = new ArrayList<>();
        }

        notesList = notesDao.getAll();
        foldersList = folderDao.getAll();

        noteAdapter = new NoteAdapter(notesList);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(noteAdapter);

        folderAdapter = new FolderAdapter(foldersList);
        recyclerViewFolders.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewFolders.setAdapter(folderAdapter);

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
        //notları yükle
        notesList = notesDao.getAll();
        if (notesList == null){
            notesList = new ArrayList<>();
        }

        noteAdapter = new NoteAdapter(notesList);
        recyclerViewNotes.setAdapter(noteAdapter);

        // Klasörleri yükle
        foldersList = folderDao.getAll();
        if (foldersList == null) {
            foldersList = new ArrayList<>();
        }
        folderAdapter = new FolderAdapter(foldersList);
        recyclerViewFolders.setAdapter(folderAdapter);
    }
}