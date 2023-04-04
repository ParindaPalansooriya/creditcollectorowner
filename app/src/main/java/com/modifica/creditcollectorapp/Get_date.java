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

public class Get_date extends AppCompatActivity {

    DatePicker datePicker;
    String ful_date_change;

    private ArrayList<String> offices_List = new ArrayList<>();
    private String[] array_root = new String [4];
    String FileName = "CraditCollactor";
    public static final String EXTRA_TEXT_DATE = "com.modifica.creditcollector.EXTRA_TEXT_DATE";

    private Button submit_button;

    private String root_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_date);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Spinner root = (Spinner) findViewById(R.id.spinner_off);
        submit_button = (Button) findViewById(R.id.button_off);

        readFile();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Registation").child(array_root[2]).child("Offices");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Get_date.this, android.R.layout.simple_list_item_1,offices_List);

        root.setAdapter(arrayAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String off = dataSnapshot.getKey();
                offices_List.add(off);
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


        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                ful_date_change = datePicker.getYear()+"|"+datePicker.getMonth()+"|"+datePicker.getDayOfMonth();
                Toast.makeText(Get_date.this,ful_date_change,Toast.LENGTH_SHORT).show();
            }
        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root_num = root.getSelectedItem().toString().trim();
                saveFile(array_root[0],array_root[1],array_root[2],root_num);
                open_Due();
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
                array_root[i] = line;
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

    private void open_Due() {

        Intent intent = new Intent(this,TodayTotal_list.class);
        intent.putExtra(EXTRA_TEXT_DATE,ful_date_change);
        startActivity(intent);
        finish();

    }
}
