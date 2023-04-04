package com.modifica.creditcollectorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private DatabaseReference databaseRef_log;
    private EditText u_name_txt;
    private EditText pass_word_txt;
    private String u_name;
    private String active_key = null;
    private String pass_word = null;
    private String owner_code = null;
    private String root_num = null;
    private String code = null;
    private String db_pass_word = null;

    private String[] info = new String [4];

    String FileName = "CraditCollactor";
    String KEEP = "DONOT";

    private CheckBox chack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        chack = (CheckBox) findViewById(R.id.checkBox);

        databaseRef_log = FirebaseDatabase.getInstance().getReference();

        u_name_txt = (EditText) findViewById(R.id.editText);
        pass_word_txt = (EditText) findViewById(R.id.editText7);


        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chack.isChecked()){
                    KEEP = "KEEP";
                }

                u_name = u_name_txt.getText().toString().trim();
                pass_word = pass_word_txt.getText().toString().trim();
                if (!u_name.isEmpty()&& !pass_word.isEmpty()){

                boolean net = isOnline();

                if (net == true) {


                        databaseRef_log.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Registation").child(u_name).exists()){
                                    root_num  = dataSnapshot.child("Registation").child(u_name).child("rootnum").getValue(String.class);
                                    db_pass_word = dataSnapshot.child("Registation").child(u_name).child("password").getValue().toString();
                                    owner_code = dataSnapshot.child("Registation").child(u_name).child("ownercode").getValue(String.class);
                                    active_key = dataSnapshot.child("Activation").child(owner_code).getValue(String.class);

                                    if (dataSnapshot.child("Registation").child(u_name).child("code").exists()){
                                        code  = dataSnapshot.child("Registation").child(u_name).child("code").getValue(String.class);
                                    }else {
                                        u_name_txt.setText(null);
                                        pass_word_txt.setText(null);
                                        NotAccess_dialog();
                                    }

                                }else {
                                    u_name_txt.setText(null);
                                    pass_word_txt.setText(null);
                                    Toast.makeText(MainActivity.this, "worng u name!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {



                            }
                        });


                        if (owner_code != null) {


                             if (active_key.equals("Active")) {
                             if (db_pass_word.equals(pass_word)) {
                                openactivity_selecter();
                                 saveFile(code,KEEP,owner_code,root_num);
                            } else {
                                u_name_txt.setText(null);
                                pass_word_txt.setText(null);
                                Toast.makeText(MainActivity.this, "worng password!", Toast.LENGTH_LONG).show();
                              }
                            } else {
                              u_name_txt.setText(null);
                               pass_word_txt.setText(null);
                                Active_dialog();
                            }
                             }else {
                            Toast.makeText(MainActivity.this, "Please Click again", Toast.LENGTH_LONG).show();
                        }

                       } else {
                        Internet();
                    }
                }
            }
        });
    }

    public void openactivity_selecter(){
        Intent intent = new Intent(this, Selector.class);
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

    public void NotAccess_dialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NOT ACCESS").setMessage("You have not Access for this. Please use your password and user name")
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

    public void saveFile(String A,String B,String C,String D)  {

        try{
            String NAME = A+"\n"+B+"\n"+C+"\n"+D;
            FileOutputStream FOS = openFileOutput(FileName,Context.MODE_PRIVATE);
            FOS.write(NAME.getBytes());
            FOS.close();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
