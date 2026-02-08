package com.example.notepad.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.R;
import com.example.notepad.dao.FolderDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Folders;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CreateFolder extends AppCompatActivity {
    private TextInputEditText etFolderName;
    private MaterialButton btnDialogCreate, btnDialogCancel;
    private TextView folderTv;

    private FolderDao folderDao;

    private int folderId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_folder);

        //dao
        folderDao = AppDatabase.getInstance(this).folderDao();

        //edittext
        etFolderName = findViewById(R.id.etFolderName);
        //material button
        btnDialogCreate = findViewById(R.id.btnDialogCreate);
        btnDialogCancel = findViewById(R.id.btnDialogCancel);
        //textview
        folderTv = findViewById(R.id.folderTv);

        setupListener();
        checkIntent();

    }

    private void checkIntent() {
        // kontrol işlemleri
        if (getIntent().hasExtra("FOLDER_ID")){
            folderId =  getIntent().getIntExtra("FOLDER_ID",-1);
            String folderName = getIntent().getStringExtra("FOLDER_NAME");

            folderTv.setText(folderName);
        }
    }

    private void setupListener() {
        //klasör oluşturma butonu
        btnDialogCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFolderName.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateFolder.this,"Klasör adı girmeniz gerekmektedir!", Toast.LENGTH_SHORT).show();
                }else {
                    //database kayıt işlemleri olacak
                    saveFolder();
                }
            }
        });

        // iptal etme
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
    }

    private void saveFolder() {
        String folderName = etFolderName.getText().toString().trim();

        Folders folder = new Folders(folderName);
        folderDao.insert(folder);

        /*Database'e kaydedilme kontrolü*/
        /*List<Folders> folders = folderDao.getAll();
        for (Folders f: folders){
            System.out.println("ID: " + f.fid + "\n" +
                                "Title: " + f.folderName);
        }*/

        Toast.makeText(CreateFolder.this,"Klasör Kaydedildi",Toast.LENGTH_SHORT).show();
        finish();


    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("İptal etmek istediğinze emin misiniz?")
                .setMessage("Klasör oluşturulmadı, iptal etmek istediğinize emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CreateFolder.this,"Klasör oluşturulmadı!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}
