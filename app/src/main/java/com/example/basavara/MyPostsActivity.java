package com.example.basavara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.basavara.Adapters.Basa;
import com.example.basavara.Adapters.MyPostsAdapter;
import com.example.basavara.Post.EditPostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    private MyPostsAdapter adapter;

    public static final String EXTRA_AD_ID = "com.example.firebaseprofile.EXTRA_AD_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        toolbar = findViewById(R.id.toolbar_test);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("My Posts");

        recyclerView = findViewById(R.id.recyclerview_my_posts);

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getUid() ;

        CollectionReference basa = db.collection("rajshahi");

        Query query = basa.whereEqualTo("user_id",userID).orderBy("vara", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Basa> options = new FirestoreRecyclerOptions.Builder<Basa>()
                .setQuery(query, Basa.class)
                .build();

        adapter = new MyPostsAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new MyPostsAdapter.OnItemClickListener()  {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                Basa basa = documentSnapshot.toObject(Basa.class);
                String id = documentSnapshot.getId();

                Intent intent = new Intent(MyPostsActivity.this, EditPostActivity.class);
                intent.putExtra(EXTRA_AD_ID,id);
                startActivity(intent);
            }
        });


    }
}
