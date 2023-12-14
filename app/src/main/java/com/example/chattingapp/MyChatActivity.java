package com.example.chattingapp;

import static androidx.core.graphics.PaintCompat.setBlendMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;



public class MyChatActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private final int REQ_CODE = 100;
    private TextView textViewChat;
    private EditText editTextMessage;
    private FloatingActionButton fab;
    private RecyclerView rvChat;


    String userName, otherName;


    String key, key1;
    FirebaseDatabase database;
    DatabaseReference reference;

    MessageAdapter adapter;
    List<ModelClass> list;

    UUID uuid;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);



        imageViewBack = findViewById(R.id.imageViewBack);
        textViewChat = findViewById(R.id.textViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab = findViewById(R.id.fab);
        rvChat = findViewById(R.id.rvChat);


        rvChat.addItemDecoration(new NoSpaceItemDecoration());

        rvChat.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();

        userName = getIntent().getStringExtra("userName");
        otherName = getIntent().getStringExtra("otherName");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        textViewChat.setText(otherName);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMessage.getText().toString();
                if (!message.equals("")) {
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });

        getMessage();
    }




    public void sendMessage(String message){

        key = reference.child("Messages").child(userName).child(otherName).push().getKey();

        Map<String,Object> messageMap = new HashMap<>();

        messageMap.put("message",message);
        messageMap.put("from",userName);
        reference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
                }
            }
        });

        reference.child("Ids").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("Ids").child(otherName).child(userName).child(key).setValue(messageMap);
                }
            }
        });
    }
    public void getMessage(){
        reference.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                System.out.println(modelClass);
                adapter.notifyDataSetChanged();
                rvChat.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        adapter = new MessageAdapter(list,userName);
        rvChat.setAdapter(adapter);
    }


    public class NoSpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, 0);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(layoutParams);
        }
    }

    boolean appInBackgound=false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQ_CODE: {
//                if (resultCode == RESULT_OK && data!=null) {
//                    ArrayList result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    editTextMessage.setText((CharSequence) result.get(0));
//                }
//                break;
//            }
//        }
//    }
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && !result.isEmpty()) {
                        String recognizedText = result.get(0);

                        // Get the existing text in the EditText
                        String existingText = editTextMessage.getText().toString();

                        // Append the recognized text to the existing text
                        String newText = existingText + " " + recognizedText;

                        // Set the combined text to the EditText
                        editTextMessage.setText(newText);
                    }
                }
                break;
            }
        }
    }

}


//
//
//
//package com.bharath.El_chat;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MyChatActivity extends AppCompatActivity {
//    private ImageView imageViewBack;
//    private TextView textViewChat;
//    private EditText editTextMessage;
//    private FloatingActionButton fab;
//    private RecyclerView rvChat;
//
//    String userName, otherName;
//
//    FirebaseDatabase database;
//    DatabaseReference reference;
//
//    MessageAdapter adapter;
//    List <ModelClass> list;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_chat);
//
//        imageViewBack = findViewById(R.id.imageViewBack);
//        textViewChat = findViewById(R.id.textViewChat);
//        editTextMessage = findViewById(R.id.editTextMessage);
//        fab = findViewById(R.id.fab);
//        rvChat = findViewById(R.id.rvChat);
//
//        rvChat.setLayoutManager(new LinearLayoutManager(this));
//        list = new ArrayList<>();
//
//        userName = getIntent().getStringExtra("userName");
//        otherName = getIntent().getStringExtra("otherName");
//
//        database = FirebaseDatabase.getInstance();
//        reference =database.getReference();
//
//
//        textViewChat.setText(otherName);
//
//        imageViewBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(MyChatActivity.this, MainActivity.class);
//                startActivity(i);
//            }
//        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = editTextMessage.getText().toString();
//                if(!message.equals("")){
//                    sendMessage(message);
//                    editTextMessage.setText("");
//                }
//            }
//        });
//
//        getMessage();
//    }
//        public void deleteMessage(String s){
//
//        String other = otherName;
//        String user = userName;
//        database = FirebaseDatabase.getInstance();
//        reference =database.getReference();
//        reference.child("Messages").child(user).child(other).child(s).removeValue();
//        adapter.notifyDataSetChanged();
//
//    }
//
//    public void sendMessage(String message){
//        String key = reference.child("Messages").child(userName).child(otherName).push().getKey();
//        Map<String,Object> messageMap = new HashMap<>();
//        messageMap.put("key",key);
//        messageMap.put("message",message);
//        messageMap.put("from",userName);
//        reference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    reference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
//                }
//            }
//        });
//    }
//    public void getMessage(){
//        reference.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                ModelClass modelClass = snapshot.getValue(ModelClass.class);
//                list.add(modelClass);
//                adapter.notifyDataSetChanged();
//                rvChat.scrollToPosition(list.size()-1);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        adapter = new MessageAdapter(list,userName);
//        rvChat.setAdapter(adapter);
//    }
//}