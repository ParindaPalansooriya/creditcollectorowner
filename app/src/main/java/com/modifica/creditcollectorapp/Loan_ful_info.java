package com.modifica.creditcollectorapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Loan_ful_info extends AppCompatActivity {


    private DatabaseReference databaseReference;

    private String[] loan = new String [4];
    String FileName = "CraditCollactor";

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView10;

    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_ful_info);

        readFile();

        textView1 = (TextView) findViewById(R.id.LOAN_NUM_TEXT_LN_Info);
        textView2 = (TextView) findViewById(R.id.LOAN_NUM_TEXT2_LN_Info);
        textView3 = (TextView) findViewById(R.id.LOAN_NUM_TEXT3_LN_Info);
        textView4 = (TextView) findViewById(R.id.LOAN_NUM_TEXT4_LN_Info);
        textView5 = (TextView) findViewById(R.id.LOAN_NUM_TEXT5_LN_Info);
        textView6 = (TextView) findViewById(R.id.LOAN_NUM_TEXT6_LN_Info);
        textView7 = (TextView) findViewById(R.id.LOAN_NUM_TEXT7_LN_Info);
        textView8 = (TextView) findViewById(R.id.LOAN_NUM_TEXT8_LN_Info);
        textView9 = (TextView) findViewById(R.id.LOAN_NUM_TEXT9_LN_Info);
        textView10 = (TextView) findViewById(R.id.LOAN_NUM_TEXT10_LN_Info);

        Button OK = (Button) findViewById(R.id.button_conferm_LN_Info);


        Intent intent = getIntent();
        key = intent.getStringExtra(Loan_info_list.EXTRA_TEXT_LN_INFO);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(loan[2]).child(key);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Loan loan = dataSnapshot.getValue(Loan.class);

                textView1.setText(loan.getLoan_num());
                textView2.setText(loan.getNik_name());
                textView3.setText(loan.getF_amount());
                textView4.setText(loan.getLoan_period());
                textView5.setText(loan.getLoan_rate());
                textView6.setText(loan.getFull_terms());
                textView7.setText(loan.getAmount_per_term());
                textView8.setText(loan.getCollection_type());
                textView9.setText(loan.getRoot_name());
                textView10.setText(loan.getStart_date());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                loan[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}
