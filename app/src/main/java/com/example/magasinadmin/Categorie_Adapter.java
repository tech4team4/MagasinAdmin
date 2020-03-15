package com.example.magasinadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Categorie_Adapter extends FirestoreRecyclerAdapter<Model_Category, Categorie_Adapter.CategorieHolder> {
    private OnItemClickListner listner;

    public Categorie_Adapter(@NonNull FirestoreRecyclerOptions<Model_Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategorieHolder holder, int position, @NonNull Model_Category model) {
        holder.categorieTextView.setText(model.getName());
    }

    @NonNull
    @Override
    public CategorieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);
        return new CategorieHolder(v);
    }

    /*------------------------------------Supprimer une list item------------------------*/
    public void delete_Item(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class CategorieHolder extends RecyclerView.ViewHolder {
        TextView categorieTextView;
        public CategorieHolder(@NonNull View itemView) {
            super(itemView);
            categorieTextView = itemView.findViewById(R.id.list_name_category);

            /////ce code pour click listner list
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner != null) {
                        listner.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
            ///
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner != null) {
                        listner.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    //////ce code pour clicker sur list
    public interface OnItemClickListner {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }
}
