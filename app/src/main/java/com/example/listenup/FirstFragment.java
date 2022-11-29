package com.example.listenup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.events.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment implements RecyclerViewClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
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

    TextView greet;
    RecyclerView trendingRecycleview, popularRecycleview, recommendRecycleview;
    recycleFirebaseAdapter recycleAdapter1, recycleAdapter2, recycleAdapter3;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        greet = view.findViewById(R.id.greet);
        trendingRecycleview = view.findViewById(R.id.trending);
        popularRecycleview = view.findViewById(R.id.popular);
        recommendRecycleview = view.findViewById(R.id.recommend);

        Greeting();

        trendingRecycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Data> options1 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("trending"), Data.class)
                        .build();

        recycleAdapter1 = new recycleFirebaseAdapter(options1, this, "trending", "mainAdapter");
        trendingRecycleview.setAdapter(recycleAdapter1);

        popularRecycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Data> options2 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("popular"), Data.class)
                        .build();

        recycleAdapter2 = new recycleFirebaseAdapter(options2, this, "popular", "mainAdapter");
        popularRecycleview.setAdapter(recycleAdapter2);

        recommendRecycleview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Data> options3 =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("recommend"), Data.class)
                        .build();

        recycleAdapter3 = new recycleFirebaseAdapter(options3, this, "recommend", "mainAdapter");
        recommendRecycleview.setAdapter(recycleAdapter3);

        return view;
    }

    private void Greeting() {
        String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        int Time = Integer.parseInt(currentTime);

        if (Time < 12) {
            greet.setText("Good Morning");
        } else if (Time <= 24) {
            greet.setText("Good Evening");
        }
    }


    @Override
    public void onItemClick(int position, String type, DataSnapshot dataSnapshot) {
        Intent intent = new Intent(getContext(), Player.class);
        Data ss = dataSnapshot.getValue(Data.class);
        intent.putExtra("type", type);
        intent.putExtra("name", ss.getName());
        intent.putExtra("image", ss.getImage());
        intent.putExtra("song", ss.getSong());

        if (type.equals("trending")){
            intent.putExtra("itemCount", String.valueOf(trendingRecycleview.getAdapter().getItemCount()));
        }
        else if (type.equals("popular")){
            intent.putExtra("itemCount", popularRecycleview.getAdapter().getItemCount());
        }
        else if (type.equals("recommend")){
            intent.putExtra("itemCount", recommendRecycleview.getAdapter().getItemCount());
        }

        intent.putExtra("id", String.valueOf(position));
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        recycleAdapter1.startListening();
        recycleAdapter2.startListening();
        recycleAdapter3.startListening();
    }
}