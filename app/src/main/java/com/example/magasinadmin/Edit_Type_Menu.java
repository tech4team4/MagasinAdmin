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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Edit_Type_Menu extends AppCompatActivity {
    EditText nom_menu_editText;
    TextView nom_menu_textView;
    String Path = "", ID = "", IDR = "", x = "", NAME = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__type__menu);

        Path = getIntent().getStringExtra("PATH");
        ID = getIntent().getStringExtra("ID_Document");
        IDR = getIntent().getStringExtra("ID_Restaurant");
        NAME = getIntent().getStringExtra("NAME");
        mAuth = FirebaseAuth.getInstance();

        nom_menu_editText = findViewById(R.id.Nom_Type_Menu_editText);
        nom_menu_textView = findViewById(R.id.Nom_Type_Menu_textView);

        nom_menu_textView.setText("");
        nom_menu_textView.setText(NAME);
        nom_menu_editText.setText(NAME);
        nom_menu_editText.setFocusable(true);
    }


    public void SupprimerAction(View view) {
        AlertDialog.Builder altdial = new AlertDialog.Builder(Edit_Type_Menu.this);
        altdial.setMessage("Etes Vous Sur De Supprimer La Categorie >>" + nom_menu_textView.getText().toString() + "<< ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DocumentReference document = db.document(Path);
                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Edit_Type_Menu.this, "Suppression Success", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Edit_Type_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
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

    public void ModifierTypeAction(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Log.d("Heyyyyy55", "succc");
            String name = nom_menu_editText.getText().toString();
            String old = nom_menu_textView.getText().toString();
            if (name.equals("") || name.equals(null)) {
                nom_menu_editText.setError("Ecrire Une Categorie");
            } else {
                DocumentReference document = db.document(Path);
                document.update("name", name)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Edit_Type_Menu.this, "Modification Succés", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_Type_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            Toast.makeText(Edit_Type_Menu.this, "Connexion Internet Limiter", Toast.LENGTH_LONG).show();
            Log.d("Heyyyyy55", "fail");
        }
    }
}
