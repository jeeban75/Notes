package com.example.notes;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity  {


    ImageView imageAddNoteMain;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    private NoteAdapter noteAdapter;
    public static final int requestAddNote = 1;
    public static final int requestUpdateNote = 2;
    private int noteClickedPosition= -1;

    private AlertDialog dialogDeleteNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.notesRecyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

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


    }
    private void showRecycleView()
    {
        Query query = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("Data")
                .orderBy("DateTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> alluserNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        noteAdapter = new NoteAdapter(alluserNotes);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);

       /* noteAdapter.setOnLongCliclListener(new NoteAdapter.onLongClickListener() {
            @Override
            public void onLongClickListener(int position) {
                NoteDeleteDialog();
            }
        });*/
     /*   noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
               // String Title = note.getTitle();
               // String Subtitle = note.getSubtitle();
                //String DateTime = note.getDateTime();
                //String Text = note.gettext();

                noteClickedPosition=position;
                Intent intent = new Intent(MainActivity.this,createNote.class);
                 intent.putExtra("isViewOrUpdate",true);
                intent.putExtra("documentSnapshot", (Serializable) note);
                startActivityForResult(intent,requestUpdateNote);

            }
        });*/
        noteAdapter.setOnLongCliclListener(new NoteAdapter.onLongClickListener() {
            @Override
            public void onLongClickListener(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
            }



            
        });
    }

    private void NoteDeleteDialog()
    {
        if(dialogDeleteNote == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete,(ViewGroup) findViewById(R.id.layoutDeleteContainerNote)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow() != null){
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            });

            view.findViewById(R.id.textCancelNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
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