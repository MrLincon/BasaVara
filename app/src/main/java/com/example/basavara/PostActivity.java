package com.example.basavara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.Authentication.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView location,address,details,contact,vara;
    private Button savePost;

    private FirebaseAuth mAuth;
    private String userID;
    private DocumentReference document_ref,document_reference;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Post");
//        toolbarTitle.setText("Post");

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

        loadData();

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

                    document_ref = db.collection("rajshahi").document(userID);

                    Map<String, String> userMap = new HashMap<>();

                    userMap.put("location", Location);
                    userMap.put("address", Address);
                    userMap.put("details", Details);
                    userMap.put("contact", Contact);
                    userMap.put("vara", Vara);

                    document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void loadData(){

        document_reference = db.collection("rajshahi").document(userID);

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    String get_location = documentSnapshot.getString("location");
                    String get_address = documentSnapshot.getString("address");
                    String get_details = documentSnapshot.getString("details");
                    String get_contact = documentSnapshot.getString("contact");
                    String get_vara = documentSnapshot.getString("vara");

                    location.setText(get_location);
                    address.setText(get_address);
                    details.setText(get_details);
                    contact.setText(get_contact);
                    vara.setText(get_vara);

                } else {
//                    Toast.makeText(PostActivity.this, "Information does not exist!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_post:
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle("Are you sure?")
                        .setMessage("If you delete this, it  will no longer be available in BasaVara database!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                document_reference.delete();
                                location.setText("");
                                address.setText("");
                                details.setText("");
                                contact.setText("");
                                vara.setText("");

                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
               break;
        }
        return super.onOptionsItemSelected(item);
    }
}
