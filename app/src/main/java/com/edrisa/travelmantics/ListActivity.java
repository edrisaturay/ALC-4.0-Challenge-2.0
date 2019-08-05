package com.edrisa.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.edrisa.travelmantics.Adapters.DealsAdapter;
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

    private RecyclerView rv_deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initComponents();
        initRecyclerView();

    }

    private void initComponents(){
        tv_city = findViewById(R.id.tv_city);

    }

    private void initRecyclerView(){
        rv_deals = findViewById(R.id.rv_deals);
        final DealsAdapter deal_adapter = new DealsAdapter();
        rv_deals.setAdapter(deal_adapter);
        LinearLayoutManager deals_layout_manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_deals.setLayoutManager(deals_layout_manager);
    }
}
