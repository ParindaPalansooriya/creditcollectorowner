package com.modifica.creditcollectorapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Online_workers_list extends AppCompatActivity {

    ListView online_list;
    private String[] onlinelist = new String [4];
    String FileName = "CraditCollactor";
    DatabaseReference databaseReference;

    private ArrayList<String> onlineWorker = new ArrayList<>();
    private ArrayList <String> onlineroot = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_workers_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        online_list = (ListView) findViewById(R.id.online_lists);
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("Registation").child(onlinelist[2]).child("Offices");


        final MyAdapter_online onlineAdapter = new MyAdapter_online(Online_workers_list.this,onlineroot,onlineWorker);
      //  final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,onlineWorker);
        online_list.setAdapter(onlineAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String root = dataSnapshot.getKey();
                onlineroot.add(root);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                  //  onlineroot.clear();
                    onlineWorker.clear();

                   /* for (DataSnapshot onlineDataSnap : dataSnapshot.getChildren()) {

                        String root = dataSnapshot.getKey();
                        onlineroot.add(root);

                    }*/

                    for (int p=0; p<onlineroot.size(); p++){
                        String rootname = dataSnapshot.child(String.valueOf(onlineroot.get(p))).getValue().toString();
                       // String rootname = "nio";
                        onlineWorker.add(rootname);
                        onlineAdapter.notifyDataSetChanged();
                       // arrayAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void readFile() {

        try{

            FileInputStream input = openFileInput(FileName);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
            int i=0;
            while((line=bufferedReader.readLine()) !=null){
                onlinelist[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}
