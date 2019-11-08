package com.modifica.creditcollectorapp;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Splash extends AppCompatActivity {

    ImageView imageView02;
    ImageView imageView03;

    Intent intent;

    private String[] info_e = {null,null,null,null};

    String FileName = "CraditCollactor";


    ArrayList<String> finish_arraylist = new ArrayList<>();
    ArrayList <String> finish_key = new ArrayList<>();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        if (info_e[1].equals("KEEP")){
            intent = new Intent(Splash.this,Selector.class);
        }else {
            intent = new Intent(Splash.this,MainActivity.class);
        }




       databaseReference = FirebaseDatabase.getInstance().getReference();

        if (info_e[2] !=null) {

            databaseReference.child("Registation").child(info_e[2]).child("Offices").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.exists()) {

                        String key = dataSnapshot.getKey();
                        finish_key.add(key);
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

            databaseReference.child("Loan").child(info_e[2]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (int k = 0; k < finish_key.size(); k++) {


                            if (dataSnapshot.child(finish_key.get(k)).exists()) {

                                for (DataSnapshot finishSnapshot : dataSnapshot.child(finish_key.get(k)).getChildren()) {

                                    Loan finishLoan = finishSnapshot.getValue(Loan.class);

                                    if (finishLoan.getLoan_num() != null) {

                                        finish_arraylist.add(finishLoan.getAvailable_amount());

                                        if (Double.parseDouble(finishLoan.getAvailable_amount().trim()) <= 0) {

                                            databaseReference.child("Loan").child(info_e[2]).child("TO_finish").child(finish_key.get(k)).child(finishLoan.getLoan_num()).setValue(finishLoan);
                                            databaseReference.child("Loan").child(info_e[2]).child(finish_key.get(k)).child(finishLoan.getLoan_num()).removeValue();


                                        }

                                    }

                                }

                            }

                        }

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        imageView02 = (ImageView) findViewById(R.id.logo);
        imageView03 = (ImageView) findViewById(R.id.pic_01);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splam);

        imageView03.startAnimation(animation);
        imageView02.startAnimation(animation);



        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();

    }


    public void readFile() {

        try{
            File file = new File(this.getFilesDir(), FileName);
            boolean kt = file.exists();
            if (kt){
                FileInputStream input = openFileInput(FileName);
                InputStreamReader inputread = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputread);
                String line = null;
                int i=0;
                while((line=bufferedReader.readLine()) !=null){
                    info_e[i] = line;
                    i++;
                }
                input.close();
                inputread.close();
            }else {
                info_e[1]= "no";
            }

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }
}
