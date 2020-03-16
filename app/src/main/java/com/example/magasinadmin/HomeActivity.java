package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        /*------------------definir les relations---------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*------------------Tool Bar---------------*/
        setSupportActionBar(toolbar);

        /*------------------Navigation Drawer Menu---------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        /*-----------------------ce code pour navigation drawer-------------------*/
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    String id="";
    /*-----------------------------ce code pour navigation drawer---------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_list:

                db.collection("Menu")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = document.getId();
                                Log.d("Heyyyyy66Home ", "      > " + document.getId() + " => " + document.getData());
                            }
                            Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                            intent.putExtra("IDR",id);
                            startActivity(intent);
                            //finish();

                        } else {
                            //Log.d("Heyyyyy66", "Error getting documents: ", task.getException());
                        }
                    }
                });

                break;
            case R.id.nav_logout:
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {
        // Firebase sign out
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent1 = new Intent(HomeActivity.this, Log_In_Email.class);
        startActivity(intent1);
        finish();
    }


}
