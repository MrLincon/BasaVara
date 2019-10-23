package com.example.basavara;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BasaAdapter extends FirestoreRecyclerAdapter<Basa, BasaAdapter.BasaHolder> {

    private OnItemClickListener listener;
    private Context mContext;

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
        TextView location,vara,address,details;

        public BasaHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            vara = itemView.findViewById(R.id.vara);
//            address = itemView.findViewById(R.id.vara);
//            details = itemView.findViewById(R.id.vara);

            mContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "Vaaa", Toast.LENGTH_SHORT).show();

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(DocumentSnapshot documentSnapshot);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
}
