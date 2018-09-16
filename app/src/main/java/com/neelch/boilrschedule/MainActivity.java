package com.neelch.boilrschedule;

import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button sendData;
    EditText data;
    FirebaseFirestore db;
    //WebView web;
    //int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendData = (Button)findViewById(R.id.button1);
        data = (EditText)(findViewById(R.id.editText1));
        //step = 0;

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        sendData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DocumentReference docRef = db.collection("userInfo").document(data.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("HEY", "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> data = (Map<String, Object>) document.getData();
                                ArrayList<String> locations = (ArrayList<String>) data.get("locations");
                                ArrayList<String> classes = (ArrayList<String>) data.get("classes");
                                ArrayList<String> times = (ArrayList<String>) data.get("times");
                                for(int i = 0; i < 3; i++){
                                    Log.d("HEY", classes.get(i).toString());
                                    Log.d("HEY", times.get(i).toString());
                                    Log.d("HEY", locations.get(i).toString());
                                }

                            } else {
                                Log.d("HEY", "No such document");
                            }
                        } else {
                            Log.d("HEY", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        /*
        web = (WebView)findViewById(R.id.web1);
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){}

            @Override
            public void onPageFinished(WebView view, String url){
                Log.d("HEYYYYYYY", "finished");
                if(step == 0){
                    step++;
                } else if(step == 1){
                    Log.d("HEYYYYYYY", "we in here");
                    Log.d("HEYYYYYYY", "" + step);
                    step++;
                    //web.evaluateJavascript("document.getElementById('_TargetedContent_WAR_luminis_INSTANCE_fVLQ61ZLElsj_tcUserViewForm').getElementsByTagName('div')[0].getElementsByClassName('section-container-enduser')[0].getElementsByClassName('section-container-enduser')[0].getElementsByTagName('p')[0].getElementsByTagName('span')[0].getElementsByClassName('listItem')[2].click();", null);
                }
            }
        });
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("https://selfservice.mypurdue.purdue.edu/prod/bwskfshd.P_CrseSchd");

        sendData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                web.getSettings().setDomStorageEnabled(true);
                web.evaluateJavascript("document.getElementById('username').value = 'chaudh11'; " +
                        "document.getElementById('password').value = '"+data.getText().toString()+"';" +
                        "document.getElementsByName('submit')[0].click();", null);
            }
        });
        */
    }
}
