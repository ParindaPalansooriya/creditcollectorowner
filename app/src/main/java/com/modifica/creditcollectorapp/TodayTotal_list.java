package com.modifica.creditcollectorapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
import java.util.Calendar;

public class TodayTotal_list extends AppCompatActivity {

    int year,month,date;
    String ful_date;
    String ful_text;
    String date_text;
    String DATE;

    double pay,due,pay_sum=0;

    MyAdapter_02 adapter;

    private DatabaseReference dueData;

    private TextView root_num;
    private ListView dueList;
    private TextView totalPay;

    private String[] array_off = new String [4];
    String FileName = "CraditCollactor";

    public static final String EXTRA_TEXT_DUE_LN = "com.modifica.creditcollector.EXTRA_TEXT_DUE_LN";
    public static final String EXTRA_TEXT_DUE_DATE = "com.modifica.creditcollector.EXTRA_TEXT_DUE_DATE";
    public static final String EXTRA_TEXT_DUE_KEY = "com.modifica.creditcollector.EXTRA_TEXT_DUE_KEY";

    private ArrayList <String> nikName = new ArrayList<>();
    private ArrayList <String> loanNumber = new ArrayList<>();
    private ArrayList <String> dueAmmount = new ArrayList<>();
    private ArrayList <String> payedAmmount = new ArrayList<>();
    private ArrayList <String> dueKey = new ArrayList<>();

    private String dueInfo_LN;
    private String dueInfo_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_total_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        root_num = (TextView) findViewById(R.id.textView_collact_root);
        dueList = (ListView) findViewById(R.id.due_list);
        totalPay = (TextView) findViewById(R.id.textView_collact_pay);

        readFile();

        Intent intent = getIntent();
        DATE = intent.getStringExtra(Get_date.EXTRA_TEXT_DATE);

        //++++++++++++++ DATE ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);

        ful_date = String.valueOf(year)+"|"+String.valueOf(month)+"|"+String.valueOf(date);



        if (DATE != null){
            date_text = DATE;
        }else {
            date_text = ful_date;
        }
        ful_text = array_off[3]+"\t\t"+date_text;
        root_num.setText(ful_text);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        root_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_date();
            }
        });


        adapter = new MyAdapter_02(TodayTotal_list.this,nikName,loanNumber,dueAmmount,payedAmmount);
        dueList.setAdapter(adapter);

        dueData = FirebaseDatabase.getInstance().getReference().child("Collaction").child(array_off[2]).child(date_text).child(array_off[3]);


        dueData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nikName.clear();
                loanNumber.clear();
                payedAmmount.clear();
                dueAmmount.clear();

                    for (DataSnapshot dueSnapshot : dataSnapshot.getChildren()){

                    Collaction list = dueSnapshot.getValue(Collaction.class);
                    dueKey.add(dueSnapshot.getKey());

                    //assert list != null;
                    nikName.add(list.getNik_name());
                    loanNumber.add(list.getLoan_num());
                    payedAmmount.add(list.getPayment());
                    dueAmmount.add(list.getDue());


                        pay = Double.parseDouble(list.getPayment());

                        pay_sum += pay;

                    adapter.notifyDataSetChanged();

                }

                totalPay.setText(String.valueOf(pay_sum));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dueInfo_LN = loanNumber.get(i);
                dueInfo_key = dueKey.get(i);
                open_dueInfo();
            }
        });

       // setTotal();



    }

    public void readFile() {

        try{

            FileInputStream input = openFileInput(FileName);
            InputStreamReader inputread = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputread);
            String line = null;
            int i=0;
            while((line=bufferedReader.readLine()) !=null){
                array_off[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    private void open_date() {

        Intent intent = new Intent(this,Get_date.class);
        startActivity(intent);
        finish();

    }

    private void open_dueInfo() {

        Intent intent = new Intent(this,Due_infor.class);
        intent.putExtra(EXTRA_TEXT_DUE_LN,dueInfo_LN);
        intent.putExtra(EXTRA_TEXT_DUE_DATE,date_text);
        intent.putExtra(EXTRA_TEXT_DUE_KEY,dueInfo_key);
        startActivity(intent);

    }

}
