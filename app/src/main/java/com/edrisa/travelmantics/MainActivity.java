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

import com.bumptech.glide.Glide;
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

    TravelDeals travel_deal;
    Intent intent;

    private static final int PICTURE_INTENT = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initFirebase();
    }

    private void initComponents(){
        iv_selected_image = findViewById(R.id.iv_selected_image);
        et_name = findViewById(R.id.et_name);
        et_price = findViewById(R.id.et_price);
        et_city = findViewById(R.id.et_city);
        btn_select_image = findViewById(R.id.btn_select_image);

        intent = getIntent();
        travel_deal = (TravelDeals) intent.getSerializableExtra("TRAVEL_DEAL");
        if(travel_deal == null){
            travel_deal = new TravelDeals();
        }
        loadImagetoImageView(travel_deal.getImage_url());
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_INTENT);
            }
        });

        et_name.setText(travel_deal.getName());
        et_price.setText(travel_deal.getPrice());
        et_city.setText(travel_deal.getCity());

    }

    private void initFirebase(){
        FirebaseUtil.openFbReference("travel_deals");
        m_firebase_database = FirebaseUtil.m_firebase_database;
        m_database_reference = FirebaseUtil.m_database_reference;
        m_storage = FirebaseStorage.getInstance();
        m_storage_ref = m_storage.getReference().child("deal_pictures");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_INTENT && resultCode == RESULT_OK ){
            Uri image_uri = data.getData();
            final StorageReference ref = m_storage_ref.child(image_uri.getLastPathSegment());
            ref.putFile(image_uri)
            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String[] url = new String[1];
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "2 Image : " + uri.toString());
                            travel_deal.setImage_url(uri.toString());
                        }
                    });
                }
            });
            loadImagetoImageView(image_uri.toString());
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
        Log.d("Image: " , ""+ url);
        if (url != null && url.isEmpty() == false){
            Glide.with(MainActivity.this).load(url).centerCrop().into(iv_selected_image);
        }

    }
    private void saveDealToFirebase(){
        travel_deal.setCity(et_city.getText().toString());
        travel_deal.setPrice(et_price.getText().toString());
        travel_deal.setName(et_name.getText().toString());

        if(travel_deal.getId() == null){
            m_database_reference.push().setValue(travel_deal);
        }else{
            m_database_reference.child(travel_deal.getId()).setValue(travel_deal);
        }
    }

    private void cleanViews(){
        et_city.setText("");
        et_price.setText("");
        et_name.setText("");
        iv_selected_image.setImageResource(android.R.color.transparent);
        et_city.requestFocus();
    }
}
