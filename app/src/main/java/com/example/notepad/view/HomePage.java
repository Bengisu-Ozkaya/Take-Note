package com.example.notepad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePage extends AppCompatActivity {

    FloatingActionButton fabAdd;

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

        View bottomSheetView = getLayoutInflater().inflate(R.layout.fab_menu, null); //fab_menu'yu bul
        bottomSheetDialog.setContentView(bottomSheetView); // bottonSheetDialog olayını fab_menu layout'unda yap

        // fab_menu deki layoutCreateNote ve layoutCreateFolder layoutunu bul
        LinearLayout layoutCreateNote = bottomSheetView.findViewById(R.id.layoutCreateNote);
        LinearLayout layoutCreateFolder = bottomSheetView.findViewById(R.id.layoutCreateFolder);

        // bu layoutlara tıklanma olursa sayfa değiştir.
        if (layoutCreateNote != null) {
            layoutCreateNote.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                // Not oluşturma sayfasına git
                Intent intent = new Intent(HomePage.this, TakingNotes.class);
                startActivity(intent);
            });
        }

        if (layoutCreateFolder != null) {
            layoutCreateFolder.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                // Klasör oluşturma sayfasına git
                Intent intent = new Intent(HomePage.this, CreateFolder.class);
                startActivity(intent);
            });
        }

        // BottomSheet'i göster
        bottomSheetDialog.show();
    }

}
