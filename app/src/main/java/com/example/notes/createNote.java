package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class createNote extends AppCompatActivity {

    ImageView imageBack, imageSave, imageNote,removeURl,removeImage;
    EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    TextView textDateTime, textWebURl;
    private LinearLayout layoutWebURl;
    private String ImagePath;
    private String SelectedNoteColor;
    private View SubtitleIndicator;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private AlertDialog dialogAddURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        inputNoteTitle = (EditText) findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = (EditText) findViewById(R.id.inputNoteSubtitle);
        inputNoteText = (EditText) findViewById(R.id.inputNote);
        SubtitleIndicator = findViewById(R.id.viewsubtitleIndicator);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        textWebURl = findViewById(R.id.textWebUrl);
        layoutWebURl = findViewById(R.id.layoutWebUrl);
        textDateTime = (TextView) findViewById(R.id.textDateTime);
        textDateTime.setText(
                new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        imageNote = findViewById(R.id.imageNote);
        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SelectedNoteColor = "#333333";
        ImagePath = "";
        Miscellaneous();
        SubtitleIndiciatorColor();

        imageSave = findViewById(R.id.imageSave);

        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();

            }
        });

        removeURl = findViewById(R.id.imageRemoveWebURl);
        removeURl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWebURl.setText(null);
                layoutWebURl.setVisibility(View.GONE);
            }
        });

        removeImage = findViewById(R.id.imageRemoveImage);
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                removeImage.setVisibility(View.GONE);
            }
        });
    }


    //Validation
    private void saveNote() {
        if (inputNoteTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty()
                || inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String Title = inputNoteTitle.getText().toString();
            String Subtitle = inputNoteSubtitle.getText().toString();
            String Text = inputNoteText.getText().toString();
            String DateTime = textDateTime.getText().toString();
            DocumentReference documentReference = firebaseFirestore.collection("Notes").document("Test").collection("Data").document();
            Map<String, Object> note = new HashMap<>();
            note.put("Title", Title);
            note.put("Subtitle", Subtitle);
            note.put("Text", Text);
            note.put("DateTime", DateTime);
            if(layoutWebURl.getVisibility() == View.VISIBLE){
                note.put("Web Link",textWebURl.getText().toString());
            }
            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(createNote.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(createNote.this, MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(createNote.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void Miscellaneous() {
        final LinearLayout linearLayout = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        linearLayout.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = linearLayout.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = linearLayout.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = linearLayout.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = linearLayout.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = linearLayout.findViewById(R.id.imageColor5);

        linearLayout.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                SubtitleIndiciatorColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                SubtitleIndiciatorColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                SubtitleIndiciatorColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                SubtitleIndiciatorColor();
            }
        });

        linearLayout.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                SubtitleIndiciatorColor();
            }
        });
        linearLayout.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            createNote.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }

            }
        });

        linearLayout.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                URlDialog();
            }
        });
    }

    private void SubtitleIndiciatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) SubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(SelectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        removeImage.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private  void  URlDialog(){
        if(dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(createNote.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_addurl,(ViewGroup)findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);

            dialogAddURL = builder.create();
            if(dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputUrl = view.findViewById(R.id.inputUrl);
            inputUrl.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                 public void onClick(View v) {
                        if(inputUrl.getText().toString().trim().isEmpty()){
                            Toast.makeText(createNote.this, "Enter URL", Toast.LENGTH_SHORT).show();
                        }else if(!Patterns.WEB_URL.matcher(inputUrl.getText().toString()).matches()){
                            Toast.makeText(createNote.this, "Enter Valid URl", Toast.LENGTH_SHORT).show();
                        }else{
                            textWebURl.setText(inputUrl.getText().toString());
                            layoutWebURl.setVisibility(View.VISIBLE);
                            dialogAddURL.dismiss();
                        }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });

        }
        dialogAddURL.show();
    }
}