package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Calendar;

public class Date_for_map extends AppCompatActivity {

    DatePicker datePicker_map;
    String ful_date_change_map;

    private ArrayList<String> offices_List_map = new ArrayList<>();
    private String[] array_root_map = new String [4];
    String FileName = "CraditCollactor";
    public static final String EXTRA_TEXT_DATE_MAP = "com.modifica.creditcollector.EXTRA_TEXT_DATE_MAP";

    private Button submit_button_map;
    private String root_num_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_for_map);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Spinner root_map = (Spinner) findViewById(R.id.spinner_off_map);
        submit_button_map = (Button) findViewById(R.id.button_off_map);

        readFile();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Registation").child(array_root_map[2]).child("Offices");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Date_for_map.this, android.R.layout.simple_list_item_1,offices_List_map);

        root_map.setAdapter(arrayAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String off = dataSnapshot.getKey();
                offices_List_map.add(off);
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


        datePicker_map = (DatePicker) findViewById(R.id.datePicker_map);
        Calendar calendar = Calendar.getInstance();
        datePicker_map.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                ful_date_change_map = datePicker.getYear()+"|"+datePicker.getMonth()+"|"+datePicker.getDayOfMonth();
                Toast.makeText(Date_for_map.this,ful_date_change_map,Toast.LENGTH_SHORT).show();
            }
        });


        submit_button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root_num_map = root_map.getSelectedItem().toString().trim();
                saveFile(array_root_map[0],array_root_map[1],array_root_map[2],root_num_map);
                open_Map();
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
                array_root_map[i] = line;
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

    private void open_Map() {

        Intent intent = new Intent(this,Collaction_map.class);
        intent.putExtra(EXTRA_TEXT_DATE_MAP,ful_date_change_map);
        startActivity(intent);
        finish();

    }
}
