package com.modifica.creditcollectorapp;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Collection_list extends AppCompatActivity {

    ListView LW;
    MyAdapter adapter;
    private TextView textView_coll;
    String key;
    String loanNumber,nikName,amountPerTerms,fullTerms,availableTerms,fullAmount;
    private DatabaseReference BD_ref;
    private ArrayList <String> arrayList = new ArrayList<>();
    private ArrayList <String> f_amount = new ArrayList<>();
    private ArrayList <String> nik_name = new ArrayList<>();
    private ArrayList <String> full_terms = new ArrayList<>();
    private ArrayList <String> available_terms = new ArrayList<>();
    private ArrayList <String> amount_per_term = new ArrayList<>();
    private ArrayList <String> loan_num = new ArrayList<>();


    private String[] array_1 = new String [4];
    String FileName = "CraditCollactor";

    String inforChack = "NO";


    public static final String EXTRA_TEXT_LOAN_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_LOAN_NUMBER";
    public static final String EXTRA_TEXT_NIk = "com.modifica.creditcollector.EXTRA_TEXT_NIk";
    public static final String EXTRA_TEXT_AMOUT_PER_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_AMOUT_PER_TERMS";
    public static final String EXTRA_TEXT_F_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_F_TERMS";
    public static final String EXTRA_TEXT_A_TERMS = "com.modifica.creditcollector.EXTRA_TEXT_A_TERMS";
    public static final String EXTRA_TEXT_F_AMOUNT = "com.modifica.creditcollector.EXTRA_TEXT_F_AMOUNT";
    public static final String EXTRA_TEXT_INFO_CH = "com.modifica.creditcollector.EXTRA_TEXT_INFO_CH";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        textView_coll = (TextView) findViewById(R.id.textView_collaction01);
        textView_coll.setText("Root number : "+array_1[3]);
        textView_coll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Collection_list.this,Offices_list.class);
                startActivity(intent1);
                finish();
            }
        });

        LW = (ListView) findViewById(R.id.collect_list);
        BD_ref = FirebaseDatabase.getInstance().getReference().child("Loan").child(array_1[2]).child(array_1[3]);
        adapter = new MyAdapter(this,loan_num,nik_name,amount_per_term,available_terms,f_amount,full_terms);
        LW.setAdapter(adapter);


        BD_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                full_terms.clear();
                f_amount.clear();
                nik_name.clear();
                available_terms.clear();
                amount_per_term.clear();
                loan_num.clear();
                for (DataSnapshot collSnapshot : dataSnapshot.getChildren()){


                    Loan loan = collSnapshot.getValue(Loan.class);

                    if (loan.getAmount_per_term() != null ) {

                        f_amount.add(loan.getF_amount());
                        nik_name.add(loan.getNik_name());
                        full_terms.add(loan.getF_amount());
                        available_terms.add(loan.getAvailable_amount());
                        amount_per_term.add(loan.getAmount_per_term());
                        loan_num.add(loan.getLoan_num());

                        adapter.notifyDataSetChanged();

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        LW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loanNumber = loan_num.get(i);
                nikName = nik_name.get(i);
                amountPerTerms = amount_per_term.get(i);
                fullAmount = f_amount.get(i);
                fullTerms = full_terms.get(i);
                availableTerms = available_terms.get(i);

                openCollaction();

            }
        });





    }

    public void openCollaction(){
        Intent intent = new Intent(this, getCollection.class);
        intent.putExtra(EXTRA_TEXT_NIk,nikName);
        intent.putExtra(EXTRA_TEXT_LOAN_NUMBER,loanNumber);
        intent.putExtra(EXTRA_TEXT_AMOUT_PER_TERMS,amountPerTerms);
        intent.putExtra(EXTRA_TEXT_F_AMOUNT,fullAmount);
        intent.putExtra(EXTRA_TEXT_F_TERMS,fullTerms);
        intent.putExtra(EXTRA_TEXT_A_TERMS,availableTerms);
        intent.putExtra(EXTRA_TEXT_INFO_CH,inforChack);
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
                array_1[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}

