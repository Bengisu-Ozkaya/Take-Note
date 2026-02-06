package com.example.notepad.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.example.notepad.dao.NotesDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Notes;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class TakingNotes extends AppCompatActivity {
    private EditText etNoteTitle, etNoteContent;
    private MaterialButton btnSave,btnCancel;
    NotesDao notesDao;
    private int noteId = -1;
    private boolean isEditMode = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_taking);

        //text
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);

        //button
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        //database
        notesDao = AppDatabase.getInstance(this).notesDao();

        // Intent'ten gelen verileri kontrol et
        if (getIntent() != null && getIntent().hasExtra("NOTE_ID")) {
            isEditMode = true;
            noteId = getIntent().getIntExtra("NOTE_ID", -1);
            String title = getIntent().getStringExtra("NOTE_TITLE");
            String content = getIntent().getStringExtra("NOTE_CONTENT");

            // DEBUG - Logları kontrol edin
            Log.d("TakingNotes", "Note ID: " + noteId);
            Log.d("TakingNotes", "Title: " + title);
            Log.d("TakingNotes", "Content: " + content);

            // Mevcut notu göster
            if (title != null) {
                etNoteTitle.setText(title);
            }
            if (content != null) {
                etNoteContent.setText(content);
            }

            btnSave.setText("Güncelle");
            btnCancel.setText("Sil");
        }
        buttonListener();

    }

    private void buttonListener() {
        // Kaydetme butonu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode){
                    editNote();
                }
                if (etNoteContent.getText().toString().trim().isEmpty()){
                    Toast.makeText(TakingNotes.this,"Boş not kaydedilemez!",Toast.LENGTH_SHORT).show();;
                    finish(); // anasayfaya dön
                }
                else{
                    //notu kaydetme
                    savingNotes();
                    Toast.makeText(TakingNotes.this,"Not kaydedildi!", Toast.LENGTH_SHORT).show();;
                    finish();
                }
            }
        });

        //İptal butonu
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode){
                    deleteNote();
                }
                // kullanıcı not girdiyse
                if (!etNoteContent.getText().toString().trim().isEmpty()){
                    showCancel();
                }
                else{
                    //boş not anasayfaya dön
                    finish();
                }
            }
        });
    }

    private void deleteNote() {
        showCancel();

        Notes noteToDelete = notesDao.findById(noteId);
        notesDao.delete(noteToDelete);

        Toast.makeText(this,"Not silindi!",Toast.LENGTH_SHORT).show();
    }

    private void editNote() {
        String title = etNoteTitle.getText().toString().trim();
        String content = etNoteContent.getText().toString().trim();

        // boş not
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Not boş olamaz!", Toast.LENGTH_SHORT).show();
        }

        Notes editNote = new Notes(title,content);
        editNote.nid = noteId;
        notesDao.update(editNote);

        Toast.makeText(this,"Not Güncellendi!",Toast.LENGTH_SHORT).show();
    }

    private void showCancel() {
        new AlertDialog.Builder(this)
                .setTitle("Silmek istediğinize emin misiniz?")
                .setMessage("Not kaydedilmedi, çıkmak istediğinize emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Evet derse, notu kaydetmeden çık
                        Toast.makeText(TakingNotes.this, "Not kaydedilmedi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // hayır derse dialogu kapat
                    }
                }).show();

    }

    private void savingNotes() {
        //notu kaydet
        String title = etNoteTitle.getText().toString().trim();
        String content = etNoteContent.getText().toString().trim();

        if (content.isEmpty()){
            Toast.makeText(TakingNotes.this,"Boş Not Kaydedilemez!",Toast.LENGTH_SHORT);
        }

        Notes note = new Notes(title,content);
        notesDao.insert(note);
        Toast.makeText(this, "Not kaydedildi!", Toast.LENGTH_SHORT).show();

        /*Database'e kaydedilme kontrolü*/
        /*List<Notes> notes = notesDao.getAll();
        for (Notes n:notes){
            System.out.println("ID: " + n.nid + "\n" +
                                "Title: " + n.noteTitle + "\n" +
                                "Content: " + n.noteContent);
        }*/
    }
}