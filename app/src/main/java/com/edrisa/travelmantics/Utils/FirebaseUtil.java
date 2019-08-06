package com.edrisa.travelmantics.Utils;

import com.edrisa.travelmantics.models.TravelDeals;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase m_firebase_database;
    public static DatabaseReference m_database_reference;
    private static FirebaseUtil firebase_util;
    public static ArrayList<TravelDeals> m_deals;

    private  FirebaseUtil(){};

    public static void openFbReference(String ref){
        if(firebase_util == null){
            firebase_util = new FirebaseUtil();
            m_firebase_database = FirebaseDatabase.getInstance();
        }
        m_deals = new ArrayList<TravelDeals>();
        m_database_reference = m_firebase_database.getReference().child(ref);
    }
}
