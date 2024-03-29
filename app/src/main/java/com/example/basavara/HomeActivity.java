package com.example.basavara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.Adapters.Basa;
import com.example.basavara.Adapters.BasaAdapter;
import com.example.basavara.Authentication.LoginActivity;
import com.example.basavara.Post.DetailsActivity;
import com.example.basavara.Post.PostActivity;
import com.example.basavara.Profile.ProfileActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Toolbar toolbar;
    private TextView toolbarTitle;
    CardView post;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    LinearLayout chng_division;

    TextView division,location;

    public String selectDivision;
    public String selectLocation;

    public static final String EXTRA_NAME = "com.example.firebaseprofile.EXTRA_NAME";
    public static final String EXTRA_TIME = "com.example.firebaseprofile.EXTRA_TIME";
    public static final String EXTRA_DIVISION = "com.example.firebaseprofile.EXTRA_DIVISION";
    public static final String EXTRA_LOCATION = "com.example.firebaseprofile.EXTRA_LOCATION";
    public static final String EXTRA_AREA = "com.example.firebaseprofile.EXTRA_AREA";
    public static final String EXTRA_VARA = "com.example.firebaseprofile.EXTRA_VARA";
    public static final String EXTRA_ADDRESS = "com.example.firebaseprofile.EXTRA_ADDRESS";
    public static final String EXTRA_DETAILS = "com.example.firebaseprofile.EXTRA_DETAILS";
    public static final String EXTRA_CONTACT = "com.example.firebaseprofile.EXTRA_CONTACT";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference basa = db.collection("rajshahi");

    private BasaAdapter adapter;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Division = "division";
    public static final String Location = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: inside onCreate");

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        post = findViewById(R.id.post);

        recyclerView = findViewById(R.id.recyclerview);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.bottomPadding);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        recyclerView.addItemDecoration(new BasaRecyclerDecoration(topPadding, sidePadding, bottomPadding));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("BasaVara");

        loadData();

        //Post Button

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        //................

        //Navigation drawer layout

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view_left);

        NavigationItems();

        //........................

        //Change division options

        chng_division = findViewById(R.id.change_division_layout);
        chng_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeDivision = new Intent(HomeActivity.this,SelectDivisionActivity.class);
                startActivity(changeDivision);
            }
        });

        //................


    }

    //Recyclerviewer for firebase items

    private void FirebaseLoadData() {

        Query query = basa.whereEqualTo("division",selectDivision)
                .whereEqualTo("location",selectLocation)
                .whereEqualTo("location",selectLocation)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Basa> options = new FirestoreRecyclerOptions.Builder<Basa>()
                .setQuery(query, Basa.class)
                .build();

        adapter = new BasaAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new  BasaAdapter.OnItemClickListener()  {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                Basa basa = documentSnapshot.toObject(Basa.class);
                String id = documentSnapshot.getId();

                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);

                String name = basa.getName();
                SimpleDateFormat spf = new SimpleDateFormat("MMM dd, YYYY");
                String date = spf.format(basa.getTimestamp());
                String division = basa.getDivision();
                String location = basa.getLocation();
                String area = basa.getArea();
                String vara = basa.getVara();
                String details = basa.getDetails();
                String address = basa.getAddress();
                String contact = basa.getContact();

                intent.putExtra(EXTRA_NAME,name);
                intent.putExtra(EXTRA_TIME,date);
                intent.putExtra(EXTRA_DIVISION,division);
                intent.putExtra(EXTRA_LOCATION,location);
                intent.putExtra(EXTRA_AREA,area);
                intent.putExtra(EXTRA_VARA,vara);
                intent.putExtra(EXTRA_ADDRESS,address);
                intent.putExtra(EXTRA_DETAILS,details);
                intent.putExtra(EXTRA_CONTACT,contact);

                startActivity(intent);
            }
        });
    }

    //...................


    //    Shared Preference

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        selectDivision = sharedPreferences.getString(Division, "");
        selectLocation = sharedPreferences.getString(Location, "");
    }

    public void updateData() {
//        division = findViewById(R.id.division);
        location = findViewById(R.id.location);

//        division.setText(selectDivision);
        location.setText(selectLocation);

    }

    //..........................

    //Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.post:
                    Intent intent = new Intent(HomeActivity.this,PostActivity.class);
                    startActivity(intent);
                break;
            case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent signOut = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(signOut);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }

    //.......................

    //Navigation Drawer

    public void NavigationItems() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.post:
                        Intent intent = new Intent(HomeActivity.this,PostActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_post:
                        Intent my_post = new Intent(HomeActivity.this, MyPostsActivity.class);
                        startActivity(my_post);
                        break;
                    case R.id.feedback:

                        Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "ahamed_lincon@outlook.com"));

                        try {
                            startActivity(Intent.createChooser(feedback,"Choose an e-mail client"));
                            finish();
                        }catch (android.content.ActivityNotFoundException e){
                            Toast.makeText(HomeActivity.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                        }
//                        Intent feedback = new Intent(HomeActivity.this,FeedbackActivity.class);
//                        startActivity(feedback);
                        break;
                    case R.id.about:
//                        Intent test = new Intent(HomeActivity.this,ProfileActivity.class);
//                        startActivity(test);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        Toast.makeText(HomeActivity.this, "About", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent signOut = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(signOut);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    //............................

    @Override
    public void onStart() {
        super.onStart();
        loadData();
        updateData();
        FirebaseLoadData();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        loadData();
        updateData();
        FirebaseLoadData();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            adapter.stopListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
