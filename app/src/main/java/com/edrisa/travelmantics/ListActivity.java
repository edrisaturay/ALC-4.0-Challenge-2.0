package com.edrisa.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.edrisa.travelmantics.Utils.FirebaseUtil;
import com.edrisa.travelmantics.models.TravelDeals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getName();


    private ArrayList<TravelDeals> deals;

    private TextView tv_city;

    private FirebaseDatabase m_firebase_database;
    private DatabaseReference m_database_reference;
    private ChildEventListener m_child_event_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initComponents();
        initFirebase();

    }

    private void initComponents(){
        tv_city = findViewById(R.id.tv_city);

    }

    private void initFirebase(){
//        m_firebase_database = FirebaseDatabase.getInstance();
//        m_database_reference = m_firebase_database.getReference().child("travel_deals");
        FirebaseUtil.openFbReference("travel_deals");
        m_firebase_database = FirebaseUtil.m_firebase_database;
        m_database_reference = FirebaseUtil.m_database_reference;
        m_child_event_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeals travel_deal = dataSnapshot.getValue(TravelDeals.class);
                Log.d(TAG, travel_deal.getCity());
                tv_city.setText(travel_deal.getCity());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        m_database_reference.addChildEventListener(m_child_event_listener);
    }
}
