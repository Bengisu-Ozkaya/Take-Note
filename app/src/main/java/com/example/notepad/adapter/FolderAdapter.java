package com.example.notepad.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.dao.FolderDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Folders;
import com.example.notepad.database.Notes;
import com.example.notepad.view.CreateFolder;
import com.example.notepad.view.FolderDetail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<Folders> folderList;
    private Context context;
    private FolderDao folderDao;

    public FolderAdapter(List<Folders> folderList) {
        this.folderList = folderList;
        folderDao = AppDatabase.getInstance(context).folderDao();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconFolder;
        TextView tvFolderName;
        public ViewHolder(View itemView) {
            super(itemView);

            iconFolder = itemView.findViewById(R.id.iconFolder);
            tvFolderName = itemView.findViewById(R.id.tvFolderName);
        }
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        //item_folder.xml dosyasını view'e dönüştür
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
        Folders folder = folderList.get(position);
        holder.tvFolderName.setText(folder.folderName);

        // klasöre tıklayınca gitme
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateFolder.class);

                //klasör bilgilerini gönder
                intent.putExtra("FOLDER_ID",folder.fid);
                intent.putExtra("FOLDER_NAME",folder.folderName);

                context.startActivity(intent);
            }
        });

        // klasörün içine girme
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FolderDetail.class);

                // Klasör bilgilerini gönder
                intent.putExtra("FOLDER_ID", folder.fid);
                intent.putExtra("FOLDER_NAME", folder.folderName);

                context.startActivity(intent);
            }
        });


        // klasör ayarı değiştirme
        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Folders longClickedFolder = folderList.get(currentPosition);
                showFolderActions(longClickedFolder, currentPosition);
            }
            return true;
        });
    }

    private void showFolderActions(Folders longClickedFolder, int currentPosition) {
        //dialog oluşturma
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.folder_actions,null);
        bottomSheetDialog.setContentView(bottomSheetView);

        //viewlar
        LinearLayout layoutRename = bottomSheetView.findViewById(R.id.layoutRename);
        LinearLayout layoutDelete = bottomSheetView.findViewById(R.id.layoutDelete);
        MaterialButton btnCancel = bottomSheetView.findViewById(R.id.btnCancel);

        // rename
        layoutRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // yeniden adlandırma
                bottomSheetDialog.dismiss();
                showRenameDialog(longClickedFolder, currentPosition);
            }
        });

        // silme
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                showDeleteDialog(longClickedFolder,currentPosition);
            }
        });

        //cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();


    }

    private void showRenameDialog(Folders folder, int position) {
        // Dialog view'ını oluştur
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_folder, null);

        // EditText'i bul
        EditText etNewName = dialogView.findViewById(R.id.etNewName);


        // Mevcut klasör adını göster
        etNewName.setText(folder.folderName);
        etNewName.setSelection(folder.folderName.length()); // Cursor'u sona al

        // AlertDialog oluştur
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        // Butonları bul
        MaterialButton btnCancel = dialogView.findViewById(R.id.btnFolderCancel);
        MaterialButton btnCreate = dialogView.findViewById(R.id.btnCreate);

        // Buton metnini değiştir
        btnCreate.setText("Güncelle");

        // İptal
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Güncelle
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etNewName.getText().toString().trim();

                if (newName.isEmpty()) {
                    Toast.makeText(context, "Klasör adı boş olamaz", Toast.LENGTH_SHORT).show();
                } else {
                    // Klasör adını güncelle
                    folder.folderName = newName;
                    folderDao.update(folder);

                    // RecyclerView'ı güncelle
                    folderList.set(position, folder);
                    notifyItemChanged(position);

                    Toast.makeText(context, "Klasör adı güncellendi", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showDeleteDialog(Folders folder, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Klasörü silmek istediğinize emin misiniz?")
                .setMessage("Bu işlem geri alınamaz yine de silmek istiyor musunuz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        folderDao.delete(folder);
                        folderList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, folderList.size());
                        Toast.makeText(context, "Klasör siliniyor!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }
}
