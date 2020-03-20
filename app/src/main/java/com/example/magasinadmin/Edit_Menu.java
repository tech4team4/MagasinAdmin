package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class Edit_Menu extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText menu_name, menu_details, manu_prix, menu_choix, menu_nbr_viande;
    Dialog epicDialog;
    String ID = "", IDR = "", PATH = "", TYPE = "", CATGORY = "", CAT = "";
    String namex, details;
    String choix;
    String prix;
    String nbr_viande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__menu);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //epicDialog = new Dialog(this);
        ////get Intent Extras
        ID = getIntent().getStringExtra("ID_Document");
        PATH = getIntent().getStringExtra("PATH");
        CATGORY = getIntent().getStringExtra("Category");
        CAT = getIntent().getStringExtra("CAT_NAME");

        namex = getIntent().getStringExtra("name");
        details = getIntent().getStringExtra("details");
        nbr_viande = (getIntent().getStringExtra("nbr_viande"));
        prix = (getIntent().getStringExtra("prix"));
        choix = getIntent().getStringExtra("choix");

        menu_name.setText(namex);
        menu_details.setText(details);
        manu_prix.setText(prix + "");
        menu_nbr_viande.setText(nbr_viande);
        menu_choix.setText(choix + "");
    }

    private void initializeUI() {
        menu_name = findViewById(R.id.menu_nomx);
        menu_details = findViewById(R.id.menu_detailsx);
        manu_prix = findViewById(R.id.menu_prixx);
        menu_nbr_viande = findViewById(R.id.menu_nbr_viandex);
        menu_choix = findViewById(R.id.menu_choixx);
    }

    public void MODIFIER_MENU(View view) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            final String name = menu_name.getText().toString();
            final String details = menu_details.getText().toString();
            final String prix = manu_prix.getText().toString();
            final String nbr_viande = menu_nbr_viande.getText().toString();
            final String menu_choixx = menu_choix.getText().toString();
            if (name.equals(null) || name.equals("")) {
                menu_name.setError("Ecrire Un Nom s'il vous plaît");
            } else {
                if (details.equals(null) || details.equals("")) {
                    menu_details.setError("Ecrire les Details s'il vous plaît");
                } else {
                    if (prix.equals(null) || prix.equals("")) {
                        manu_prix.setError("Définir le Prix s'il vous plaît");
                    } else {
                        if (nbr_viande.equals(null) || nbr_viande.equals("")) {
                            menu_nbr_viande.setError("Définir le nombre des Viandes s'il vous plaît");
                        } else {
                            if (menu_choixx.equals(null) || menu_choixx.equals("")) {
                                menu_choix.setError("Définir le choix ajouter s'il vous plaît");
                            } else {
                                // Get a new write batch
                                WriteBatch batch = db.batch();
                                DocumentReference document = db.document(PATH);
                                /*batch.update(document,"name",name);
                                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                    }
                                });
*/

                                document.update("name", name,
                                        "details", details,
                                        "prix", Long.parseLong(prix),
                                        "nbr_viande", Integer.parseInt(nbr_viande),
                                        "choix", Boolean.parseBoolean(choix))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Edit_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Edit_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                            }
                                        });/*
                                document.update("details", details)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(Edit_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Edit_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                document.update("prix", Long.parseLong(prix))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(Edit_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Edit_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                document.update("nbr_viande", Integer.parseInt(nbr_viande))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(Edit_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Edit_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                document.update("choixx", Integer.parseInt(menu_choixx))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(Edit_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Edit_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                            }
                                        });*/

                            }
                        }
                    }
                }
            }
        } else {
            connected = false;
            Toast.makeText(Edit_Menu.this, "Connexion Internet Limiter", Toast.LENGTH_LONG).show();
            Log.d("Heyyyyy55", "fail");
        }
    }
}
