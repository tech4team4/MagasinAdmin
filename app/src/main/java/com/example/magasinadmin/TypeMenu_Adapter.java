package com.example.magasinadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TypeMenu_Adapter extends FirestoreRecyclerAdapter<Type, TypeMenu_Adapter.TypeMenuHolder> {


    public TypeMenu_Adapter(@NonNull FirestoreRecyclerOptions<Type> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TypeMenuHolder holder, int position, @NonNull Type model) {
        holder.type_name.setText(model.getName());
    }

    @NonNull
    @Override
    public TypeMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category_type_menu,parent,false);
        return new TypeMenuHolder(v);
    }

    class TypeMenuHolder extends RecyclerView.ViewHolder{
        TextView type_name;
        public TypeMenuHolder(@NonNull View itemView) {
            super(itemView);
            type_name=itemView.findViewById(R.id.type_list_name_category);
        }
    }

}
