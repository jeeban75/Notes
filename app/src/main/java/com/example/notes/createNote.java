package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class createNote extends AppCompatActivity {

    ImageView imageBack,imageSave;
    EditText inputNoteTitle,inputNoteSubtitle,inputNoteText;
    TextView textDateTime;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        inputNoteTitle = (EditText)findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = (EditText)findViewById(R.id.inputNoteSubtitle);
        inputNoteText = (EditText)findViewById(R.id.inputNote);

        firebaseFirestore = FirebaseFirestore.getInstance();

        textDateTime = (TextView)findViewById(R.id.textDateTime);
        textDateTime.setText(
                new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        imageSave = findViewById(R.id.imageSave);

        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title = inputNoteTitle.getText().toString();
                String Subtitle = inputNoteSubtitle.getText().toString();
                String Text = inputNoteText.getText().toString();
                Note data = new Note(Title,Subtitle,Text);
                /*firebaseFirestore.collection("Notes").document("User").set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Note Added",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(createNote.this,MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(getApplicationContext(),"An error Occured"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });*/

                DocumentReference documentReference = firebaseFirestore.collection("Notes").document("user");
                Map<String,Object> user = new HashMap<>();
                //user.put("Title",Title);
                //user.put("Subtitle",Subtitle);
                //user.put("Text",Text);
                documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(createNote.this, "data inserted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(createNote.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    //Validation
    private void saveNote() {
        if (inputNoteTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty()
                && inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}