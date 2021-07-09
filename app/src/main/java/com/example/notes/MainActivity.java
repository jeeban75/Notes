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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
        notesRecyclerView.setHasFixedSize(true);


        StaggeredGridLayoutManager staggeredGridLayoutManager  = new StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        notesRecyclerView.setAdapter(noteAdapter);

       firebaseFirestore.collection("Notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                                          @Override
                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                              if (!queryDocumentSnapshots.isEmpty()) {
                                                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                  for (DocumentSnapshot documentSnapshot : list) {
                                                      Note note = documentSnapshot.toObject(Note.class);
                                                      notes.add(note);
                                                  }
                                                  noteAdapter.notifyDataSetChanged();
                                              } else {
                                                  Toast.makeText(MainActivity.this, "empty", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure( Exception e) {
               Toast.makeText(MainActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });






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


    //private void fetchFirebase(){



    //}
}