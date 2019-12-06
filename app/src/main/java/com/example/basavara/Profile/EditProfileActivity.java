package com.example.basavara.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basavara.HomeActivity;
import com.example.basavara.R;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolbarTitle;
    ProgressBar progressBar;

    Spinner division_spinner;
    Spinner city_spinner;

    EditText user_name, user_email, user_address, user_contact;
    TextView add_image;

    CircularImageView imageView;

    private FirebaseAuth mAuth;
    private String userID;
    private String image_link,thumb_image_link;


    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    private StorageReference mStorageRefThumb;
    private DocumentReference document_reference;
    private DocumentReference document_ref;
    private DocumentReference doc_ref;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private Uri resultUri;
    private Uri thumbResultUri;

    public String user_division;
    public String user_city;

    Bitmap bitmap;
    Bitmap thumbBitmap;

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
//        user_division = findViewById(R.id.division);
//        user_city = findViewById(R.id.city);
        user_address = findViewById(R.id.address);
        user_contact = findViewById(R.id.contact);
        imageView = findViewById(R.id.add_image);

        division_spinner = findViewById(R.id.division);
        final ArrayAdapter<CharSequence> divisionAdapter = ArrayAdapter.createFromResource(this,
                R.array.divisions, android.R.layout.simple_spinner_item);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division_spinner.setAdapter(divisionAdapter);
        division_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_division = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner = findViewById(R.id.city);
        final ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityAdapter);
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_city = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        mStorageRef = FirebaseStorage.getInstance().getReference().child("userProfile");
        mStorageRefThumb = FirebaseStorage.getInstance().getReference().child("userProfileThumbnails");

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
                    String division = documentSnapshot.getString("division");
                    String city = documentSnapshot.getString("city");
                    String address = documentSnapshot.getString("address");
                    String contact = documentSnapshot.getString("contact");
                    String url = documentSnapshot.getString("thumbImageUrl");

                    user_name.setText(name);
                    user_email.setText(email);
                    user_address.setText(address);
                    user_contact.setText(contact);
                    Picasso.get().load(url).error(R.drawable.add_user_pic).into(imageView);

                    if (division != null) {
                        int spinnerPosition = divisionAdapter.getPosition(division);
                        division_spinner.setSelection(spinnerPosition);
                    }
                    if (city != null) {
                        int spinnerPosition = cityAdapter.getPosition(city);
                        city_spinner.setSelection(spinnerPosition);
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

            Log.d(TAG, "onActivityResult: inside Activity Result");

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(EditProfileActivity.this);
        }

//            CropImage.ActivityResult result = null;

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                Log.d(TAG, "onActivityResult: Crop Image");

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    resultUri = result.getUri();
                    thumbResultUri = result.getUri();

                    Picasso.get().load(resultUri).error(R.drawable.add_user_pic).into(imageView);

                    File compressed = new File(resultUri.getPath());
                    File compressedThumb = new File(thumbResultUri.getPath());

                    try {
                        bitmap = new Compressor(EditProfileActivity.this)
                                .setQuality(100)
                                .compressToBitmap(compressed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                    final byte[] image_byte = byteArrayOutputStream.toByteArray();

                    try {
                        thumbBitmap = new Compressor(EditProfileActivity.this)
                                .setQuality(100)
                                .compressToBitmap(compressedThumb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream thumbByteArrayOutputStream = new ByteArrayOutputStream();
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 30, thumbByteArrayOutputStream);
                    final byte[] thumb_image_byte = thumbByteArrayOutputStream.toByteArray();

//                    long time= System.currentTimeMillis();


                    final StorageReference imageName = mStorageRef.child(userID);
                    final StorageReference thumbImageName = mStorageRefThumb.child(userID);

                    final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
                    progressDialog.setTitle("Uploading Photo");
                    progressDialog.setMessage("Please wait a few seconds!");
                    progressDialog.show();

                    UploadTask uploadTask = imageName.putBytes(image_byte);
                    UploadTask uploadTaskThumb = thumbImageName.putBytes(thumb_image_byte);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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




                    //Uploading Thumbnails

                    uploadTaskThumb.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                            thumbImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    thumb_image_link = uri.toString();
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
                                                    doc_ref.update("thumbImageUrl", thumb_image_link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(EditProfileActivity.this, "Profile thumb picture uploaded!", Toast.LENGTH_SHORT).show();

                                                            Intent save_img = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                            startActivity(save_img);
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                } else {
                                                    Map<String, String> userMapThumbImg = new HashMap<>();

                                                    userMapThumbImg.put("thumbImageUrl", thumb_image_link);

                                                    doc_ref.set(userMapThumbImg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
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

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                Exception error = croppedImage.getError();
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }

    }


    private void updateDetails() {

        final String name = user_name.getText().toString().trim();
        final String email = user_email.getText().toString().trim();
        final String division = user_division;
        final String city = user_city;
        final String address = user_address.getText().toString().trim();
        final String contact = user_contact.getText().toString().trim();


        if (name.isEmpty()) {
            user_name.setError("Field must be filled");
            return;
        }
        if (email.isEmpty()) {
            user_email.setError("Field must be filled");
            return;
        }
        if (address.isEmpty()) {
            user_address.setError("Field must be filled");
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
                                    "division", division,
                                    "city", city,
                                    "address", address,
                                    "contact", contact).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            userMap.put("division", division);
                            userMap.put("city", city);
                            userMap.put("address", address);
                            userMap.put("contact", contact);

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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
