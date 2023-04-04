package com.modifica.creditcollectorapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Collaction_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaction_info);

        Button button1 = (Button) findViewById(R.id.button_coll_info);
        Button button2 = (Button) findViewById(R.id.button_coll_aval);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collaction_info.this,TodayTotal_list.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collaction_info.this,Online_workers_list.class);
                startActivity(intent);
            }
        });


    }
}
