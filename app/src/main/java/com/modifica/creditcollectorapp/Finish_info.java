package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;

public class Finish_info extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private String[] loan = new String [4];
    String FileName = "CraditCollactor";

    private int year,month,date;

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

    private Button conform;

    private String key,S_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_info);

        readFile();


        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        final String ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);


        textView1 = (TextView) findViewById(R.id.LOAN_NUM_TEXT_finish);
        textView2 = (TextView) findViewById(R.id.LOAN_NUM_TEXT2_finish);
        textView3 = (TextView) findViewById(R.id.LOAN_NUM_TEXT3_finish);
        textView4 = (TextView) findViewById(R.id.LOAN_NUM_TEXT4_finish);
        textView5 = (TextView) findViewById(R.id.LOAN_NUM_TEXT5_finish);
        textView6 = (TextView) findViewById(R.id.LOAN_NUM_TEXT6_finish);
        textView7 = (TextView) findViewById(R.id.LOAN_NUM_TEXT7_finish);
        textView8 = (TextView) findViewById(R.id.LOAN_NUM_TEXT8_finish);
        textView9 = (TextView) findViewById(R.id.LOAN_NUM_TEXT9_finish);
        textView10 = (TextView) findViewById(R.id.LOAN_NUM_TEXT10_finish);

        conform = (Button) findViewById(R.id.button_conferm_finish);


        Intent intent = getIntent();
        key = intent.getStringExtra(TofinishLoan.EXTRA_TEXT_FINISH);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(loan[2]);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Loan loan = dataSnapshot.child("TO_finish").child(key).getValue(Loan.class);

                assert loan != null;
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
                S_date = loan.getStart_date();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String LN = textView1.getText().toString().trim();
                final String NN = textView2.getText().toString().trim();
                final String FA = textView3.getText().toString().trim();
                final String LP = textView4.getText().toString().trim();
                final String LR = textView5.getText().toString().trim();
                final String FT = textView6.getText().toString().trim();
                final String APT = textView7.getText().toString().trim();
                final String CT = textView8.getText().toString().trim();
                final String RN = textView9.getText().toString().trim();


                Loan setLoan = new Loan(LN,NN,FA,APT,FT,FA,LR,LP,CT,"finish",RN,S_date,ful_date);
                databaseReference.child("Histry").child(RN).child(ful_date).push().child(LN).setValue(setLoan);
                databaseReference.child(RN).child("free").child(LN).setValue("free");
                deletval(key);
                openActive_loan_list();

            }
        });

    }


    private void openActive_loan_list() {
        Intent intent = new Intent(Finish_info.this,TofinishLoan.class);
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
                loan[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    private void deletval(String key){
        databaseReference.child("TO_finish").child(key).child("loan_num").removeValue();
        databaseReference.child("TO_finish").child(key).child("nik_name").removeValue();
        databaseReference.child("TO_finish").child(key).child("f_amount").removeValue();
        databaseReference.child("TO_finish").child(key).child("amount_per_term").removeValue();
        databaseReference.child("TO_finish").child(key).child("full_terms").removeValue();
        databaseReference.child("TO_finish").child(key).child("available_amount").removeValue();
        databaseReference.child("TO_finish").child(key).child("loan_rate").removeValue();
        databaseReference.child("TO_finish").child(key).child("loan_period").removeValue();
        databaseReference.child("TO_finish").child(key).child("collection_type").removeValue();
        // databaseReference.child("TO_finish").child(ROOT_NUMBER).child(LOAN_NUMBER).child("activation").removeValue();
        databaseReference.child("TO_finish").child(key).child("root_name").removeValue();
        databaseReference.child("TO_finish").child(key).child("start_date").removeValue();
        databaseReference.child("TO_finish").child(key).child("end_date").removeValue();

    }

}
