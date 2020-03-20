package com.example.magasinadmin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Menu_Adapter extends FirestoreRecyclerAdapter<Menu, Menu_Adapter.MenuHolder> {
    private OnItemClickListner listner;

    public Menu_Adapter(@NonNull FirestoreRecyclerOptions<Menu> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull Menu model) {
        holder.menu_name.setText(model.getName());
        holder.menu_detail.setText(model.getDetails());
        holder.menu_price.setText( (model.getPrix()) + " DA");
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        return new MenuHolder(v);
    }

    class MenuHolder extends RecyclerView.ViewHolder {
        TextView menu_name;
        TextView menu_detail;
        TextView menu_price;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = itemView.findViewById(R.id.Menu_Text_View_Title);
            menu_detail = itemView.findViewById(R.id.Menu_Text_View_Details);
            menu_price = itemView.findViewById(R.id.Prix_Text_View_Menu);

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    listner.onItemLongClick(getSnapshots().getSnapshot(position), getAdapterPosition(), v);
                    return true;
                }
            });
        }
    }


    //////ce code pour clicker sur list
    public interface OnItemClickListner {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);

        void onItemLongClick(DocumentSnapshot documentSnapshot, int position, View v);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }
}
