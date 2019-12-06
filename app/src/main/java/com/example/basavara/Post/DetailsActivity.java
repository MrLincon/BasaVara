package com.example.basavara.Post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basavara.HomeActivity;
import com.example.basavara.R;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView name,time,division,city,location,vara,address,details,contact;
    ImageView copy;
    CardView call;

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        name = findViewById(R.id.details_name);
        time = findViewById(R.id.details_time);
        division = findViewById(R.id.details_division);
        city = findViewById(R.id.details_city);
        location = findViewById(R.id.location);
        vara = findViewById(R.id.vara);
        address = findViewById(R.id.address);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);

        copy = findViewById(R.id.copy);
        call = findViewById(R.id.call);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Details");
//        toolbarTitle.setText("BasaVara");

        final Intent intent = getIntent();

        String Name = intent.getStringExtra(HomeActivity.EXTRA_NAME);
        String Time = intent.getStringExtra(HomeActivity.EXTRA_TIME);
        String Division = intent.getStringExtra(HomeActivity.EXTRA_DIVISION);
        String City = intent.getStringExtra(HomeActivity.EXTRA_CITY);
        String Location = intent.getStringExtra(HomeActivity.EXTRA_LOCATION);
        String Vara = intent.getStringExtra(HomeActivity.EXTRA_VARA);
        String Address = intent.getStringExtra(HomeActivity.EXTRA_ADDRESS);
        String Details = intent.getStringExtra(HomeActivity.EXTRA_DETAILS);
        final String Contact = intent.getStringExtra(HomeActivity.EXTRA_CONTACT);

        name.setText(Name);
        time.setText(Time);
        division.setText(Division);
        city.setText(City);
        location.setText(Location);
        vara.setText(Vara);
        address.setText(Address);
        details.setText(Details);
        contact.setText(Contact);

        clipboardManager = (ClipboardManager) DetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);

        copy.setVisibility(View.GONE);

//        copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                clipData = ClipData.newPlainText("contact",Contact);
//                clipboardManager.setPrimaryClip(clipData);
//
//                Toast.makeText(DetailsActivity.this,"copied: "+Contact, Toast.LENGTH_SHORT).show();
//            }
//        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+Contact));

                if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(DetailsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (ActivityNotFoundException activityException) {
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }

            }
        });
    }
}
