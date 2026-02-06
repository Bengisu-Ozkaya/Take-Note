package com.example.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.database.Folders;

import org.w3c.dom.Text;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<Folders> folderList;

    public FolderAdapter(List<Folders> folderList) {
        this.folderList = folderList;
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
        //item_folder.xml dosyasını view'e dönüştür
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder holder, int position) {
        Folders folder = folderList.get(position);
        holder.tvFolderName.setText(folder.folderName);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }
}
