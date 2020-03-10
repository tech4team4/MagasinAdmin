package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirstActv extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailTV, passwordTV;
    private Button loginBtn;
    private ProgressBar progressBar;
    FirebaseFirestore db;

    ProgressBar splashProgress;
    int SPLASH_TIME = 2000; //This is 3 seconds
    String id = "";
    String email = "";
    String type = "";
    int k=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_actv);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            db.collection("user_magasin")
                    .document("restaurant_users")
                    .collection("restaurant_owner")
                    .whereEqualTo("email", "restau1@gmail.com")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                k= task.getResult().size();
                                if (k == 0) {
                                    //////l'utilisateur n'existe pas
                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        id = document.getId();
                                        email = (String) document.get("email");
                                        type = (String) document.get("type");
                                        Log.d("Heyyyyy66_For ", email + "      > " + document.getId() + " => " + document.getData());
                                    }
                                }
                            } else {
                                Toast.makeText(FirstActv.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                Log.d("Heyyyyy66", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            //Code to start timer and take action after the timer ends
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do any action here. Now we are moving to next page
                    if (type.equals("") || k==-1) {
                        Intent mySuperIntent = new Intent(FirstActv.this, MainActivity.class);
                        startActivity(mySuperIntent);
                    }
                    if (type.equals("restaurant")) {
                        Intent mySuperIntent = new Intent(FirstActv.this, EspaceTravail.class);
                        startActivity(mySuperIntent);
                    }
                    finish();
                }
            }, SPLASH_TIME);


        }else{
            Intent mySuperIntent = new Intent(FirstActv.this, MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }

    }


}
