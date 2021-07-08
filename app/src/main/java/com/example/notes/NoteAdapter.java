package com.example.notes;

import android.content.Context;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends  RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View containerNote= layoutInflater.inflate(R.layout.container_note, parent, false);
        NoteViewHolder noteviewHolder = new NoteViewHolder(containerNote);
        return noteviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  NoteAdapter.NoteViewHolder holder, int position) {
           Note note = notes.get(position);
           //holder.textTitle.setText(Note.);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class NoteViewHolder extends ViewHolder{

        public TextView textTitle,textSubtitle,textDateTime;

        public NoteViewHolder(@NonNull  View itemView) {
            super(itemView);
            this.textTitle = (TextView)itemView.findViewById(R.id.textTitle);
            this.textSubtitle = (TextView)itemView.findViewById(R.id.textSubtitle);
            this.textDateTime = (TextView)itemView.findViewById(R.id.textDateandTime);

        }

    }
}


