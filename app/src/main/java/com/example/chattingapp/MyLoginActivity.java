package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyLoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonSignIn;
    private TextView textViewForgot,textViewSignUp;
    private ImageView icon;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;



    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null){
            Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);

//        ToggleButton toggleButton = findViewById(R.id.togglePasswordVisibility);
        TextInputEditText editTextPassword = findViewById(R.id.editTextPassword);

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        //buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewForgot = findViewById(R.id.textViewForgot);
        icon= findViewById(R.id.imageViewIconLogin);
        auth = FirebaseAuth.getInstance();

//        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                // Show password
//                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            } else {
//                // Hide password
//                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            }
//
//            // Move the cursor to the end of the text
//            editTextPassword.setSelection(editTextPassword.length());
//        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    signin(email,password);
                }
                else{
                    Toast.makeText(MyLoginActivity.this, "Please enter login details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyLoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyLoginActivity.this, ForgetAcivity.class);
                startActivity(i);
            }
        });

    }
    public void signin(String email, String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
                    Toast.makeText(MyLoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MyLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}