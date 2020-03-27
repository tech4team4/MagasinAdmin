package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    float x = -1000000, y = -1000000;
    private StorageReference mStorageRef;
    ImageView profile_image;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference storageReference;
    EditText location;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_image = findViewById(R.id.image_profile);
        location = findViewById(R.id.location_gps_profile);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storageReference = storage.getReferenceFromUrl("gs://deliveryapk-a5b8f.appspot.com/Images_magasin").child(currentUser.getUid() + ".jpg");

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Image_Profile_Editing.class);
                startActivity(intent);
            }
        });

        //get user id
        db.collection("Menu")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = (String) document.getId();
                            }
                        }
                    }
                });

        RetrieveUserInfo();
        getLocationFromDB();


    }


    GeoPoint geoPoint;
    double lat = -1000000;
    double lng = -1000000;
    LatLng latLng;

    private void getLocationFromDB() {
        db.collection("Menu")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = document.getId();
                                geoPoint = document.getGeoPoint("Location_magasin");
                                location.setText(geoPoint.getLatitude()+","+geoPoint.getLongitude());
                                Log.d("Heyyyyyvvvvvvvvvvv ", "      > " + document.getId() + " => " + document.getData());
                            }

                        } else {
                            location.setText("aucune Location Ajouter");
                            //Log.d("Heyyyyy66", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void demander_password(View view) {

    }


    private void RetrieveUserInfo() {
        final File file;
        try {
            file = File.createTempFile("image", "jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile((file.getAbsolutePath()));
                    profile_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        RetrieveUserInfo();
    }

    public void ChoosLocation(View view) {
        Intent intent = new Intent(Profile.this, Location_Actv.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    public void SaveProfileInfo(View view) {
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        RetrieveUserInfo();
    }*/
}
