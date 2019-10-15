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
        holder.room.setText(model.getRoom());
        holder.varanda.setText(model.getVaranda());
        holder.toilet.setText(model.getToilet());
        holder.extra.setText(model.getExtra());
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
        TextView room;
        TextView varanda;
        TextView toilet;
        TextView extra;
        TextView vara;

        public BasaHolder(View itemView) {
            super(itemView);
            room = itemView.findViewById(R.id.room);
            varanda = itemView.findViewById(R.id.varanda);
            toilet = itemView.findViewById(R.id.toilet);
            extra = itemView.findViewById(R.id.extra);
            vara = itemView.findViewById(R.id.vara);
        }
    }
}
