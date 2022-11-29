package com.example.listenup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements RecyclerViewClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    recycleFirebaseAdapter firebaseAdapter;

    SearchView searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = view.findViewById(R.id.search_recycle_view);
        searchText = view.findViewById(R.id.search);

        setData();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position, String type, DataSnapshot dataSnapshot) {
        Intent intent = new Intent(getContext(), Player.class);
        Data ss = dataSnapshot.getValue(Data.class);
        intent.putExtra("type", type);
        intent.putExtra("name", ss.getName());
        intent.putExtra("image", ss.getImage());
        intent.putExtra("song", ss.getSong());
        intent.putExtra("id", ss.getId());
        intent.putExtra("itemCount", recyclerView.getAdapter().getItemCount());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        firebaseAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        firebaseAdapter.stopListening();
        super.onStop();
    }

    private void setData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("search"), Data.class)
                        .build();

        firebaseAdapter = new recycleFirebaseAdapter(options, this, "search", "seaarchAdapter");
        recyclerView.setAdapter(firebaseAdapter);
        firebaseAdapter.startListening();
    }

    private void processSearch(String query) {

        String finalStr;

        if (query != null && !query.equals("")) {

            String firstLetStr = query.substring(0, 1);
            firstLetStr = firstLetStr.toUpperCase();
            String remLetStr = query.substring(1);
            finalStr = firstLetStr+remLetStr;
        }
        else {
            finalStr = query;
        }

        if (finalStr.equals("")){
            setData();
            return;
        }


        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("search")
                                .orderByChild("name").startAt(finalStr).endAt(finalStr + "\uf9ff"), Data.class)
                        .build();
        firebaseAdapter = new recycleFirebaseAdapter(options, this, "search", "seaarchAdapter");
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);

    }
}