package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class Loan_info_list extends AppCompatActivity {


    private String[] finish_list = new String [4];
    String FileName = "CraditCollactor";

    ListView loanInfo_listView;
    private String finish;

    DatabaseReference databaseReference;
    private ArrayList<String> keyList = new ArrayList<>();
    private ArrayList <String> loanInfo_AMO = new ArrayList<>();
    private ArrayList <String> loanInfo_NIK = new ArrayList<>();
    private ArrayList <String> loanInfo_L_NUM = new ArrayList<>();
    private ArrayList <String> loanInfo_root = new ArrayList<>();


    public static final String EXTRA_TEXT_LN_INFO = "com.modifica.creditcollector.EXTRA_TEXT_LN_INFO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_info_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(finish_list[2]);

        loanInfo_listView = (ListView) findViewById(R.id.LN_Info_list);

        final MyAdapter_03 finishAdapter = new MyAdapter_03(Loan_info_list.this,loanInfo_root,loanInfo_L_NUM,loanInfo_NIK,loanInfo_AMO);
        loanInfo_listView.setAdapter(finishAdapter);



        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    String KEY = dataSnapshot.getKey();
                    if (!KEY.equals("Histry")&&!KEY.equals("Reject_Loan")&&!KEY.equals("TO_activation")) {
                        keyList.add(KEY);
                    }
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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    loanInfo_root.clear();
                    loanInfo_NIK.clear();
                    loanInfo_L_NUM.clear();
                    loanInfo_AMO.clear();

                    for (int j=0;j<keyList.size();j++) {

                        for (DataSnapshot finishDataSnap : dataSnapshot.child(keyList.get(j)).getChildren()) {

                            Loan finishLoan = finishDataSnap.getValue(Loan.class);

                            if (finishLoan.getLoan_num() != null ) {

                                loanInfo_AMO.add(finishLoan.getF_amount());
                                loanInfo_L_NUM.add(finishLoan.getLoan_num());
                                loanInfo_NIK.add(finishLoan.getNik_name());
                                loanInfo_root.add(finishLoan.getRoot_name());

                                finishAdapter.notifyDataSetChanged();

                            }

                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        loanInfo_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!loanInfo_root.get(i).equals("")){
                  //  Toast.makeText(Loan_info_list.this,loanInfo_root.get(i),Toast.LENGTH_SHORT).show();
                    finish = loanInfo_root.get(i) + "/" + loanInfo_L_NUM.get(i);
                    openLaon_fullInfo();

                }

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
                finish_list[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    public void openLaon_fullInfo(){

        Intent intent = new Intent(this,Loan_ful_info.class);
        intent.putExtra(EXTRA_TEXT_LN_INFO,finish);
        startActivity(intent);
    }


}
