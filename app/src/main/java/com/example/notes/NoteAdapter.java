package com.example.notes;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View containerItem = layoutInflater.inflate(R.layout.container_note,parent,false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(containerItem);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  NoteAdapter.NoteViewHolder holder, int position) {
        //holder.setNote(notes.get(position));
        Note note = notes.get(position);
        holder.textTitle.setText(note.getTitle());
        if(note.getSubtitle().trim().isEmpty()){
            holder.textSubtitle.setVisibility(View.GONE);
        }
        else{
            holder.textSubtitle.setText(note.getSubtitle());
        }
        //textDateTime.setText(note.getDateTIme());

    }




    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textSubtitle,textDateTime;

        public NoteViewHolder(@NonNull  View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateandTime);
        }

      /*  void setNote(Note note)
        {
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }
            else{
                textSubtitle.setText(note.getSubtitle());
            }
            //textDateTime.setText(note.getDateTIme());
        }*/
    }
}


