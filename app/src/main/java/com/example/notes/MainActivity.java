package com.example.notes;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity  {


    ImageView imageAddNoteMain;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;

    private NoteAdapter noteAdapter;
    public static final int requestAddNote = 1;
    public static final int requestUpdateNote = 2;
    private int noteClickedPosition= -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.notesRecyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), createNote.class), requestAddNote
                );
            }
        });
        showRecycleView();
        //end of imagenoteitem

        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                String Title = note.getTitle();
                String Subtitle = note.getSubtitle();
                String DateTime = note.getDateTime();
                String Text = note.gettext();
                Toast.makeText(MainActivity.this, "position"+position+Title+Subtitle+DateTime+Text, Toast.LENGTH_SHORT).show();
                noteClickedPosition=position;
                Intent intent = new Intent(getApplicationContext(),createNote.class);
                intent.putExtra("isViewOrUpdate",false);
                intent.putExtra("documentSnapshot", (Serializable) documentSnapshot);
                startActivityForResult(intent,requestUpdateNote);
            }
        });
    }
    private void showRecycleView()
    {
        Query query = firebaseFirestore.collection("Notes").document("Test").collection("Data");
        FirestoreRecyclerOptions<Note> alluserNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        noteAdapter = new NoteAdapter(alluserNotes);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
}