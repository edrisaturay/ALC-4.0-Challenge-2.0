package com.edrisa.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edrisa.travelmantics.Utils.FirebaseUtil;
import com.edrisa.travelmantics.models.TravelDeals;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button btn_select_image;
    private EditText et_city, et_price, et_name;
    private ImageView iv_selected_image;

    private FirebaseStorage m_storage;
    private StorageReference m_storage_ref;
    private FirebaseDatabase m_firebase_database;
    private DatabaseReference m_database_reference;

    private static final int PICTURE_INTENT = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initFirebase();
    }

    private void initComponents(){
        btn_select_image = findViewById(R.id.btn_select_image);
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_INTENT);
            }
        });
        iv_selected_image = findViewById(R.id.iv_selected_image);
        et_name = findViewById(R.id.et_name);
        et_price = findViewById(R.id.et_price);
        et_city = findViewById(R.id.et_city);
    }

    private void initFirebase(){
//        m_firebase_database = FirebaseDatabase.getInstance();
//        m_database_reference = m_firebase_database.getReference().child("travel_deals");

        FirebaseUtil.openFbReference("travel_deals");
        m_firebase_database = FirebaseUtil.m_firebase_database;
        m_database_reference = FirebaseUtil.m_database_reference;

        m_storage = FirebaseStorage.getInstance();
        m_storage_ref = m_storage.getReference().child("deal_pictures");
    }
    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Closing app. please try again.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void connectStorage(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_INTENT && resultCode == RESULT_OK ){
            Uri image_uri = data.getData();
            StorageReference ref = m_storage_ref.child(image_uri.getLastPathSegment());
            ref.putFile(image_uri)
            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getUploadSessionUri().toString();
                    Log.d(TAG, "Image Url: " + url);
                    loadImagetoImageView(url);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_save:
                saveDealToFirebase();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                cleanViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadImagetoImageView(String url){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        Picasso.with(MainActivity.this)
                .load(url)
                .resize(width, width*2/3)
                .centerCrop()
                .into(iv_selected_image);
    }

    private void saveDealToFirebase(){
        String deal_city = et_city.getText().toString();
        String deal_price = et_price.getText().toString();
        String deal_name = et_name.getText().toString();
        TravelDeals travel_deal = new TravelDeals(deal_city, deal_price, deal_name);
        m_database_reference.push().setValue(travel_deal);
    }

    private void cleanViews(){
        et_city.setText("");
        et_price.setText("");
        et_name.setText("");
        et_city.requestFocus();
    }
}
