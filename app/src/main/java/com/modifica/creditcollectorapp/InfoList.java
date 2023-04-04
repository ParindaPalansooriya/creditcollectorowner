package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InfoList extends AppCompatActivity {

    private DatabaseReference data_ref;
    private ListView niclist;
    private ArrayList<String> nic_arrayList = new ArrayList<>();

    private String[] array_5 = new String [4];
    String FileName = "CraditCollactor";

    public static final String EXTRA_TEXT_NIC = "com.modifica.creditcollector.EXTRA_TEXT_NIC";
    private String chil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        niclist = (ListView) findViewById(R.id.nic_list);

        data_ref = FirebaseDatabase.getInstance().getReference().child("Customers").child(array_5[2]).child(array_5[3]);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nic_arrayList);

        niclist.setAdapter(arrayAdapter);


        data_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ID_Num = dataSnapshot.getKey();
                nic_arrayList.add(ID_Num);
                arrayAdapter.notifyDataSetChanged();
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

            niclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(InfoList.this,nic_arrayList.get(i),Toast.LENGTH_SHORT).show();
                    chil = nic_arrayList.get(i);
                    openFinder();
                }
            });

        }

        public void openFinder(){
            Intent intent = new Intent(this, Finder.class);
            intent.putExtra(EXTRA_TEXT_NIC,chil);
            startActivity(intent);
            finish();
        }

    public void readFile() {

        try{

            FileInputStream input = openFileInput(FileName);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
            int i=0;
            while((line=bufferedReader.readLine()) !=null){
                array_5[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}

