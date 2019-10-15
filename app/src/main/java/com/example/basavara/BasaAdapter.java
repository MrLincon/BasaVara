package com.example.basavara;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BasaAdapter extends FirestoreRecyclerAdapter<Basa, BasaAdapter.BasaHolder> {

    public BasaAdapter(@NonNull FirestoreRecyclerOptions<Basa> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BasaHolder holder, int position, @NonNull Basa model) {
        holder.location.setText(model.getLocation());
        holder.vara.setText(model.getVara());
    }

    @NonNull
    @Override
    public BasaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basa_card_layout,
                parent, false);
        return new BasaHolder(view);
    }

    class BasaHolder extends RecyclerView.ViewHolder {
        TextView location,vara;

        public BasaHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            vara = itemView.findViewById(R.id.vara);
        }
    }
}
