//package com.example.basavara;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.example.mydailyvu.Activity.RoutineActivity;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.mikhaellopez.circularimageview.CircularImageView;
//import com.squareup.picasso.Picasso;
//
//public class CrProfileActivity extends AppCompatActivity {
//
//    Toolbar toolbar;
//
//    private TextView nameCr;
//    private TextView idCr;
//    private TextView semesterCr;
//    private TextView sectionCr;
//    private TextView contactCr;
//    private TextView emailCr;
//    private CircularImageView circularImageView;
//
//    ProgressBar progressBar;
//
//    private FirebaseAuth mAuth;
//    private String userID;
//
//    private FirebaseFirestore db;
//    private DocumentReference document_reference;
//
//    private static final String KEY_NAME = "name";
//    private static final String KEY_ID = "id";
//    private static final String KEY_SEMESTER = "semester";
//    private static final String KEY_SECTION = "section";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_CONTACT = "contact";
//    private static final String KEY_IMAGE = "imageUrl";
//
//    public static final String EXTRA_NAME = "com.example.firebaseprofile.EXTRA_NAME";
//    public static final String EXTRA_ID = "com.example.firebaseprofile.EXTRA_ID";
//    public static final String EXTRA_SEMESTER = "com.example.firebaseprofile.EXTRA_SEMESTER";
//    public static final String EXTRA_SECTION = "com.example.firebaseprofile.EXTRA_SECTION";
//    public static final String EXTRA_EMAIL = "com.example.firebaseprofile.EXTRA_EMAIL";
//    public static final String EXTRA_CONTACT = "com.example.firebaseprofile.EXTRA_CONTACT";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cr_profile);
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Profile");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        userID = mAuth.getUid();
//
//        nameCr = findViewById(R.id.tv_cr_name);
//        idCr = findViewById(R.id.tv_cr_id);
//        semesterCr = findViewById(R.id.tv_cr_semester);
//        sectionCr = findViewById(R.id.tv_cr_section);
//        contactCr = findViewById(R.id.tv_cr_contact);
//        emailCr = findViewById(R.id.tv_cr_email);
//
//        circularImageView = findViewById(R.id.circularImageView);
//
//        progressBar = findViewById(R.id.progress_loading);
//        progressBar.setVisibility(View.GONE);
//
//        db = FirebaseFirestore.getInstance();
//        document_reference = db.collection("CR_CSE").document(userID);
//
//        loadData();
//
//    }
//
//    public void loadData(){
//
//        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                if (documentSnapshot.exists()) {
//
//                    progressBar.setVisibility(View.VISIBLE);
//
//                    String name = documentSnapshot.getString(KEY_NAME);
//                    String id = documentSnapshot.getString(KEY_ID);
//                    String semester = documentSnapshot.getString(KEY_SEMESTER);
//                    String section = documentSnapshot.getString(KEY_SECTION);
//                    String contact = documentSnapshot.getString(KEY_CONTACT);
//                    String email = documentSnapshot.getString(KEY_EMAIL);
//                    String url = documentSnapshot.getString(KEY_IMAGE);
//
//                    Picasso.get().load(url).error(R.drawable.user_default).into(circularImageView);
//                    nameCr.setText(name);
//                    idCr.setText(id);
//                    semesterCr.setText(semester);
//                    sectionCr.setText(section);
//                    contactCr.setText(contact);
//                    emailCr.setText(email);
//
//                } else {
//                    Toast.makeText(CrProfileActivity.this, "Information does not exist!", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Picasso.get().load(R.drawable.user_default).into(circularImageView);
//            }
//        });
//
//        progressBar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_cr, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit:
//
//                String name = nameCr.getText().toString().trim();
//                String id = idCr.getText().toString().trim();
//                String semester = semesterCr.getText().toString().trim();
//                String section = sectionCr.getText().toString().trim();
//                String email = emailCr.getText().toString().trim();
//                String contact = contactCr.getText().toString().trim();
//
//                Intent edit = new Intent(CrProfileActivity.this, EditCrProfileActivity.class);
//                edit.putExtra(EXTRA_NAME,name);
//                edit.putExtra(EXTRA_ID,id);
//                edit.putExtra(EXTRA_SEMESTER,semester);
//                edit.putExtra(EXTRA_SECTION,section);
//                edit.putExtra(EXTRA_EMAIL,email);
//                edit.putExtra(EXTRA_CONTACT,contact);
//
//                startActivity(edit);
//                return true;
//            case R.id.delete_profile:
//                openDialog();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void openDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(CrProfileActivity.this);
//        builder.setTitle("Are you sure?")
//        .setMessage("If you delete this profile, it  will no longer be available in CR's list!")
//         .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialogInterface, int i) {
//
//                 document_reference.delete();
//                 Picasso.get().load(R.drawable.user_default).into(circularImageView);
//                 nameCr.setText("");
//                 idCr.setText("");
//                 semesterCr.setText("");
//                 sectionCr.setText("");
//                 contactCr.setText("");
//                 emailCr.setText("");
//
//                 Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
//             }
//         }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(CrProfileActivity.this, RoutineActivity.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }
//}
