package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class TofinishLoan extends AppCompatActivity {

    private String[] finish_list = new String [4];
    String FileName = "CraditCollactor";

    ListView fininsh_listView;
    private String finish;

    DatabaseReference databaseReference;
    private ArrayList <String> keyList = new ArrayList<>();
    private ArrayList <String> loanInfo_AMO = new ArrayList<>();
    private ArrayList <String> loanInfo_NIK = new ArrayList<>();
    private ArrayList <String> loanInfo_L_NUM = new ArrayList<>();
    private ArrayList <String> loanInfo_root = new ArrayList<>();


    public static final String EXTRA_TEXT_FINISH = "com.modifica.creditcollector.EXTRA_TEXT_FINISH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tofinish_loan);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Loan").child(finish_list[2]).child("TO_finish");

        fininsh_listView = (ListView) findViewById(R.id.to_finish_list);

        final MyAdapter_03 finishAdapter = new MyAdapter_03(TofinishLoan.this,loanInfo_root,loanInfo_L_NUM,loanInfo_NIK,loanInfo_AMO);
        fininsh_listView.setAdapter(finishAdapter);



        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    String KEY = dataSnapshot.getKey();
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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    loanInfo_root.clear();
                    loanInfo_NIK.clear();
                    loanInfo_L_NUM.clear();
                    loanInfo_AMO.clear();

                   // for (DataSnapshot finishKey : dataSnapshot.getChildren()){

                   //     String key = finishKey.getKey();
                   //     keyList.add(key);

                    for (int j=0;j<keyList.size();j++) {

                        for (DataSnapshot finishDataSnap : dataSnapshot.child(keyList.get(j)).getChildren()) {

                            Loan finishLoan = finishDataSnap.getValue(Loan.class);
                            assert finishLoan != null;
                            if (finishLoan.getAvailable_amount() != null) {
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



        fininsh_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!loanInfo_root.get(i).equals("")){

                    Toast.makeText(TofinishLoan.this,loanInfo_root.get(i),Toast.LENGTH_SHORT).show();
                    finish = loanInfo_root.get(i) + "/" + loanInfo_L_NUM.get(i);
                    openFinish_loan();

                }

            }
        });


        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }


    public void openFinish_loan(){

        Intent intent = new Intent(this,Finish_info.class);
        intent.putExtra(EXTRA_TEXT_FINISH,finish);
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
                finish_list[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}
