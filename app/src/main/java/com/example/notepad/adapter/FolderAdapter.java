package com.example.notepad.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.dao.FolderDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Folders;
import com.example.notepad.database.Notes;
import com.example.notepad.view.CreateFolder;

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

        // klasör silme
        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Folders longClickedFolder = folderList.get(currentPosition);
                showDeleteDialog(longClickedFolder, currentPosition);
            }
            return true;
        });
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
