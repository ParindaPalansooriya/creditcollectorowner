package com.modifica.creditcollectorapp;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class Collaction_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat,lng;
    String FileName = "CraditCollactor";

    private String[] array_map = new String [4];

    private int year,date,month;
    private String ful_date,date_text,ful_text,DATE;
    private ArrayList<Double> latKey = new ArrayList<>();
    private ArrayList<Double> lngKey = new ArrayList<>();
    private ArrayList <String> LNKey = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaction_map);

        readFile();

        TextView textView = (TextView) findViewById(R.id.textView_map);

        Intent intent = getIntent();
        DATE = intent.getStringExtra(Date_for_map.EXTRA_TEXT_DATE_MAP);

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
        ful_text = array_map[3]+"\t\t"+date_text;
        textView.setText(ful_text);

        //lat = new Double[]{6.491036386398971, 6.491136386398971, 6.490936386398971};
        //lng = new Double[]{82.67825892008841,80.67825892008841,81.67825892008841};
        //note = new String[]{"text1","test2","test3"};

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatemap();
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        readFile();
        Toast.makeText(this,array_map[0]+array_map[1]+array_map[2]+array_map[3], Toast.LENGTH_LONG).show();
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Collaction").child(array_map[2]).child(date_text).child(array_map[3]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if (dataSnapshot.exists()){

                    for (DataSnapshot mapSnapshot : dataSnapshot.getChildren()){

                        Collaction mapColl = mapSnapshot.getValue(Collaction.class);

                        Double lat = Double.parseDouble(mapColl.getLatT());
                        Double lng = Double.parseDouble(mapColl.getLogT());

                        latKey.add(lat);
                        lngKey.add(lng);
                        LNKey.add(mapColl.getLoan_num());


                    }

                } else {
                    Toast.makeText(Collaction_map.this,"That root have not Collation Untill Now",Toast.LENGTH_SHORT).show();
                }

                int k;
                for (k=0; k<latKey.size();k++) {
                    // Add a marker in Sydney
                    LatLng sydney = new LatLng(latKey.get(k), lngKey.get(k));
                    mMap.addMarker(new MarkerOptions().position(sydney).title(LNKey.get(k)));
                   lat = latKey.get(k);
                   lng = lngKey.get(k);
                }
                if (lat != null && lng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                array_map[i] = line;
                i++;
            }
            input.close();
            inputread.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    private void openDatemap(){
        Intent intent = new Intent(this,Date_for_map.class);
        startActivity(intent);
        finish();
    }

}
