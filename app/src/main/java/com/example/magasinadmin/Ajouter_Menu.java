package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Ajouter_Menu extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText menu_name, menu_details, manu_prix, menu_choix, menu_nbr_viande;
    Dialog epicDialog;
    String ID = "", IDR = "", PATH = "", TYPE = "", CATGORY = "", CAT="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter__menu);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        epicDialog = new Dialog(this);
        ////get Intent Extras
        ID = getIntent().getStringExtra("ID_Document");
        PATH = getIntent().getStringExtra("PATH");
        TYPE = getIntent().getStringExtra("Title");
        CATGORY = getIntent().getStringExtra("Category");
        CAT=getIntent().getStringExtra("CAT_NAME");
    }

    private void initializeUI() {
        menu_name = findViewById(R.id.menu_nom);
        menu_details = findViewById(R.id.menu_details);
        manu_prix = findViewById(R.id.menu_prix);
        menu_nbr_viande = findViewById(R.id.menu_nbr_viande);
        menu_choix = findViewById(R.id.menu_choix);
    }


    public void ADD_MENU(View view) {
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
                            Insert_TYPE(PATH, name, details, prix, nbr_viande, menu_choixx);
                        }
                    }
                }
            }
        }
    }

    private void Insert_TYPE(String path, String nom, String details, String prix, String nbr, String choixxx) {
        Map<String, Object> Categ = new HashMap<>();
        Categ.put("name", nom);
        Categ.put("details", details);
        Categ.put("prix", Long.parseLong(prix));
        Categ.put("nbr_viande", Integer.parseInt(nbr));
        Categ.put("choix", Boolean.valueOf(choixxx));
//   /Menu/1txFn61xmmPfbZvyVgbu/category/h1YdUw0sDJ6hde1JktEw
        db.document(CAT).collection("list_menu").document()
                .set(Categ)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Ajouter_Menu.this, "TYPE a Ajoutée Avec Succées", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Heyyyyy98", "Error writing document", e);
                    }
                });
    }


}
