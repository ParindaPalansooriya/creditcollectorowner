package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Finder extends AppCompatActivity {

    private Button button3;
    private DatabaseReference BD_ref;
    private EditText editText8;
    private TextView textView_phone;
    private TextView textView_Fname;
    private TextView textView_Lname;
    private TextView textView_Nname;
    private TextView textView_root;

    private String[] array_3 = new String [4];

    String FileName = "CraditCollactor";

    private String nic,f_name,l_name,n_name="null",phone_num;

    public static final String EXTRA_TEXT_NIK_NAME = "com.modifica.creditcollector.EXTRA_TEXT_NIK_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        button3 = (Button) findViewById(R.id.button3);
        editText8 = (EditText) findViewById(R.id.editText8);
        textView_Fname = (TextView) findViewById(R.id.textView_Fname);
        textView_Lname = (TextView) findViewById(R.id.textView_Lname);
        textView_phone = (TextView) findViewById(R.id.textView_phone);
        textView_Nname = (TextView) findViewById(R.id.textView_Nname);
        textView_root = (TextView) findViewById(R.id.textView_root01);

        final Intent intent = getIntent();
        final String text = intent.getStringExtra(InfoList.EXTRA_TEXT_NIC);

        textView_root.setText("Root number : "+array_3[3]);
        textView_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Finder.this,Offices_list.class);
                startActivity(intent1);
                finish();
            }
        });



        if(text != null){

            BD_ref = FirebaseDatabase.getInstance().getReference().child("Customers").child(array_3[2]).child(array_3[3]).child(text);

            BD_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nic = dataSnapshot.child("cus_nic").getValue().toString();
                    editText8.setText(nic);
                    f_name = dataSnapshot.child("cus_fname").getValue().toString();
                    textView_Fname.setText(f_name);
                    l_name = dataSnapshot.child("cus_lname").getValue().toString();
                    textView_Lname.setText(l_name);
                    phone_num = dataSnapshot.child("cus_phone").getValue().toString();
                    textView_phone.setText(phone_num);
                    n_name = dataSnapshot.child("cus_nikname").getValue().toString();
                    textView_Nname.setText(n_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        editText8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openactivity_list();
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                select();
            }
        });



    }

    public void openactivity_create_loan(){

            Intent intent = new Intent(this, Create_loan.class);
            intent.putExtra(EXTRA_TEXT_NIK_NAME, n_name);
            startActivity(intent);
            finish();

    }

    public void openactivity_list(){
        Intent intent = new Intent(this,InfoList .class);
        startActivity(intent);
        finish();
    }

    public void select(){
            openactivity_create_loan();
    }

    public void readFile() {

        try{

            FileInputStream input = openFileInput(FileName);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
            int i=0;
            while((line=bufferedReader.readLine()) !=null){
                array_3[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }
}
