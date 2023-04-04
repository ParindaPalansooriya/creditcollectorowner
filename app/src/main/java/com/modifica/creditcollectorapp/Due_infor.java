package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class Due_infor extends AppCompatActivity {

    private DatabaseReference due_ref;

    private String[] array_due = new String [4];
    String FileName = "CraditCollactor";
    public static final String EXTRA_TEXT_MAP_INFO_LOG = "com.modifica.creditcollector.EXTRA_TEXT_MAP_INFO_LOG";
    public static final String EXTRA_TEXT_MAP_INFO_LAT = "com.modifica.creditcollector.EXTRA_TEXT_MAP_INFO_LAT";
    public static final String EXTRA_TEXT_MAP_INFO_LN = "com.modifica.creditcollector.EXTRA_TEXT_MAP_INFO_LN";

    double latitude;
    double longitude;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_infor);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        readFile();

        final Intent intent = getIntent();
        final String date = intent.getStringExtra(TodayTotal_list.EXTRA_TEXT_DUE_DATE);
        final String key = intent.getStringExtra(TodayTotal_list.EXTRA_TEXT_DUE_KEY);
        final String loanNumber = intent.getStringExtra(TodayTotal_list.EXTRA_TEXT_DUE_LN);

        final TextView due_nikName = (TextView) findViewById(R.id.dueNikname);
        final TextView due_loanNumber = (TextView) findViewById(R.id.dueLoannum);
        final TextView due_payment = (TextView) findViewById(R.id.duePayment);
        final TextView due_dueAmmount = (TextView) findViewById(R.id.dueAmmount);
        final TextView due_terms = (TextView) findViewById(R.id.dueTerms);
        final TextView due_date = (TextView) findViewById(R.id.dueDate);

        Button mapbutton = (Button) findViewById(R.id.button_coll_MV);


        due_ref = FirebaseDatabase.getInstance().getReference().child("Collaction").child(array_due[2]).child(date).child(array_due[3]);

        due_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dueSnapshot : dataSnapshot.getChildren()){
                    Collaction list = dueSnapshot.getValue(Collaction.class);

                    if (list.getLoan_num().equals(loanNumber)){

                        due_date.setText(list.getDate());
                        due_dueAmmount.setText(list.getDue());
                        due_loanNumber.setText(list.getLoan_num());
                        due_nikName.setText(list.getNik_name());
                        due_payment.setText(list.getPayment());
                        due_terms.setText(list.getPaid_terms());
                        tag = list.getLoan_num();
                        latitude = Double.parseDouble(list.getLatT());
                        longitude = Double.parseDouble(list.getLogT());

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Due_infor.this,oneMap.class);
                intent1.putExtra(EXTRA_TEXT_MAP_INFO_LAT,String.valueOf(latitude));
                intent1.putExtra(EXTRA_TEXT_MAP_INFO_LOG,String.valueOf(longitude));
                intent1.putExtra(EXTRA_TEXT_MAP_INFO_LN,String.valueOf(tag));
                startActivity(intent1);
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
                array_due[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

}
