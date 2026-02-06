package com.example.notepad.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private NotesDao notesDao;
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

        checkIntent(); // intentten gelen verileri kontrol et
        buttonListener();

    }

    private void checkIntent() {
        // kontrol işlemleri
        if (getIntent().hasExtra("NOTE_ID")) { // Eğer homepage den geldiysem verileri al
            isEditMode = true;
            noteId = getIntent().getIntExtra("NOTE_ID", -1);
            String title = getIntent().getStringExtra("NOTE_TITLE");
            String content = getIntent().getStringExtra("NOTE_CONTENT");

            // başlığı ve notu yükle
            etNoteTitle.setText(title);
            etNoteContent.setText(content);

            btnSave.setText("Güncelle");
        }
    }

    private void buttonListener() {
        // Kaydetme butonu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private void showCancel() {
        new AlertDialog.Builder(this)
                .setTitle("Çıkmak istediğinize emin misiniz?")
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

        if (isEditMode){ // notta güncelleme yaptıysak
            Notes updateNote = notesDao.findById(noteId);
            if (updateNote != null){
                if(updateNote.noteContent == null && updateNote.noteContent.isEmpty()){
                    Toast.makeText(this, "Boş not siliniyor...", Toast.LENGTH_SHORT).show();
                    notesDao.delete(updateNote);
                }

                updateNote.noteTitle = title;
                updateNote.noteContent = content;
                notesDao.update(updateNote);
            }
        } else {
            Notes note = new Notes(title,content);
            notesDao.insert(note);
            Toast.makeText(this, "Not kaydedildi!", Toast.LENGTH_SHORT).show();
        }

        if (content.isEmpty()){
            Toast.makeText(TakingNotes.this,"Boş Not Kaydedilemez!",Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()){
            title = "Başlıksız Not";
            Toast.makeText(TakingNotes.this,"Başlıksız not kaydedildi!",Toast.LENGTH_SHORT).show();
        }

        /*Database'e kaydedilme kontrolü*/
        /*List<Notes> notes = notesDao.getAll();
        for (Notes n:notes){
            System.out.println("ID: " + n.nid + "\n" +
                                "Title: " + n.noteTitle + "\n" +
                                "Content: " + n.noteContent);
        }*/
    }
}