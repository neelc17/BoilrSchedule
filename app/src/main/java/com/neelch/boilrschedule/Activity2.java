package com.neelch.boilrschedule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Map;

public class Activity2 extends AppCompatActivity {

    String un;
    ArrayList<String> locations, times, classes;
    FirebaseFirestore db;
    Button directions;
    GridView gw;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        un = getIntent().getStringExtra("username");
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        DocumentReference docRef = db.collection("userInfo").document(un);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("HEY", "DocumentSnapshot data: " + document.getData());
                    Map<String, Object> data1 = (Map<String, Object>) document.getData();
                    if (document.exists()) {
                        locations = (ArrayList<String>) data1.get("locations");
                        classes = (ArrayList<String>) data1.get("classes");
                        times = (ArrayList<String>) data1.get("times");
                        gw = (GridView)findViewById(R.id.gridView21);
                        ArrayList<String> bois = new ArrayList<String>();
                        bois.add("Classes");
                        bois.add("Locations");
                        bois.add("Times");
                        for(int i = 0; i < locations.size(); i++){
                            for(int j = 0; j < 3; j++){
                                if(j==0) bois.add(classes.get(i));
                                else if(j==1) bois.add(locations.get(i));
                                else if(j==2) bois.add(times.get(i));
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                                android.R.layout.simple_list_item_1, bois);
                        gw.setAdapter(adapter);

                        directions = (Button) findViewById(R.id.button21);
                        directions.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                sendNotification();
                                ArrayList<ArrayList<Double>> doubleTimes = new ArrayList<ArrayList<Double>>();
                                for(int i = 0; i < times.size(); i++){
                                    ArrayList<Double> temp = new ArrayList<Double>();
                                    temp.add((double)i);
                                    temp.add(Double.parseDouble(times.get(i)));
                                    doubleTimes.add(temp);
                                }
                                Log.d("HEY", doubleTimes.toString());
                                int j;
                                boolean flag = true;
                                ArrayList<Double> temp;
                                while (flag){
                                    flag= false;
                                    for(j=0; j < doubleTimes.size() - 1; j++) {
                                        if (doubleTimes.get(j).get(1) > doubleTimes.get(j+1).get(1)) {
                                            temp = doubleTimes.get(j);
                                            doubleTimes.set(j, doubleTimes.get(j+1));
                                            doubleTimes.set(j+1, temp);
                                            flag = true;
                                        }
                                    }
                                }
                                Log.d("HEY", doubleTimes.toString());
                                String address = "http://maps.google.com/maps?daddr=";
                                for(int i = 0; i < doubleTimes.size(); i++){
                                    if(i == doubleTimes.size()-1) address += (locations.get((int) Math.floor(doubleTimes.get(i).get(0))));
                                    else address += locations.get((int) Math.floor(doubleTimes.get(i).get(0))) + "+to:";
                                }
                                Log.d("HEY", address);
                                Uri gmmIntentUri = Uri.parse(address);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                                mapIntent.setPackage("com.google.android.apps.maps");

                                startActivity(mapIntent);
                            }
                        });
                    } else {
                        Log.d("HEY", "No such document");
                    }
                } else {
                    Log.d("HEY", "get failed with ", task.getException());
                }
            }
        });
    }

    public void sendNotification () {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), Activity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("30 Minutes until class!");
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
