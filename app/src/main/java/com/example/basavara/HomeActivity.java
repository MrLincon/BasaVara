package com.example.basavara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.basavara.Authentication.LoginActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    RecyclerView recyclerView;

    public static final String EXTRA_LOCATION = "com.example.firebaseprofile.EXTRA_LOCATION";
    public static final String EXTRA_VARA = "com.example.firebaseprofile.EXTRA_VARA";
    public static final String EXTRA_ADDRESS = "com.example.firebaseprofile.EXTRA_ADDRESS";
    public static final String EXTRA_DETAILS = "com.example.firebaseprofile.EXTRA_DETAILS";
    public static final String EXTRA_CONTACT = "com.example.firebaseprofile.EXTRA_CONTACT";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference basa = db.collection("rajshahi");

    private BasaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        recyclerView = findViewById(R.id.recyclerview);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.bottomPadding);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        recyclerView.addItemDecoration(new BasaRecyclerDecoration(topPadding, sidePadding, bottomPadding));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("BasaVara");

        Query query = basa.orderBy("vara", Query.Direction.ASCENDING);
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

                Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);

                String location = basa.getLocation();
                String vara = basa.getVara();
                String details = basa.getDetails();
                String address = basa.getAddress();
                String contact = basa.getContact();

                intent.putExtra(EXTRA_LOCATION,location);
                intent.putExtra(EXTRA_VARA,vara);
                intent.putExtra(EXTRA_ADDRESS,address);
                intent.putExtra(EXTRA_DETAILS,details);
                intent.putExtra(EXTRA_CONTACT,contact);

                startActivity(intent);
//                Toast.makeText(HomeActivity.this, id, Toast.LENGTH_SHORT).show();
            }
        });


    }

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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
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