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
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MenuDeTypeX extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private RecyclerView mFirestoreList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categorieRef;//=db.collection("/Menu/1txFn61xmmPfbZvyVgbu/category");
    String ID = "", IDR = "", PATH = "", TYPE = "", CATGORY = "";
    ////change this
    private Menu_Adapter adapter;
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_de_type_x);

        mFirestoreList = findViewById(R.id.recycler_view_menu);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        txv = findViewById(R.id.Text_View_Menu_item);
        ////get Intent Extras
        ID = getIntent().getStringExtra("ID_Document");
        //IDR = getIntent().getStringExtra("ID_Restaurant");
        PATH = getIntent().getStringExtra("PATH");
        TYPE = getIntent().getStringExtra("Title");
        CATGORY = getIntent().getStringExtra("Category");
        txv.setText("Liste De " + CATGORY + " " + TYPE);
        //Toast.makeText(MenuDeTypeX.this, "path=" + PATH, Toast.LENGTH_LONG).show();
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        Query query = db
                .collection("/Menu/MRKv8uO1uwAg7XHgqHfe/category/xZKnPaWbOAFLesGfCbz1/list/puy06c21bu2JmxhDVu1x/list_menu")
                .orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Menu> options = new FirestoreRecyclerOptions.Builder<Menu>()
                .setQuery(query, Menu.class)
                .build();
        adapter = new Menu_Adapter(options);
        ////---------------------------------------change ici
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /////action de click sur item recycler viewer + change ici
        adapter.setOnItemClickListner(new Menu_Adapter.OnItemClickListner() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Categorie_List list = documentSnapshot.toObject(Categorie_List.class);
                /*String path = documentSnapshot.getReference().getPath();
                String id = documentSnapshot.getId();
                String title = "";
                title = (String) documentSnapshot.get("name");
                //DocumentReference ref = documentSnapshot.getReference();
                Toast.makeText(MenuDeTypeX.this, "path=" + PATH, Toast.LENGTH_LONG).show();
                //////cliquer sur item et lancer la modification ou bien suppresion
                Intent intent1 = new Intent(MenuDeTypeX.this, Ajouter_Menu.class);
                intent1.putExtra("PATH", path);
                intent1.putExtra("ID_Document", id);
                intent1.putExtra("Title", title);
                intent1.putExtra("Category", CATGORY);
                intent1.putExtra("TYPE", TYPE);
                intent1.putExtra("CAT_NAME", PATH);
                startActivity(intent1);*/
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
                        Edit_Menu(documentSnapshot);
                        return true;
                    case R.id.delete_category_menu: // Handle option2 Click
                        Delete_Menu(documentSnapshot);
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

    public void Edit_Menu(DocumentSnapshot documentSnapshot) {

        try {
            String path = documentSnapshot.getReference().getPath();
            String id = documentSnapshot.getId();
            ////////
            String name = (String) documentSnapshot.get("name");
            String details = (String) documentSnapshot.get("details");
            String prix = (String) String.valueOf(documentSnapshot.get("prix"));
            String nbr_viande = (String) String.valueOf(documentSnapshot.get("nbr_viande"));
            String choix = (String) String.valueOf(documentSnapshot.get("choix"));

            Intent intent1 = new Intent(MenuDeTypeX.this, Edit_Menu.class);

            intent1.putExtra("PATH", path);
            intent1.putExtra("ID_Document", id);
            intent1.putExtra("Category", CATGORY);
            intent1.putExtra("TYPE", TYPE);
            intent1.putExtra("CAT_NAME", PATH);

            intent1.putExtra("name", name);
            intent1.putExtra("details", details);
            intent1.putExtra("nbr_viande", nbr_viande);
            intent1.putExtra("prix", prix);
            intent1.putExtra("choix", choix);

            startActivity(intent1);
            //finish();
        } catch (Exception e) {
            Log.e("ERRRRRR", e.getMessage());
        }
        //String n = (String) documentSnapshot.get("name");

    }

    public void Delete_Menu(DocumentSnapshot documentSnapshot) {
        final String path = documentSnapshot.getReference().getPath();
        String id = documentSnapshot.getId();
        AlertDialog.Builder altdial = new AlertDialog.Builder(MenuDeTypeX.this);
        altdial.setMessage("Etes Vous Sur De Supprimer ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DocumentReference document = db.document(path);
                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MenuDeTypeX.this, "Suppression Success", Toast.LENGTH_LONG).show();
                                        //finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MenuDeTypeX.this, "Connexion Echoué", Toast.LENGTH_LONG).show();
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

    public void Ajouter_menu(View view) {
        Intent intent1 = new Intent(MenuDeTypeX.this, Ajouter_Menu.class);
        intent1.putExtra("Category", CATGORY);
        intent1.putExtra("TYPE", TYPE);
        intent1.putExtra("CAT_NAME", PATH);
        startActivity(intent1);
    }
}
