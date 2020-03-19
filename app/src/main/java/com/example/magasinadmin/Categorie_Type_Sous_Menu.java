package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.Query;

import java.util.Map;

public class Categorie_Type_Sous_Menu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private RecyclerView mFirestoreList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categorieRef;//=db.collection("/Menu/1txFn61xmmPfbZvyVgbu/category");
    String ID = "", IDR = "", PATH = "", TITLE = "";
    private TypeMenu_Adapter adapter;
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie__list__sous__menu);
        mFirestoreList = findViewById(R.id.recycler_view_type_menu);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        txv = findViewById(R.id.Text_View_Sous_Menu);
        ////get Intent Extras
        ID = getIntent().getStringExtra("ID_Document");
        IDR = getIntent().getStringExtra("ID_Restaurant");
        PATH = getIntent().getStringExtra("PATH");
        TITLE = getIntent().getStringExtra("Title");
        txv.setText("Les Types De " + TITLE);
        // categorieRef = db.collection("/Menu/1txFn61xmmPfbZvyVgbu/category/h1YdUw0sDJ6hde1JktEw/list");//path to pizza collection exemple

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        //Toast.makeText(Categorie_Type_Sous_Menu.this,"Path = "+PATH, Toast.LENGTH_LONG).show();
        //Query query = categorieRef.orderBy("name",Query.Direction.ASCENDING);
        Query query = db.document(PATH).collection("list");
        FirestoreRecyclerOptions<Type> options = new FirestoreRecyclerOptions.Builder<Type>()
                .setQuery(query, Type.class)
                .build();
        adapter = new TypeMenu_Adapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_type_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /////action de click sur item recycler viewer
        adapter.setOnItemClickListner(new TypeMenu_Adapter.OnItemClickListner() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Categorie_List list = documentSnapshot.toObject(Categorie_List.class);
                /*String path = documentSnapshot.getReference().getPath();
                String id = documentSnapshot.getId();
                String title = "";
                title = (String) documentSnapshot.get("name");
                //DocumentReference ref = documentSnapshot.getReference();

                //////cliquer sur item et lancer la modification ou bien suppresion
                Intent intent1 = new Intent(ListActivity.this, Categorie_Type_Sous_Menu.class);
                intent1.putExtra("PATH", path);
                intent1.putExtra("ID_Document", id);
                intent1.putExtra("ID_Restaurant", idR);
                intent1.putExtra("Title", title);
                startActivity(intent1);
                //finish();
                //ShowPopUp();
                */

            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position, View v) {
                String path = documentSnapshot.getReference().getPath();
                String id = documentSnapshot.getId();
                ShowPopUpMenu(v, documentSnapshot);
            }
        });


    }

    public void ShowPopUpMenu(View v, final DocumentSnapshot documentSnapshot) {

        MenuBuilder menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popup_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_category_menu: // Handle option1 Click
                        Edit_Type(documentSnapshot);
                        return true;
                    case R.id.delete_category_menu: // Handle option2 Click
                        Delete_Type(documentSnapshot);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });

        optionsMenu.show();
    }


    public void Edit_Type(DocumentSnapshot documentSnapshot) {
        String path = documentSnapshot.getReference().getPath();
        String id = documentSnapshot.getId();
        String n = (String) documentSnapshot.get("name");
        Intent intent1 = new Intent(Categorie_Type_Sous_Menu.this, Edit_Type_Menu.class);
        intent1.putExtra("PATH", path);
        intent1.putExtra("ID_Document", id);
        intent1.putExtra("NAME", n);
        //intent1.putExtra("ID_Restaurant", idR);
        startActivity(intent1);
        //finish();
    }

    public void Delete_Type(DocumentSnapshot documentSnapshot) {
        final String path = documentSnapshot.getReference().getPath();
        String id = documentSnapshot.getId();
        AlertDialog.Builder altdial = new AlertDialog.Builder(Categorie_Type_Sous_Menu.this);
        altdial.setMessage("Etes Vous Sur De Supprimer La Categorie ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DocumentReference document = db.document(path);
                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Categorie_Type_Sous_Menu.this, "Suppression Success", Toast.LENGTH_LONG).show();
                                        //finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Categorie_Type_Sous_Menu.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
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


    public void Ajouter_sous_menu(View view) {
        Intent intent1 = new Intent(Categorie_Type_Sous_Menu.this, Ajouter_Categorie_Type_Menu.class);
        intent1.putExtra("PATH", PATH);
        startActivity(intent1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
