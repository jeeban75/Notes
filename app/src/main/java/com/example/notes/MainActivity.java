package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity   {


    ImageView imageAddNoteMain;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    public static final int requestAddNote = 1;
    private int noteClickedPosition = -1;
    private AlertDialog dialogDeleteNote;

    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;

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
    private void showRecycleView() {
        Query query = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("Data")
                .orderBy("DateTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> alluserNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(alluserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull  NoteViewHolder holder, int position, @NonNull  Note note) {

                holder.Title.setText(note.getTitle());
                holder.Subtitle.setText(note.getSubtitle());
                holder.DateTime.setText(note.getDateTime());

                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),UpdateDeleteNote.class);
                        intent.putExtra("isViewOrUpdate",true);
                        intent.putExtra("documentSnapshot", note);
                        intent.putExtra("DocId",docId);
                        v.getContext().startActivity(intent);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View view = LayoutInflater.from(getApplicationContext()).inflate(
                                R.layout.layout_delete, (ViewGroup) findViewById(R.id.layoutDeleteContainerNote)
                        );
                        builder.setView(view);
                        dialogDeleteNote = builder.create();
                        if (dialogDeleteNote.getWindow() != null) {
                            dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        }
                        view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DocumentReference documentReference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("Data").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                        dialogDeleteNote.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialogDeleteNote.dismiss();
                                    }
                                });

                            }
                        });

                        view.findViewById(R.id.textCancelNote).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogDeleteNote.dismiss();
                            }
                        });
                        dialogDeleteNote.show();
                        Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_note, parent, false);
                return new NoteViewHolder(v);
            }


        };



        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);


    }

    public class NoteViewHolder extends  RecyclerView.ViewHolder{

        TextView Title,Subtitle,DateTime;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.textTitle);
            Subtitle = itemView.findViewById(R.id.textSubtitle);
            DateTime = itemView.findViewById(R.id.textDateandTime);



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