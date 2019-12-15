package com.example.basavara.Post;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.basavara.HomeActivity;
import com.example.basavara.MyPostsActivity;
import com.example.basavara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView area,address,details,contact,vara;
    private Button savePost;

    private Spinner edit_division_spinner;
    private Spinner edit_location_spinner;

    public String user_division;
    public String user_location;

    private String get_location,get_division;

    private FirebaseAuth mAuth;
    private String userID;
    private DocumentReference document_ref,document_reference;

    private FirebaseFirestore db;

    private String AD_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        toolbar = findViewById(R.id.toolbar_my_post);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Edit Post");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getUid();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        area = findViewById(R.id.area);
        address = findViewById(R.id.address);
        details = findViewById(R.id.details);
        contact = findViewById(R.id.contact);
        vara = findViewById(R.id.vara);
        savePost = findViewById(R.id.savePost);

        final Intent intent = getIntent();
        AD_ID = intent.getStringExtra(MyPostsActivity.EXTRA_AD_ID);

        loadData();


        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Division = user_division;
                String Location = user_location;
                String Area = area.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Details = details.getText().toString().trim();
                String Contact = contact.getText().toString().trim();
                String Vara = vara.getText().toString().trim();


                if (Area.isEmpty()){
                    area.setError("All field must be filled");
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

                    ProgressDialog progressDialog = new ProgressDialog(EditPostActivity.this);
                    progressDialog.setTitle("Updating");
                    progressDialog.setMessage("Please wait a few seconds!");
                    progressDialog.show();

                    document_ref = db.collection("rajshahi").document(AD_ID);

                    document_ref.update("division", Division,
                            "location", Location,
                            "area", Area,
                            "address", Address,
                            "details", Details,
                            "contact", Contact,
                            "vara", Vara).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            Toast.makeText(EditPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent save = new Intent(EditPostActivity.this, HomeActivity.class);
                    startActivity(save);
                    finish();

                }

            }
        });

    }

    public void loadData(){
        edit_division_spinner = findViewById(R.id.editDivision);
        final ArrayAdapter<CharSequence> editDivisionAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.divisions, android.R.layout.simple_spinner_item);
        editDivisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_division_spinner.setAdapter(editDivisionAdapter);
        edit_division_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_division = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_location_spinner = findViewById(R.id.editLocation);
        final ArrayAdapter<CharSequence> editLocationAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.underRajshahi, android.R.layout.simple_spinner_item);
        editLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_location_spinner.setAdapter(editLocationAdapter);
        edit_location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        document_reference = db.collection("rajshahi").document(AD_ID);

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    get_division = documentSnapshot.getString("division");
                    get_location = documentSnapshot.getString("location");
                    String get_area = documentSnapshot.getString("area");
                    String get_address = documentSnapshot.getString("address");
                    String get_details = documentSnapshot.getString("details");
                    String get_contact = documentSnapshot.getString("contact");
                    String get_vara = documentSnapshot.getString("vara");

                    if (get_division != null) {
                        int spinnerPosition = editDivisionAdapter.getPosition(get_division);
                        edit_division_spinner.setSelection(spinnerPosition);
                    }if (get_location != null) {
                        int spinnerPosition = editLocationAdapter.getPosition(get_location);
                        edit_location_spinner.setSelection(spinnerPosition);
                    }

                    area.setText(get_area);
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
        inflater.inflate(R.menu.my_post_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_post:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
                builder.setTitle("Are you sure?")
                        .setMessage("If you delete this, it  will no longer be available in BasaVara database!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                document_reference.delete();
                                area.setText("");
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

    @Override
    protected void onStart() {
        super.onStart();
    }
}
