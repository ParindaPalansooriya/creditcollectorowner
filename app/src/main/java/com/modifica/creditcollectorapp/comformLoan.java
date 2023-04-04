package com.modifica.creditcollectorapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class comformLoan extends AppCompatActivity {

    private String[] loan02 = new String [4];
    String FileName = "CraditCollactor";
    DatabaseReference databaseReference;
    String ROOT_NUMBER,LOAN_NUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comform_loan);

        readFile();
        final Intent intent = getIntent();
        ROOT_NUMBER = intent.getStringExtra(TO_Active_loanInfo.EXTRA_TEXT_ROOT_NUMBER);
        LOAN_NUMBER = intent.getStringExtra(TO_Active_loanInfo.EXTRA_TEXT_LOAN_NUMBER);


       databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(loan02[2]);

        Button button = (Button) findViewById(R.id.button_loan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("TO_activation").child(ROOT_NUMBER).child(LOAN_NUMBER).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                Intent intent1 = new Intent(comformLoan.this,Loan_info.class);
                startActivity(intent1);
                finish();
                deletval();

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
                loan02[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

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
