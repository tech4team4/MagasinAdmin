package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ////code pour afficher pop up menu
    LinearLayout L1, L2;
    Button b, accept;
    Dialog epicDialog;

    //////code
    private RecyclerView mFirestoreList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference categorieRef;
    private Categorie_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mFirestoreList = findViewById(R.id.firestore_list);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getCollectionID();


        ////dialog pop up
        epicDialog = new Dialog(this);
        setUpRecyclerView();
    }//end onCreate()

    String id = "";

    public void getCollectionID() {
        db.collection("Menu")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = document.getId();
                                Log.d("Heyyyyy66TEssst ", "      > " + document.getId() + " => " + document.getData());
                            }
                            categorieRef = db.collection("Menu")
                                    .document(id)
                                    .collection("category");
                        } else {
                            Log.d("Heyyyyy66", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void setUpRecyclerView() {
        Query query = categorieRef.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Model_Category> options = new FirestoreRecyclerOptions.Builder<Model_Category>()
                .setQuery(query, Model_Category.class)
                .build();

        adapter = new Categorie_Adapter(options);
        RecyclerView recyclerView = findViewById(R.id.firestore_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ///////action pour Swip Supprimer
        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.delete_Item(viewHolder.getAdapterPosition());
            }

        }).attachToRecyclerView(recyclerView);
        */

        adapter.setOnItemClickListner(new Categorie_Adapter.OnItemClickListner() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Categorie_List list = documentSnapshot.toObject(Categorie_List.class);
                String path = documentSnapshot.getReference().getPath();
                String id = documentSnapshot.getId();
                //DocumentReference ref = documentSnapshot.getReference();
                Toast.makeText(ListActivity.this, "position " + position +
                        "\nid=" + id +
                        "\ndoc=" + path, Toast.LENGTH_LONG).show();

                //////cliquer sur item et lancer la modification ou bien suppresion
                Intent intent1 = new Intent(ListActivity.this, Edit_Category_List.class);
                intent1.putExtra("PATH", path);
                intent1.putExtra("ID_Document", id);
                startActivity(intent1);
                finish();
                //ShowPopUp();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void Ajouter_categorie(View view) {
        Intent intent1 = new Intent(ListActivity.this, Ajouter_Categorie.class);
        startActivity(intent1);
        finish();
    }


}
