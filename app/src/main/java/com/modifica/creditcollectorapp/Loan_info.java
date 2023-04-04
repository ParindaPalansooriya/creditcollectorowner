package com.modifica.creditcollectorapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Loan_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_info);

        Button button1 = (Button) findViewById(R.id.button7);
        Button button2 = (Button) findViewById(R.id.button8);
        Button button3 = (Button) findViewById(R.id.button9);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Loan_info.this,TofinishLoan.class);
                startActivity(intent);
                finish();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Loan_info.this,ToActive_loan.class);
                startActivity(intent);
                finish();

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Loan_info.this,Loan_info_list.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
