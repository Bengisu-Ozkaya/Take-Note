package com.example.notepad.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.dao.NotesDao;
import com.example.notepad.database.AppDatabase;
import com.example.notepad.database.Notes;
import com.example.notepad.view.TakingNotes;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Notes> noteArray;
    private Context context;
    private NotesDao notesDao;

    public NoteAdapter(List<Notes> noteList){
        this.noteArray = noteList;
        notesDao = AppDatabase.getInstance(context).notesDao() ;
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
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( NoteAdapter.ViewHolder holder, int position) {
        Notes note = noteArray.get(position);

        // kaydedilen notun başlığının olup olmaması
        if (note.noteTitle != null && !note.noteTitle.isEmpty()){
            holder.tvNoteTitle.setText(note.noteTitle);
        } else {
            holder.tvNoteTitle.setText("Başlıksız Not");
        }

        //notun ön izlemesi
        if (note.noteContent != null && !note.noteContent.isEmpty()){
            holder.tvNotePreview.setText(note.noteContent);
        }else {
            holder.tvNotePreview.setText("");
        }

        //kaydedilen notun üstüne tıklanırsa
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TakingNotes.class);

                // Not bilgilerini gönder
                intent.putExtra("NOTE_ID", note.nid);
                intent.putExtra("NOTE_TITLE", note.noteTitle);
                intent.putExtra("NOTE_CONTENT", note.noteContent);

                context.startActivity(intent);
            }
        });

        //notun üstüne basılı tutulursa
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();

                // pozisyonun geçerliliği kontrol ediliyor
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Notes longClickedNote = noteArray.get(currentPosition);
                    showDeleteDialog(longClickedNote, currentPosition); // Güncel pozisyonu gönder
                }
                return true;
            }
        });

    }

    private void showDeleteDialog(Notes note, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Silmek istediğinize emin misiniz?")
                .setMessage("Silinen not geri getirilemez yşne de silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //silme
                        notesDao.delete(note);
                        noteArray.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, noteArray.size());
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //pencereyi kapat
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return noteArray.size();
    }
}
