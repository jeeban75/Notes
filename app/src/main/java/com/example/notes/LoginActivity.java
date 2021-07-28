package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button Login;
    EditText setEmail,setPassword,edtResetPassword;
    TextView forgotPassword;
    private AlertDialog dialogForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setEmail = findViewById(R.id.loginEmail);
        setPassword = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        edtResetPassword = findViewById(R.id.editReset);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        Login = findViewById(R.id.btnlogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = setEmail.getText().toString();
                String Password = setPassword.getText().toString();

                if(Email.isEmpty() || Password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkMail();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        TextView btn=findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDialog();
            }
        });

    }

    private void resetDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View v = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.layout_forgot_password, (ViewGroup) findViewById(R.id.layoutForgotPassword)
        );
        builder.setView(v);
        edtResetPassword = v.findViewById(R.id.editReset);
        dialogForgotPassword = builder.create();
        if (dialogForgotPassword.getWindow() != null) {
            dialogForgotPassword.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        v.findViewById(R.id.textSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = edtResetPassword.getText().toString();
                if (mail.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                }else{

                    firebaseAuth.fetchSignInMethodsForEmail(mail)
                            .addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                                @Override
                                public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {

                                    firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(LoginActivity.this, "Reset Link is Sent Your Email", Toast.LENGTH_SHORT).show();
                                            dialogForgotPassword.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "Email sending Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(LoginActivity.this, "Provide Email is Not Registered with Us", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        v.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   dialogForgotPassword.dismiss();
            }
        });
        dialogForgotPassword.show();
    }


    private void checkMail() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified() == true)
        {
            Toast.makeText(this, "Logged In ", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else{
            Toast.makeText(this, "Verify Your Email First", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
