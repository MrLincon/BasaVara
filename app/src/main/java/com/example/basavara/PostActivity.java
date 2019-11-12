package com.example.basavara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.Adapters.Basa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView location,address,details,contact,vara;
    private Button savePost;

    private FirebaseAuth mAuth;
    private String userID;
    private DocumentReference document_ref;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolbar = findViewById(R.id.toolbar_post);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Post Ad");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getUid();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        location = findViewById(R.id.area);
        address = findViewById(R.id.address);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);
        vara = findViewById(R.id.vara);
        savePost = findViewById(R.id.savePost);

        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Location = location.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Details = details.getText().toString().trim();
                String Contact = contact.getText().toString().trim();
                String Vara = vara.getText().toString().trim();


                if (Location.isEmpty()){
                    location.setError("All field must be filled");
                    return;
                }if (Address.isEmpty()){
                    address.setError("All field must be filled");
                    return;
                }if (Details.isEmpty()){
                    details.setError("All field must be filled");
                    return;
                }if (Contact.isEmpty()){
                    contact.setError("All field must be filled");
                    return;
                }if (Vara.isEmpty()){
                    vara.setError("All field must be filled");
                    return;
                }else{

                    ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                    progressDialog.setTitle("Updating");
                    progressDialog.setMessage("Please wait a few seconds!");
                    progressDialog.show();

//                    document_ref = db.collection("rajshahi").document(userID);
                    document_ref = db.collection("rajshahi").document();

//                    Map<String, String> userMap = new HashMap<>();
//
//                    userMap.put("location", Location);
//                    userMap.put("address", Address);
//                    userMap.put("details", Details);
//                    userMap.put("contact", Contact);
//                    userMap.put("vara", Vara);

                    Basa basa = new Basa();
                    basa.setLocation(Location);
                    basa.setAddress(Address);
                    basa.setDetails(Details);
                    basa.setContact(Contact);
                    basa.setVara(Vara);
                    basa.setUser_id(userID);
                    basa.setAd_id(document_ref.getId());


                    document_ref.set(basa).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            location.setText("");
                            address.setText("");
                            details.setText("");
                            contact.setText("");
                            vara.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent save = new Intent(PostActivity.this, HomeActivity.class);
                    startActivity(save);
                    finish();

                }

            }
        });

    }
}
