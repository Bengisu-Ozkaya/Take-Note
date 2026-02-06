package com.example.notepad.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateFolder extends AppCompatActivity {
    private TextInputEditText etFolderName;
    private MaterialButton btnDialogCreate, btnDialogCancel;
    private String folderName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_folder);

        etFolderName = findViewById(R.id.etFolderName);

        folderName = etFolderName.getText().toString().trim();

        btnDialogCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderName.isEmpty()){
                    Toast.makeText(CreateFolder.this,"Klasör adı girmeniz gerekmektedir!", Toast.LENGTH_SHORT).show();
                }else {
                    //database kayıt işlemleri olacak
                    Toast.makeText(CreateFolder.this,"Klasör Kaydedildi",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
