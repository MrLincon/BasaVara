//package com.example.basavara;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.mikhaellopez.circularimageview.CircularImageView;
//import com.squareup.picasso.Picasso;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class EditCrProfileActivity extends AppCompatActivity {
//
//    private static final String KEY_USER_IMAGE = "imageUrl";
//
//    Toolbar toolbar;
//
//    ProgressBar progressBar;
//
//    EditText cr_name, cr_id, cr_semester, cr_section, cr_contact, cr_email;
//    TextView add_image;
//
//    CircularImageView imageView;
//
//    private FirebaseAuth mAuth;
//    private String userID;
//    private String image_link;
//
//
//    private FirebaseFirestore db;
//
//    private StorageReference mStorageRef;
//    private DocumentReference document_reference;
//    private DocumentReference document_ref;
//    private DocumentReference doc_ref;
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private Uri mImageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_cr_profile);
//
//        add_image = findViewById(R.id.add_image);
//        progressBar = findViewById(R.id.progress_loading);
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle("Edit Profile");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        imageView = findViewById(R.id.circularImageView_add);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userImage();
//            }
//        });
//
//        add_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userImage();
//            }
//        });
//
//
//        mStorageRef = FirebaseStorage.getInstance().getReference().child("uploadsCR");
//
//
//        cr_name = findViewById(R.id.cr_name);
//        cr_id = findViewById(R.id.cr_id);
//        cr_semester = findViewById(R.id.cr_semester);
//        cr_section = findViewById(R.id.cr_section);
//        cr_contact = findViewById(R.id.cr_contact);
//        cr_email = findViewById(R.id.cr_email);
//
//
//        Intent intent = getIntent();
//        String name = intent.getStringExtra(CrProfileActivity.EXTRA_NAME);
//        String id = intent.getStringExtra(CrProfileActivity.EXTRA_ID);
//        String semester = intent.getStringExtra(CrProfileActivity.EXTRA_SEMESTER);
//        String section = intent.getStringExtra(CrProfileActivity.EXTRA_SECTION);
//        String email = intent.getStringExtra(CrProfileActivity.EXTRA_EMAIL);
//        String contact = intent.getStringExtra(CrProfileActivity.EXTRA_CONTACT);
//
//        cr_name.setText(name);
//        cr_id.setText(id);
//        cr_semester.setText(semester);
//        cr_section.setText(section);
//        cr_email.setText(email);
//        cr_contact.setText(contact);
//
//        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
//        userID = mAuth.getUid();
//
//        progressBar.setVisibility(View.GONE);
//
////
//        document_reference = db.collection("CR_CSE").document(userID);
//
//        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if (documentSnapshot.exists()) {
//
//                    progressBar.setVisibility(View.VISIBLE);
//
//
//                    String url = documentSnapshot.getString(KEY_USER_IMAGE);
//
//                    Picasso.get().load(url).error(R.drawable.add_user_pic).into(imageView);
//
//
//                } else {
//                    Toast.makeText(EditCrProfileActivity.this, "Problem loading user image!", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//        progressBar.setVisibility(View.GONE);
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        Intent save = new Intent(EditCrProfileActivity.this, CrProfileActivity.class);
//        startActivity(save);
//        finish();
//        super.onBackPressed();
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }
//
//    private void userImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null) {
//            mImageUri = data.getData();
//
//            Picasso.get().load(mImageUri).error(R.drawable.add_user_pic).into(imageView);
//
//            final StorageReference imageName = mStorageRef.child(userID);
//
//            final ProgressDialog progressDialog = new ProgressDialog(EditCrProfileActivity.this);
//            progressDialog.setTitle("Uploading Photo");
//            progressDialog.setMessage("Please wait a few seconds!");
//            progressDialog.show();
//
//            imageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Toast.makeText(EditCrProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
//
//                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            image_link = uri.toString();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//
//                            doc_ref = db.collection("CR_CSE").document(userID);
//
//                            doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot documentSnapshot = task.getResult();
//                                        if (documentSnapshot != null && documentSnapshot.exists()) {
//                                            doc_ref.update("imageUrl", image_link).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    progressDialog.cancel();
//                                                    Toast.makeText(EditCrProfileActivity.this, "Profile picture uploaded!", Toast.LENGTH_SHORT).show();
//
//                                                    Intent save_img = new Intent(EditCrProfileActivity.this, CrProfileActivity.class);
//                                                    startActivity(save_img);
//                                                    finish();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    progressDialog.cancel();
//                                                    Toast.makeText(EditCrProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//
//                                        } else {
//                                            Map<String, String> userMapImg = new HashMap<>();
//
//                                            userMapImg.put("imageUrl", image_link);
//
//                                            doc_ref.set(userMapImg).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    progressDialog.cancel();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    progressDialog.cancel();
//                                                    Toast.makeText(EditCrProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//                            });
//
//                        }
//                    });
//
//                }
//
//            });
//
//
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.profile_save, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.save_details:
//                updateDetails();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//
//    private void updateDetails() {
//
//        final String name = cr_name.getText().toString().trim();
//        final String id = cr_id.getText().toString().trim();
//        final String semester = cr_semester.getText().toString().trim();
//        final String section = cr_section.getText().toString().toUpperCase().trim();
//        final String contact = cr_contact.getText().toString().toUpperCase().trim();
//        final String email = cr_email.getText().toString().trim();
//
////        if (mImageUri == null){
////            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
////            return;
////        }
//        if (name.isEmpty()) {
//            cr_name.setError("Field must be filled");
//            return;
//        }
//        if (id.isEmpty()) {
//            cr_id.setError("Field must be filled");
//            return;
//        }
//        if (semester.isEmpty()) {
//            cr_semester.setError("Field must be filled");
//            return;
//        }
//        if (section.isEmpty()) {
//            cr_section.setError("Field must be filled");
//            return;
//        }
//        if (email.isEmpty()) {
//            cr_email.setError("Field must be filled");
//            return;
//        }
//        if (contact.isEmpty()) {
//            cr_contact.setError("Field must be filled");
//            return;
//        } else {
//
//            ProgressDialog progressDialog = new ProgressDialog(EditCrProfileActivity.this);
//            progressDialog.setTitle("Updating");
//            progressDialog.setMessage("Please wait a few seconds!");
//            progressDialog.show();
//
//
//            document_ref = db.collection("CR_CSE").document(userID);
//
//            document_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        if (documentSnapshot != null && documentSnapshot.exists()) {
//
//                            document_ref.update("name", name,
//                                    "id", id,
//                                    "semester", semester,
//                                    "section", section,
//                                    "contact", contact,
//                                    "email", email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    cr_name.setText("");
//                                    cr_id.setText("");
//                                    cr_semester.setText("");
//                                    cr_section.setText("");
//                                    cr_contact.setText("");
//                                    cr_email.setText("");
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(EditCrProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            Intent save = new Intent(EditCrProfileActivity.this, CrProfileActivity.class);
//                            startActivity(save);
//                            finish();
//                        } else {
//                            Map<String, String> userMap = new HashMap<>();
//
//                            userMap.put("imageUrl", image_link);
//                            userMap.put("name", name);
//                            userMap.put("id", id);
//                            userMap.put("semester", semester);
//                            userMap.put("section", section);
//                            userMap.put("contact", contact);
//                            userMap.put("email", email);
//
//                            document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    cr_name.setText("");
//                                    cr_id.setText("");
//                                    cr_semester.setText("");
//                                    cr_section.setText("");
//                                    cr_contact.setText("");
//                                    cr_email.setText("");
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(EditCrProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            Intent save = new Intent(EditCrProfileActivity.this, CrProfileActivity.class);
//                            startActivity(save);
//                            finish();
//                        }
//                    }
//
//
//                }
//            });
//
//
//        }
//    }
//}