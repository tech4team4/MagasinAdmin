package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Ajouter_Categorie extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText category_field;
    ////code pour afficher pop up menu
    LinearLayout L1,L2;
    Button b, accept;
    Dialog epicDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter__categorie);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        epicDialog = new Dialog(this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                epicDialog.setContentView(R.layout.bottom_edit_list);
               // accept = (Button) epicDialog.findViewById(R.id.Accept);
                L1=(LinearLayout) epicDialog.findViewById(R.id.Editer_Categorie);
                L2=(LinearLayout) epicDialog.findViewById(R.id.Supprimer_Categorie);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        epicDialog.dismiss();
                    }
                });
                L1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Ajouter_Categorie.this,"L1111",Toast.LENGTH_LONG).show();
                    }
                });

                L2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Ajouter_Categorie.this,"L2",Toast.LENGTH_LONG).show();
                    }
                });


                epicDialog.show();
            }
        });
    }

    public void ShowPopUp(){

    }


    private void initializeUI() {
        category_field = findViewById(R.id.category_field);
    }

    String id = "";
    String email = "";
    String name = "";

    public void ADD(View view) {
        final String nom = category_field.getText().toString();
        if (nom.equals(null) || nom.equals("")) {
            category_field.setError("Ecrire une Categorie s'il vous plaît");
        } else {

            final FirebaseUser currentUser = mAuth.getCurrentUser();
            //get all document with data
            db.collection("Menu").whereEqualTo("email", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Heyyyyy1 ", document.getId() + " => " + document.getData());
                                    id = document.getId();
                                }
                                if (!id.equals(""))
                                    insert_category(id, nom);

                            } else {
                                Log.w("Heyyyyy2 ", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
    }


    public void insert_category(String id, String nom_catg) {
        Map<String, Object> Categ = new HashMap<>();
        Categ.put("name", nom_catg);

        db.collection("Menu")
                .document(id)
                .collection("category")
                .document()
                .set(Categ)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Heyyyyy98", "DocumentSnapshot successfully written!");
                        Toast.makeText(Ajouter_Categorie.this, "Categorie a Ajoutée Avec Succées", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(Ajouter_Categorie.this, ListActivity.class);
                        startActivity(intent1);
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
