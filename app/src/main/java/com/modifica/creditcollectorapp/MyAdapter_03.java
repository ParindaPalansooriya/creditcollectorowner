package com.modifica.creditcollectorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter_03 extends ArrayAdapter<String> {

    ArrayList<String> T1Array;
    ArrayList<String> T2Array;
    ArrayList<String> T3Array;
    ArrayList<String> T4Array;

    public MyAdapter_03(Context context, ArrayList<String> T11, ArrayList<String> T21, ArrayList<String> T31, ArrayList<String> T41){
        super((Context) context,R.layout.finish_list,R.id.textView_LN,T11);
        this.T1Array=T11;
        this.T2Array=T21;
        this.T3Array=T31;
        this.T4Array=T41;
    }

    @NonNull
    @Override
    public View getView(int position, View convertViwe, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.finish_list,parent,false);

        TextView myT1 = (TextView) row.findViewById(R.id.textView_NN);
        TextView myT2 = (TextView) row.findViewById(R.id.textView_LN);
        TextView myT3 = (TextView) row.findViewById(R.id.textView_due);
        TextView myT4 = (TextView) row.findViewById(R.id.textView_pay);


            myT1.setText(T1Array.get(position));
            myT2.setText(T2Array.get(position));
            myT3.setText(T3Array.get(position));
            myT4.setText("Rs " + T4Array.get(position));


        return row;
    }

}
