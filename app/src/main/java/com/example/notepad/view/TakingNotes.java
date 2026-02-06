package com.example.notepad.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.google.android.material.button.MaterialButton;

public class TakingNotes extends AppCompatActivity {
    private EditText etNoteTitle, etNoteContent;
    private MaterialButton btnSave,btnCancel;

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

        buttonListener();

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
    }
}