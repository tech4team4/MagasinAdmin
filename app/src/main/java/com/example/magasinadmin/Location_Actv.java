package com.example.magasinadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class Location_Actv extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation = null;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 9874;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference storageReference;
    String id = "";

    /*---------------------------------------On Create---------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__actv);
        id = getIntent().getStringExtra("ID");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Location_Actv.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    float x = -1000000, y = -1000000;

    /*---------------------------------------Method Map Design---------------------------------*/
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //UiSettings.setZoomControlsEnabled(true)
        if (currentLocation != null) {
            x = (float) currentLocation.getLatitude();
            y = (float) currentLocation.getLongitude();
        }
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("TRYYY", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TRYYY", "Can't find style. Error: ", e);
        }

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("ma Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                float o = googleMap.getCameraPosition().zoom;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Mon Magasin");

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, o));
                googleMap.addMarker(markerOptions);
                x = (float) markerOptions.getPosition().latitude;
                y = (float) markerOptions.getPosition().longitude;
            }
        });
    }


    LatLng latLng;
    GeoPoint geoPoint;

    /*---------------------------------------Code Button Save GeoLocation On FireStore---------------------------------*/
    public void GetThisLocation(View view) {
        if (x != -1000000 && y != -1000000) {

            /*db.collection("Menu")
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
                    });*/
            /////////////////
            geoPoint = new GeoPoint(x, y);
            //latLng = new LatLng(x, y);
            Map<String, Object> Categ = new HashMap<>();
            Categ.put("Location_magasin", geoPoint);
            db.collection("Menu").document(id).set(Categ, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Location_Actv.this, "Location Pris Avec Succéss", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Location_Actv.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(Location_Actv.this, "Choisi Un Emplacement S'il Vous Plait", Toast.LENGTH_LONG).show();
        }
    }
}