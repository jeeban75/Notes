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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ImageView imageAddNoteMain;
    RecyclerView recyclerView;
    ArrayList<Note> noteArrayList;
    NoteAdapter noteAdapter;
    FirebaseFirestore firebaseFirestore;

    public static final int requestAddNote = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteArrayList = new ArrayList<Note>();
        noteAdapter = new NoteAdapter(MainActivity.this,noteArrayList);
        recyclerView = findViewById(R.id.notesRecyclerView);
        //recyclerView.setHasFixedSize(true);
       StaggeredGridLayoutManager staggeredGridLayoutManager =
              new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView.setAdapter(noteAdapter);
        //DataListener();
         imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
       imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext() , createNote.class),requestAddNote
                );
            }
        });
       //end of imagenoteitem
        firebaseFirestore.collection("Notes").document("user").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note note = documentSnapshot.toObject(Note.class);
                        noteArrayList.add(note);
                        noteAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(MainActivity.this, "fetch error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DataListener() {

        firebaseFirestore.collection("Notes").document("User").collection("Notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                        if (error!=null)
                        {
                            Log.e("Data Fetching Error",error.getMessage());
                            Toast.makeText(MainActivity.this, "data fetching error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                noteArrayList.add(documentChange.getDocument().toObject(Note.class));
                                Toast.makeText(MainActivity.this,"data displayed got", Toast.LENGTH_SHORT).show();

                            }
                            noteAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}