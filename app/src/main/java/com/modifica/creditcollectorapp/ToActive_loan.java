package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ToActive_loan extends AppCompatActivity {


    ListView listView;
   // private ArrayList<String> key_arrayList = new ArrayList<>();
    private DatabaseReference DataB_ref;

    private ArrayList <String> keyList = new ArrayList<>();
    private ArrayList <String> loanInfo_AMO = new ArrayList<>();
    private ArrayList <String> loanInfo_NIK = new ArrayList<>();
    private ArrayList <String> loanInfo_L_NUM = new ArrayList<>();
    private ArrayList <String> loanInfo_root = new ArrayList<>();

    private String[] Active_list = new String [4];
    String FileName = "CraditCollactor";
    String KEY;
    String loan;
    String LOAN_NUMBER,ROOT_NUMBER;

    MyAdapter_03 activeAdapter;



    public static final String EXTRA_TEXT_ACTIVE = "com.modifica.creditcollector.EXTRA_TEXT_ACTIVE";
    public static final String EXTRA_TEXT_LOAN_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_LOAN_NUMBER";
    public static final String EXTRA_TEXT_ROOT_NUMBER = "com.modifica.creditcollector.EXTRA_TEXT_ROOT_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_active_loan);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        listView = (ListView) findViewById(R.id.Toactive_loan);

        DataB_ref = FirebaseDatabase.getInstance().getReference().child("Loan").child(Active_list[2]).child("TO_activation");
       // final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,key_arrayList);
        activeAdapter = new MyAdapter_03(ToActive_loan.this,loanInfo_root,loanInfo_L_NUM,loanInfo_NIK,loanInfo_AMO);

        listView.setAdapter(activeAdapter);

        DataB_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    KEY = dataSnapshot.getKey();
                    keyList.add(KEY);
                }
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

        DataB_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                loanInfo_NIK.clear();
                loanInfo_L_NUM.clear();
                loanInfo_AMO.clear();
                loanInfo_root.clear();

                for (int d=0; d<keyList.size(); d++) {

                        for (DataSnapshot finishDataSnap : dataSnapshot.child(keyList.get(d)).getChildren()) {

                            Loan finishLoan = finishDataSnap.getValue(Loan.class);
                            assert finishLoan != null;
                            if (finishLoan.getAvailable_amount() != null) {
                                loanInfo_AMO.add(finishLoan.getF_amount());
                                loanInfo_L_NUM.add(finishLoan.getLoan_num());
                                loanInfo_NIK.add(finishLoan.getNik_name());
                                loanInfo_root.add(finishLoan.getRoot_name());
                            }
                        }
                    }

                activeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!loanInfo_root.get(i).equals("")){
                loan = loanInfo_root.get(i)+"/"+loanInfo_L_NUM.get(i);
                ROOT_NUMBER = loanInfo_root.get(i);
                LOAN_NUMBER = loanInfo_L_NUM.get(i);
                openActive_loan();

                }

            }
        });


    }

    private void openActive_loan() {
        Intent intent = new Intent(this,TO_Active_loanInfo.class);
        intent.putExtra(EXTRA_TEXT_ACTIVE,loan);
        intent.putExtra(EXTRA_TEXT_LOAN_NUMBER,LOAN_NUMBER);
        intent.putExtra(EXTRA_TEXT_ROOT_NUMBER,ROOT_NUMBER);
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
                Active_list[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }
}
