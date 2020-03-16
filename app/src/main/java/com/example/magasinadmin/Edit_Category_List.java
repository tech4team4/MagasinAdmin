package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Edit_Category_List extends AppCompatActivity {

    EditText nom_category_editText;
    TextView nom_category_textView;
    String Path = "", ID = "", IDR = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__category__list);
        Path = getIntent().getStringExtra("PATH");
        ID = getIntent().getStringExtra("ID_Document");
        IDR = getIntent().getStringExtra("ID_Restaurant");
        mAuth = FirebaseAuth.getInstance();

        nom_category_editText = findViewById(R.id.Nom_Category_editText);
        nom_category_textView = findViewById(R.id.Nom_Category_textView);
        nom_category_textView.setText("");
        get_name();
    }

    String x = "";

    public String get_name() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final DocumentReference documentReference = db.document(Path);

        db.collection("Menu")
                .document(IDR)
                .collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(ID)) {
                                    x = (String) document.get("name");
                                    nom_category_textView.setText(x);
                                    nom_category_editText.setText(x);
                                    nom_category_editText.setFocusable(true);
                                    Log.d("Heyyyyy66FDFD ", "      > " + document.getId() + " => " + document.getData());
                                }
                            }
                        } else {
                            Toast.makeText(Edit_Category_List.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                            Log.d("Heyyyyy66", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return "xxx";
    }


    public void SupprimerAction(View view) {

        AlertDialog.Builder altdial = new AlertDialog.Builder(Edit_Category_List.this);
        altdial.setMessage("Etes Vous Sur De Supprimer La Categorie >>" + nom_category_textView.getText().toString() + "<< ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DocumentReference document = db.document(Path);
                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Edit_Category_List.this, "Suppression Success", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Edit_Category_List.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                                    }
                                });
                        //finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = altdial.create();
        alert.setTitle("Confirmation");
        alert.show();


    }

    public void ModifierAction(View view) {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Log.d("Heyyyyy55", "succc");
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
                                Toast.makeText(Edit_Category_List.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_Category_List.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            connected = false;
            Toast.makeText(Edit_Category_List.this, "Connexion Internet Limiter", Toast.LENGTH_LONG).show();
            Log.d("Heyyyyy55", "fail");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
