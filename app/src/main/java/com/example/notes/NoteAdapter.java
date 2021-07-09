package com.example.notes;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    ArrayList<Note> noteArrayList;
    public NoteAdapter(Context context, ArrayList<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
    }



    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.container_note,parent,false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  NoteViewHolder holder, int position) {
        Note note = noteArrayList.get(position);
        holder.Title.setText(note.getTitle());
        holder.Subtitle.setText(note.getSubtitle());
        holder.Text.setText(note.getext());
        Toast.makeText(context, note.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class NoteViewHolder extends  RecyclerView.ViewHolder {

        TextView Title,Subtitle,Text;
        public NoteViewHolder(@NonNull  View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.textTitle);
            Subtitle = itemView.findViewById(R.id.textSubtitle);
            Text = itemView.findViewById(R.id.textDateandTime);
        }
    }
}


