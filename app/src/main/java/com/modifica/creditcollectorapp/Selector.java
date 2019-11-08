package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Selector extends AppCompatActivity {

    Button reg_b;
    Button mapInfo;
    Button crt_loan_b;
    Button Collect_b;
    Button loanInfo;
    Button collactionInfo;
    String active_key;
    ImageButton logout;
    ImageView massage_button;

    private String[] array = new String [4];
    ArrayList<String> arraylist_massage = new ArrayList<>();

    String FileName = "CraditCollactor";
    String FileName_massage = "massage_file";

    String massage_01;
    String massage_02;
    String main_massage;
    String active;

    DatabaseReference databaseRef_sel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();
        if (array[2].equals(array[3])){
            open_offese();
        }

        reg_b = (Button) findViewById(R.id.button_reg);
        crt_loan_b = (Button) findViewById(R.id.button_crt_ln);
        Collect_b = (Button) findViewById(R.id.button_coll);
        logout = (ImageButton) findViewById(R.id.imageButton_logout);
        collactionInfo = (Button) findViewById(R.id.button_other);
        massage_button = (ImageView) findViewById(R.id.imageView6);


        mapInfo = (Button) findViewById(R.id.button_map);
        loanInfo = (Button) findViewById(R.id.button_crt_info);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_button();
            }
        });

        databaseRef_sel = FirebaseDatabase.getInstance().getReference();

        boolean net = isOnline();

        if (net != true){Internet();}

        if (array[0].equals("YES")) {

            if (!array[2].equals(null)) {

                databaseRef_sel.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("Massage").child(array[2]).exists()) {
                             massage_01 = dataSnapshot.child("Massage").child(array[2]).getValue(String.class);
                        }
                        if (dataSnapshot.child("Massage").child("Main").exists()) {
                             massage_02 = dataSnapshot.child("Massage").child("Main").getValue(String.class);
                        }

                        readFile_massege();

                        if (!massage_01.isEmpty() || !massage_02.isEmpty()) {

                            if (!massage_01.equals(arraylist_massage.get(0)) || !massage_02.equals(arraylist_massage.get(1))){
                                massage_button.setImageResource(R.mipmap.massage02);
                            }
                            if (!massage_02.equals(arraylist_massage.get(1)) && !massage_01.equals(arraylist_massage.get(0))){
                                main_massage = "**"+massage_02+"\n"+"*"+massage_01;
                            }else if (!massage_02.equals(arraylist_massage.get(1)) && massage_01.equals(arraylist_massage.get(0))){
                                main_massage = massage_02;
                            }else {
                                main_massage = massage_01;
                            }

                        }


                        massage_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Massage_dialog();
                                massage_button.setImageResource(R.mipmap.massage01);
                            }
                        });

                        active_key = dataSnapshot.child("Activation").child(array[2]).getValue(String.class);

                        if (active_key.equals("Active")) {


                            reg_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_reg();
                                }
                            });

                            crt_loan_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_loan();
                                }
                            });

                            Collect_b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_coll();
                                }
                            });

                            mapInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_Map();
                                }
                            });

                            loanInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   open_Loan_info();
                                }
                            });

                            collactionInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    open_collaction();
                                }
                            });

                        } else {
                            Active_dialog();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {
                openLoging();
            }
        }else {
            NotAccess_dialog();
        }


    }

    private void open_offese() {

        Intent intent = new Intent(this,Offices_list.class);
        startActivity(intent);

    }

    private void open_reg() {

        Intent intent = new Intent(this,Registation.class);
        startActivity(intent);

    }

    private void open_loan() {

        Intent intent = new Intent(this,Finder.class);
        startActivity(intent);

    }

    private void open_Map() {

        Intent intent = new Intent(this,Collaction_map.class);
        startActivity(intent);

    }

    private void open_collaction() {

        Intent intent = new Intent(this,Collaction_info.class);
        startActivity(intent);

    }

    private void open_Loan_info() {

        Intent intent = new Intent(this,Loan_info.class);
        startActivity(intent);

    }

    private void open_coll() {

        Intent intent = new Intent(this,Collection_list.class);
        startActivity(intent);

    }

    public void readFile() {

        try{
            FileInputStream input = openFileInput(FileName);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
            int i=0;
            while((line=bufferedReader.readLine()) !=null){
                array[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    public void readFile_massege() {

        try {

            File file = new File(this.getFilesDir(), FileName_massage);
            boolean kt = file.exists();

                if (!kt) {
                saveFile_massage(null,null,null);
                }
            FileInputStream input = openFileInput(FileName_massage);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
          //  int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                   // array_massage[i] = line;
                    arraylist_massage.add(line);
                   // i++;
                }
            input.close();
            inputread.close();

            }catch(java.io.IOException e){
                e.printStackTrace();
            }
    }

    public void saveFile(String A,String B,String C,String D)  {

        try{
            String NAME = A+"\n"+B+"\n"+C+"\n"+D;
            FileOutputStream FOS = openFileOutput(FileName, Context.MODE_PRIVATE);
            FOS.write(NAME.getBytes());
            FOS.close();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    public void saveFile_massage(String A,String B,String C)  {

        try{
            String NAME = A+"\n"+B+"\n"+C;
            FileOutputStream FOS = openFileOutput(FileName_massage, Context.MODE_PRIVATE);
            FOS.write(NAME.getBytes());
            FOS.close();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    public void Massage_dialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Massage").setMessage(main_massage)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        saveFile_massage(massage_01,massage_02,null);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void Active_dialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account was Blocked").setMessage("Contact your Owner to Active Your Account")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void logout_button(){

        saveFile(null,null,null,null);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();


    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void Internet (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Check your Internet Connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void NotAccess_dialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NOT ACCESS").setMessage("You have not Access for this. Please use your password and user name")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        logout_button();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void NotNet (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet").setMessage("You have not internet connection. Please check you internet connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void openLoging(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
