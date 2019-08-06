package com.edrisa.travelmantics.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edrisa.travelmantics.MainActivity;
import com.edrisa.travelmantics.R;
import com.edrisa.travelmantics.Utils.FirebaseUtil;
import com.edrisa.travelmantics.models.TravelDeals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder>{

    private ArrayList<TravelDeals> deals;
    private FirebaseDatabase m_firebase_database;
    private DatabaseReference m_database_reference;
    private ChildEventListener m_child_event_listener;

    public DealsAdapter(){
        FirebaseUtil.openFbReference("travel_deals");
        m_firebase_database = FirebaseUtil.m_firebase_database;
        m_database_reference = FirebaseUtil.m_database_reference;
        deals = FirebaseUtil.m_deals;
        m_child_event_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeals travel_deal = dataSnapshot.getValue(TravelDeals.class);
                Log.d("Deal:", travel_deal.getCity());
                travel_deal.setId(dataSnapshot.getKey());
                deals.add(travel_deal);
                notifyItemInserted(deals.size()-1);
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
    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View item_view = LayoutInflater.from(context)
                .inflate(R.layout.single_deal_layout, parent, false);
        return new DealViewHolder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeals travel_deal = deals.get(position);
        holder.bind(travel_deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_city, tv_name, tv_price;
        ImageView iv_deal_image;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_deal_image = itemView.findViewById(R.id.iv_deal_image);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeals travel_deal){
            tv_city.setText(travel_deal.getCity());
            tv_name.setText(travel_deal.getName());
            tv_price.setText(travel_deal.getPrice());
            loadImagetoImageView(travel_deal.getImage_url());
        }

        private void loadImagetoImageView(String url){
            Log.d("Image: " , ""+ url);
            if (url != null && url.isEmpty() == false){
                Glide.with(iv_deal_image.getContext()).load(url).centerCrop().into(iv_deal_image);
            }

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TravelDeals selected_deal = deals.get(position);
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("TRAVEL_DEAL", selected_deal);
            view.getContext().startActivity(intent);
        }
    }


}
