package com.example.basavara.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.Adapters.Basa;
import com.example.basavara.HomeActivity;
import com.example.basavara.Profile.EditProfileActivity;
import com.example.basavara.Profile.ProfileActivity;
import com.example.basavara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private EditText area,address, details, contact, vara;
    private TextView name,email,edit;
    private Button savePost;

    Spinner post_division_spinner;
    Spinner post_location_spinner;

    public String user_division;
    public String user_location;

    private FirebaseAuth mAuth;
    private String userID;
    private DocumentReference document_ref;
    private DocumentReference document_reference;

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

        document_reference = db.collection("UserDetails").document(userID);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        edit = findViewById(R.id.edit_user_profile);
        area = findViewById(R.id.area);
        address = findViewById(R.id.address);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);
        vara = findViewById(R.id.vara);
        savePost = findViewById(R.id.savePost);

        post_division_spinner = findViewById(R.id.division);
        final ArrayAdapter<CharSequence> divisionAdapter = ArrayAdapter.createFromResource(this,
                R.array.divisions, android.R.layout.simple_spinner_item);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        post_division_spinner.setAdapter(divisionAdapter);
        post_division_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_division = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        post_location_spinner = findViewById(R.id.location);
        final ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.underRajshahi, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        post_location_spinner.setAdapter(cityAdapter);
        post_location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    String Name = documentSnapshot.getString("name");
                    String Email = documentSnapshot.getString("email");
                    String Division = documentSnapshot.getString("division");
                    String Location = documentSnapshot.getString("location");
                    String Address = documentSnapshot.getString("address");
                    String Contact = documentSnapshot.getString("contact");

                    name.setText(Name);
                    email.setText(Email);
//                    address.setText(Address);
                    contact.setText(Contact);

                    if (Division != null) {
                        int spinnerPosition = divisionAdapter.getPosition(Division);
                        post_division_spinner.setSelection(spinnerPosition);
                    }if (Location != null) {
                        int spinnerPosition = cityAdapter.getPosition(Location);
                        post_location_spinner.setSelection(spinnerPosition);
                    }

                } else {
                    Toast.makeText(PostActivity.this, "Information does not exist!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Division = user_division;
                String Location = user_location;
                String Area = area.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Details = details.getText().toString().trim();
                String Contact = contact.getText().toString().trim();
                String Vara = vara.getText().toString().trim();


                if (Area.isEmpty()) {
                    area.setError("All field must be filled");
                    return;
                }if (Address.isEmpty()) {
                    address.setError("All field must be filled");
                    return;
                }
                if (Details.isEmpty()) {
                    details.setError("All field must be filled");
                    return;
                }
                if (Contact.isEmpty()) {
                    contact.setError("All field must be filled");
                    return;
                }
                if (Vara.isEmpty()) {
                    vara.setError("All field must be filled");
                    return;
                } else {

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
                    basa.setName(Name);
                    basa.setEmail(Email);
                    basa.setDivision(Division);
                    basa.setLocation(Location);
                    basa.setArea(Area);
                    basa.setAddress(Address);
                    basa.setDetails(Details);
                    basa.setContact(Contact);
                    basa.setVara(Vara);
                    basa.setUser_id(userID);
                    basa.setAd_id(document_ref.getId());


                    document_ref.set(basa).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            area.setText("");
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change_profile = new Intent(PostActivity.this, EditProfileActivity.class);
                startActivity(change_profile);
            }
        });

    }
}
