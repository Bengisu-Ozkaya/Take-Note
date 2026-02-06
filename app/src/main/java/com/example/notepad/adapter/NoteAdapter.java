package com.example.notepad.adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.database.Notes;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Notes> noteArray;

    public NoteAdapter(List<Notes> noteList){
        this.noteArray = noteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNoteTitle;
        TextView tvNotePreview;

        public ViewHolder(View view){
            super(view);
            tvNoteTitle = view.findViewById(R.id.tvNoteTitle);
            tvNotePreview = view.findViewById(R.id.tvNotePreview);
        }
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( NoteAdapter.ViewHolder holder, int position) {
        Notes note = noteArray.get(position);

        if (note.noteTitle != null && !note.noteTitle.isEmpty()){ // kaydedilen notun başlığı varsa
            holder.tvNoteTitle.setText(note.noteTitle);
        } else {
            holder.tvNoteTitle.setText("Başlıksız Not");
        }
    }

    @Override
    public int getItemCount() {
        return noteArray.size();
    }
}
