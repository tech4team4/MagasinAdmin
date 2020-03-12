package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //////code
    private RecyclerView mFirestoreList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mFirestoreList=findViewById(R.id.firestore_list);



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
        navigationView.setCheckedItem(R.id.nav_list);//dans cette activity on selectionne menu courant
    }
    @Override
    public void onBackPressed() {
        /*-----------------------ce code pour navigation drawer-------------------*/
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ListActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*-----------------------------ce code pour navigation drawer---------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(ListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_list:
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
        Intent intent1 = new Intent(ListActivity.this, Log_In_Email.class);
        startActivity(intent1);
        finish();
    }

    public void Ajouter_categorie(View view) {
        Intent intent1 = new Intent(ListActivity.this, Ajouter_Categorie.class);
        startActivity(intent1);
        finish();
    }
}
