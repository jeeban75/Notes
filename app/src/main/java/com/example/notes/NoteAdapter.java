package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder>   {

    private onItemClickListener listener;

    public NoteAdapter(@NonNull  FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull  NoteAdapter.NoteHolder holder, int position, @NonNull  Note note) {
        holder.Title.setText(note.getTitle());
        holder.Subtitle.setText(note.getSubtitle());
        holder.DateTime.setText(note.getDateTime());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_note, parent, false);
        return new NoteHolder(v);
    }

    public class NoteHolder extends RecyclerView.ViewHolder{
        TextView Title,Subtitle,DateTime;
        public NoteHolder(@NonNull  View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.textTitle);
            Subtitle = itemView.findViewById(R.id.textSubtitle);
            DateTime = itemView.findViewById(R.id.textDateandTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 int position = getAdapterPosition();
                 if(position!= RecyclerView.NO_POSITION && listener!=null)
                 {
                     listener.onItemClick(getSnapshots().getSnapshot(position),position);
                 }

                }
            });
        }
    }

    public interface  onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }

}
