package com.example.listenup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class recycleFirebaseAdapter extends FirebaseRecyclerAdapter<Data, recycleFirebaseAdapter.recycleViewHolder> {

    private RecyclerViewClickInterface recyclerViewClickInterface;

    String type, adapterType;

    public recycleFirebaseAdapter(@NonNull FirebaseRecyclerOptions<Data> options, RecyclerViewClickInterface recyclerViewClickInterface, String type, String adapterType) {
        super(options);
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.type = type;
        this.adapterType = adapterType;
    }

    @Override
    protected void onBindViewHolder(@NonNull recycleViewHolder holder, int position, @NonNull Data model) {
        holder.name.setText(model.getName());
        Glide.with(holder.icon.getContext()).load(model.getImage()).into(holder.icon);
    }

    @NonNull
    @Override
    public recycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view;
        if (adapterType.equals("mainAdapter")){
            view = inflater.inflate(R.layout.songadapter, parent, false);
        }
        else {
            view = inflater.inflate(R.layout.search_adapter, parent, false);
        }
        return new recycleViewHolder(view);
    }

    class recycleViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name;

            public recycleViewHolder(@NonNull View itemView) {
            super(itemView);
                icon = itemView.findViewById(R.id.songImage);
                name = itemView.findViewById(R.id.songName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        recyclerViewClickInterface.onItemClick(position, type, getSnapshots().getSnapshot(position));
                    }
                });
        }
    }
}
