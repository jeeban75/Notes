package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button submit_login;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addListenerOnButton();

        checkBox = (CheckBox)findViewById(R.id.Cbox);
    }

    public void addListenerOnButton(){
        final Context context =this;
        submit_login = (Button) findViewById(R.id.submit);

        submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                {
                    submit_login.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext() , "Login successful" , Toast.LENGTH_SHORT)
                            .show();
                }
                else
                {
                    Toast.makeText(getApplicationContext() , "Prove that you are not a robot" , Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}