package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetAcivity extends AppCompatActivity {

    private EditText editTextEmailForgot;
    private Button buttonForget;
    private ImageView reset;


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_acivity);

        editTextEmailForgot = findViewById(R.id.editTextEmailForget);
        buttonForget = findViewById(R.id.buttonForget);
        reset = findViewById(R.id.imageViewForget);

        auth = FirebaseAuth.getInstance();

        buttonForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailForgot.getText().toString();
                if (!email.equals("")){
                    passwordReset(email);
                }
                else{
                    Toast.makeText(ForgetAcivity.this, "Please enter valid Email address", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void passwordReset(String email){

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetAcivity.this, "Password reset Link has been send to"+email+"!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgetAcivity.this, "Sorry Gmail Account not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}