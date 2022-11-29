package com.example.listenup;

import com.google.firebase.database.DataSnapshot;

public interface RecyclerViewClickInterface {
    void onItemClick(int position, String type, DataSnapshot dataSnapshot);
}
