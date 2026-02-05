package com.example.notepad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    private void openMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePage.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fab_menu, null);
        bottomSheetDialog.setContentView(bottomSheetView);

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

        bottomSheetDialog.show();
    }
}