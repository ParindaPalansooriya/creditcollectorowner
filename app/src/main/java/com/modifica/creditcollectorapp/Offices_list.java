package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Offices_list extends AppCompatActivity {

    DatabaseReference databaseReference;
    private ListView listView;
    private String offiese_name;
    private ArrayList<String> offices_List = new ArrayList<>();
    private String[] array_off = new String [4];
    String FileName = "CraditCollactor";


    private ArrayList<String> offices_List01 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offices_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        listView = (ListView) findViewById(R.id.offices_listView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registation").child(array_off[2]).child("Offices");
      //  final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,offices_List);
        final MyAdapter_online onlineAdapter = new MyAdapter_online(Offices_list.this,offices_List,offices_List01);
        listView.setAdapter(onlineAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String off = dataSnapshot.getKey();
                offices_List.add(off);
                offices_List01.add("0");
                onlineAdapter.notifyDataSetChanged();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                offiese_name = offices_List.get(i);
                saveFile(array_off[0],array_off[1],array_off[2],offiese_name);
                finish();

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
                array_off[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    public void saveFile(String A,String B,String C,String D)  {

        try{
            String NAME = A+"\n"+B+"\n"+C+"\n"+D;
            FileOutputStream FOS = openFileOutput(FileName, Context.MODE_PRIVATE);
            FOS.write(NAME.getBytes());
            FOS.close();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}
