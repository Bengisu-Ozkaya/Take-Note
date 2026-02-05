package com.example.notepad.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.google.android.material.button.MaterialButton;

public class TakingNotes extends AppCompatActivity {

    MaterialButton btnSave;
    EditText etNoteContent;

    RecyclerView recyclerViewNotes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_taking);

        btnSave = findViewById(R.id.btnSave);
        etNoteContent = findViewById(R.id.etNoteContent);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);


        /*View recyclerView = getLayoutInflater().inflate(R.layout.notes_taking, null);
        recyclerViewNotes = recyclerView.findViewById(R.id.recyclerViewNotes);*/


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNoteContent.getText().toString().isEmpty()) {
                    Toast.makeText(TakingNotes.this, "Boş not silindi!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TakingNotes.this, HomePage.class);
                    startActivity(intent);
                } else {
                    savingNote();

                    Toast.makeText(TakingNotes.this, "Not kaydedildi!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TakingNotes.this, HomePage.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void savingNote() {

    }
}
