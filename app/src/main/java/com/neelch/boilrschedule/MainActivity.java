package com.neelch.boilrschedule;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button sendData;
    EditText data;
    //WebView web;
    //int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendData = (Button)findViewById(R.id.button1);
        data = (EditText)(findViewById(R.id.editText1));
        //step = 0;

        sendData.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent myIntent = new Intent(v.getContext(), Activity2.class).putExtra("username", data.getText().toString());
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
