package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Edit_Category_List extends AppCompatActivity {

    EditText nom_category_editText;
    TextView nom_category_textView;
    String Path = "", ID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__category__list);
        Path = getIntent().getStringExtra("PATH");
        ID = getIntent().getStringExtra("ID_Document");
        mAuth = FirebaseAuth.getInstance();

        nom_category_editText = findViewById(R.id.Nom_Category_editText);
        nom_category_textView = findViewById(R.id.Nom_Category_textView);
        nom_category_textView.setText(ID);
    }

    /*public String get_name(){
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference documentReference=db.document(ID);
        db.collection("Menu")
                .document()
                .collection("")
                .whereEqualTo("email", currentUser.getEmail())
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

        return "xxx";
    }*/


    public void SupprimerAction(View view) {
    }

    public void ModifierAction(View view) {
        String name = nom_category_editText.getText().toString();
        String old = nom_category_textView.getText().toString();
        if (name.equals("") || name.equals(null)) {
            nom_category_editText.setError("Ecrire Une Categorie");
        } else {
            DocumentReference document = db.document(Path);
            document.update("name", name)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Edit_Category_List.this, "Connexion OOOKKK", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Edit_Category_List.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                        }
                    });


        }

    }
}
