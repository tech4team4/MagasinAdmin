package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class Ajouter_Categorie_Type_Menu extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText category_field;
    ////code pour afficher pop up menu
    LinearLayout L1, L2;
    Button accept;
    Dialog epicDialog;
    String id = "";
    String email = "";
    String name = "";
    String PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter__categorie__sous__menu);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        PATH = getIntent().getStringExtra("PATH");//   /Menu/1txFn61xmmPfbZvyVgbu/category/h1YdUw0sDJ6hde1JktEw
        epicDialog = new Dialog(this);

    }

    private void initializeUI() {
        category_field = findViewById(R.id.category_type_sous_menu);
    }

    public void ADD_Type(View view) {
        final String nom = category_field.getText().toString();
        if (nom.equals(null) || nom.equals("")) {
            category_field.setError("Ecrire un Type s'il vous plaît");
        } else {
            Insert_TYPE(PATH, nom);


        }
    }



    private void Insert_TYPE(String path, String nom_type) {
        Map<String, Object> Categ = new HashMap<>();
        Categ.put("name", nom_type);
//   /Menu/1txFn61xmmPfbZvyVgbu/category/h1YdUw0sDJ6hde1JktEw
        db.document(path).collection("list").document()
                .set(Categ)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("Heyyyyy98", "DocumentSnapshot successfully written!");
                        Toast.makeText(Ajouter_Categorie_Type_Menu.this, "TYPE a Ajoutée Avec Succées", Toast.LENGTH_LONG).show();
                        //Intent intent1 = new Intent(Ajouter_Categorie.this, ListActivity.class);
                        //startActivity(intent1);
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
