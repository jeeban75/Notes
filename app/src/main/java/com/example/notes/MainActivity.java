package com.example.notes;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    RecyclerView notesRecyclerView;
    ImageView imageAddNoteMain;
    FirebaseFirestore firebaseFirestore;
    String Title,Subtitle,Text;

    public static final int requestAddNote = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        imageAddNoteMain = (ImageView)findViewById(R.id.imageAddNoteMain);
        notes = new ArrayList<>();

        NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        //notesRecyclerView.setHasFixedSize(true);


        StaggeredGridLayoutManager staggeredGridLayoutManager  = new StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        notesRecyclerView.setAdapter(noteAdapter);

        fetchFirebase();
        notes.add(new Note(Title,Subtitle,Text));

        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext() , createNote.class),requestAddNote
                );
            }
        });
       //end of imagenoteitem
    }

    private void fetchFirebase(){

        firebaseFirestore.collection("Notes").document("user").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note data =documentSnapshot.toObject(Note.class);
                        Title = data.getTitle();
                        Subtitle = data.getSubtitle();
                        Text = data.getext();
                        Toast.makeText(getApplicationContext(),Title+Subtitle+Text,Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(getApplicationContext(),"fetching failed"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}