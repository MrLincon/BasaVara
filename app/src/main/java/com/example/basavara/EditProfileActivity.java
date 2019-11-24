package com.example.basavara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    ProgressBar progressBar;

    EditText user_name,user_email,user_contact,blood_group;
    TextView add_image;
    RadioGroup professionGroup;
    RadioButton professionButton;

    CircularImageView imageView;

    private FirebaseAuth mAuth;
    private String userID;
    private String image_link;


    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    private DocumentReference document_reference;
    private DocumentReference document_ref;
    private DocumentReference doc_ref;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Edit Profile");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressBar = findViewById(R.id.progress_loading);
        add_image = findViewById(R.id.tv_add_image);
        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        blood_group = findViewById(R.id.blood_group);
        user_contact = findViewById(R.id.contact);
        imageView = findViewById(R.id.add_image);

        professionGroup = findViewById(R.id.professionGroup);


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference().child("uploads");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getUid();

        progressBar.setVisibility(View.GONE);

//
        document_reference = db.collection("UserDetails").document(userID);

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);


                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String bloodGroup = documentSnapshot.getString("b_group");
                    String contact = documentSnapshot.getString("contact");
                    String url = documentSnapshot.getString("imageUrl");

                    int selectedId = professionGroup.getCheckedRadioButtonId();
                    professionButton = findViewById(selectedId);
                    String profession = documentSnapshot.getString("profession");

                    user_name.setText(name);
                    user_email.setText(email);
                    blood_group.setText(bloodGroup);
                    user_contact.setText(contact);
                    Picasso.get().load(url).error(R.drawable.add_user_pic).into(imageView);

                    if (profession.equals("General")){
                       RadioButton rb = findViewById(R.id.general);
                       rb.setChecked(true);
                    }else if(profession.equals("Nutritionist")){
                        RadioButton rb = findViewById(R.id.nutritionist);
                        rb.setChecked(true);
                    }

                } else {
                    Toast.makeText(EditProfileActivity.this, "Problem loading user image!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        progressBar.setVisibility(View.GONE);



    }

    private void userImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).error(R.drawable.add_user_pic).into(imageView);

            final StorageReference imageName = mStorageRef.child(userID);

            final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setTitle("Uploading Photo");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();

            imageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_link = uri.toString();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            doc_ref = db.collection("UserDetails").document(userID);

                            doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            doc_ref.update("imageUrl", image_link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(EditProfileActivity.this, "Profile picture uploaded!", Toast.LENGTH_SHORT).show();

                                                    Intent save_img = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                    startActivity(save_img);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        } else {
                                            Map<String, String> userMapImg = new HashMap<>();

                                            userMapImg.put("imageUrl", image_link);

                                            doc_ref.set(userMapImg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.cancel();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }
                    });

                }

            });


        }
    }


    private void updateDetails() {

        final String name = user_name.getText().toString().trim();
        final String email = user_email.getText().toString().trim();
        final String bloodGroup = blood_group.getText().toString().trim();
        final String contact = user_contact.getText().toString().trim();

        int selectedId = professionGroup.getCheckedRadioButtonId();
        professionButton = findViewById(selectedId);
        final String profession = professionButton.getText().toString();



//        if (mImageUri == null){
//            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (name.isEmpty()) {
            user_name.setError("Field must be filled");
            return;
        }
        if (email.isEmpty()) {
            user_email.setError("Field must be filled");
            return;
        } if (bloodGroup.isEmpty()) {
            blood_group.setError("Field must be filled");
            return;
        }
        if (contact.isEmpty()) {
            user_contact.setError("Field must be filled");
            return;

        } else {

            ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();


            document_ref = db.collection("UserDetails").document(userID);

            document_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {

                            document_ref.update("name", name,
                                    "email", email,
                                    "b_group", bloodGroup,
                                    "contact", contact,
                                    "profession",profession).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Intent save = new Intent(EditProfileActivity.this, ProfileActivity.class);
                            startActivity(save);
                            finish();
                        } else {
                            Map<String, String> userMap = new HashMap<>();

                            userMap.put("imageUrl", image_link);
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("b_group", bloodGroup);
                            userMap.put("contact", contact);
                            userMap.put("profession", profession);

                            document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Intent save = new Intent(EditProfileActivity.this, ProfileActivity.class);
                            startActivity(save);
                            finish();
                        }
                    }


                }
            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_details:
                updateDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
