package com.example.magasinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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
    String ID = "", IDR = "", PATH = "",TITLE="";
    private TypeMenu_Adapter adapter;
    TextView txv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie__list__sous__menu);
        mFirestoreList = findViewById(R.id.recycler_view_type_menu);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        txv=findViewById(R.id.Text_View_Sous_Menu);
        ////get Intent Extras
        ID = getIntent().getStringExtra("ID_Document");
        IDR = getIntent().getStringExtra("ID_Restaurant");
        PATH = getIntent().getStringExtra("PATH");
        TITLE=getIntent().getStringExtra("Title");
        txv.setText("Les Types De "+TITLE);
       // categorieRef = db.collection("/Menu/1txFn61xmmPfbZvyVgbu/category/h1YdUw0sDJ6hde1JktEw/list");//path to pizza collection exemple

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        //Toast.makeText(Categorie_Type_Sous_Menu.this,"Path = "+PATH, Toast.LENGTH_LONG).show();
        //Query query = categorieRef.orderBy("name",Query.Direction.ASCENDING);
        Query query =db.document(PATH).collection("list");
        FirestoreRecyclerOptions<Type> options = new FirestoreRecyclerOptions.Builder<Type>()
                .setQuery(query, Type.class)
                .build();
        adapter = new TypeMenu_Adapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_type_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void Ajouter_sous_menu(View view) {
        Intent intent1 = new Intent(Categorie_Type_Sous_Menu.this, Ajouter_Categorie_Type_Menu.class);
        intent1.putExtra("PATH",PATH);
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
