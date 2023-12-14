package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView imageViewCircle;
    private EditText editTextEmailSignUp,editTextUserNameSignUp;
    private EditText editTextPasswordSignUp;
    private Button buttonRegister;
    boolean imageControl  = false;
    Uri imageUri;
    String filePath;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        imageViewCircle = findViewById(R.id.imageViewCircleProfile);
        editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextUserNameSignUp = findViewById(R.id.editTextUserNameSignUp);
        buttonRegister = findViewById(R.id.buttonRegister);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        imageViewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailSignUp.getText().toString();
                String password = editTextPasswordSignUp.getText().toString();
                String userName = editTextUserNameSignUp.getText().toString();

                if(!email.equals("")&&!password.equals("")&&!userName.equals("")){
                    signup(email,password,userName);
                }
            }
        });


    }
//    public void imageChooser(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,1);
//    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");  // Use "image/*" instead of "images/*"
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
//            //using picasso library instead of bitmap
//
//            imageUri =  data.getData();
//            Picasso.get().load(imageUri).into(imageViewCircle);
//            imageControl=true;
//        }
//        else{
//            imageControl = false;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCircle);
            imageControl = true;
        } else {
            imageControl = false;
        }
    }





    //    public void signup(String email, String password, String userName){
//        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    reference.child("Users").child((auth.getUid())).child("userName").setValue(userName);
//
//                    if(imageControl==true){
//
//
//                        UUID randomID = UUID.randomUUID();
//                        String imageName = "images/"+randomID+".jpg";
//
//                        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(SignUpActivity.this, "image control", Toast.LENGTH_SHORT).show();
//                                StorageReference myStorageref = firebaseStorage.getReference(imageName);
//                                myStorageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String filepath = uri.toString();
//                                        reference.child("users").child(auth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(SignUpActivity.this, "Image uploaded Succesfully", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(SignUpActivity.this, "Error in updating picture!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });
//
//                    }
//                    else{
//                        //Toast.makeText(SignUpActivity.this, "image contol false", Toast.LENGTH_SHORT).show();
//                        reference.child("users").child(auth.getUid()).child("image").setValue("null");
//                    }
//                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                    intent.putExtra("userName", userName);
//                    startActivity(intent);
//                    finish();
//
//
//                }
//                else{
//                    Toast.makeText(SignUpActivity.this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
public void signup(String email, String password, String userName){
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                reference.child("Users").child(auth.getUid()).child("userName").setValue(userName);

                if (imageControl) {
                    UUID randomID = UUID.randomUUID();
//                        String imageName = "images/" + randomID + ".jpg";
                    String imageName = "images/" + randomID.toString() + ".jpg";

                    StorageReference imageRef = storageReference.child(imageName);

                    UploadTask uploadTask = imageRef.putFile(imageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    filePath = uri.toString();
                                    reference.child("Users").child(auth.getUid()).child("image").setValue(filePath)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    //Toast.makeText(SignUpActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUpActivity.this, "Error in updating picture!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Error in uploading image!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    reference.child("Users").child(auth.getUid()).child("image").setValue("null");
                }

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                //intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Invalid UserName or Password", Toast.LENGTH_SHORT).show();
            }
        }
    });
}


}


