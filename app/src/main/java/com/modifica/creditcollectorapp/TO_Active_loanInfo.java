package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.annotation.NonNull;
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
import java.util.Calendar;

public class TO_Active_loanInfo extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

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

    private Button conform;
    private Button reject;

    String key,S_date,ful_date;
    String ROOT_NUMBER,LOAN_NUMBER;

    public static final String EXTRA_TEXT_LOAN_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_LOAN_NUMBER";
    public static final String EXTRA_TEXT_ROOT_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_ROOT_NUMBER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__active_loan_info);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();


        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);

        ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);


        textView1 = (TextView) findViewById(R.id.LOAN_NUM_TEXT);
        textView2 = (TextView) findViewById(R.id.LOAN_NUM_TEXT2);
        textView3 = (TextView) findViewById(R.id.LOAN_NUM_TEXT3);
        textView4 = (TextView) findViewById(R.id.LOAN_NUM_TEXT4);
        textView5 = (TextView) findViewById(R.id.LOAN_NUM_TEXT5);
        textView6 = (TextView) findViewById(R.id.LOAN_NUM_TEXT6);
        textView7 = (TextView) findViewById(R.id.LOAN_NUM_TEXT7);
        textView8 = (TextView) findViewById(R.id.LOAN_NUM_TEXT8);
        textView9 = (TextView) findViewById(R.id.LOAN_NUM_TEXT9);

        conform = (Button) findViewById(R.id.button_conferm);
        reject = (Button) findViewById(R.id.button_reject);


        Intent intent = getIntent();
        key = intent.getStringExtra(ToActive_loan.EXTRA_TEXT_ACTIVE);
        ROOT_NUMBER = intent.getStringExtra(ToActive_loan.EXTRA_TEXT_ROOT_NUMBER);
        LOAN_NUMBER = intent.getStringExtra(ToActive_loan.EXTRA_TEXT_LOAN_NUMBER);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(loan[2]);

        databaseReference.child("TO_activation").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Loan loan = dataSnapshot.getValue(Loan.class);

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
                S_date = loan.getStart_date();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                active_loan();
                openActive_loan_list();

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_loan();
            }
        });




    }


    private void openActive_loan_list() {
        Intent intent = new Intent(this,ToActive_loan.class);
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


    public void active_loan(){
        final String LN = textView1.getText().toString().trim();
        String NN = textView2.getText().toString().trim();
        String FA = textView3.getText().toString().trim();
        String LP = textView4.getText().toString().trim();
        String LR = textView5.getText().toString().trim();
        String FT = textView6.getText().toString().trim();
        String APT = textView7.getText().toString().trim();
        String CT = textView8.getText().toString().trim();
        final String RN = textView9.getText().toString().trim();

        final Loan setLoan = new Loan(LN,NN,FA,APT,FT,FA,LR,LP,CT,"YES",RN,S_date,ful_date);
        databaseReference.child(RN).child(LN).setValue(setLoan);
        deletval();

    }

    public void reject_loan(){
        String LN = textView1.getText().toString().trim();
        String NN = textView2.getText().toString().trim();
        String FA = textView3.getText().toString().trim();
        String LP = textView4.getText().toString().trim();
        String LR = textView5.getText().toString().trim();
        String FT = textView6.getText().toString().trim();
        String APT = textView7.getText().toString().trim();
        String CT = textView8.getText().toString().trim();
        String RN = textView9.getText().toString().trim();

        Loan setLoan = new Loan(LN,NN,FA,APT,FT,FA,LR,LP,CT,"NO",RN,S_date,ful_date);

        databaseReference.child("Reject_Loan").child(RN).child(ful_date).push().child(LN).setValue(setLoan);
        databaseReference.child(RN).child("free").child(LN).setValue("free");
        deletval();


        openActive_loan_list();
    }

    private void deletval(){
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("loan_num").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("nik_name").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("f_amount").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("amount_per_term").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("full_terms").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("available_amount").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("loan_rate").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("loan_period").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("collection_type").removeValue();
        // databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("activation").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("root_name").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("start_date").removeValue();
        databaseReference.child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).child("end_date").removeValue();

    }


}
