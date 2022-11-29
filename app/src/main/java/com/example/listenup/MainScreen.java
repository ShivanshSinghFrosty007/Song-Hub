package com.example.listenup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainScreen extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentAdapter adapter;

    TabLayout tabLayout;

    long mBack;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());

        viewPager.setAdapter(adapter);

        viewPager.setUserInputEnabled(false);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.library));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBack + 2000 > System.currentTimeMillis()){
            toast.cancel();
            super.onBackPressed();
        }
        else
        {
            tabLayout.selectTab(tabLayout.getTabAt(0));
            toast = Toast.makeText(getApplicationContext(),"Press again to exit", Toast.LENGTH_SHORT);
            toast.show();
            mBack = System.currentTimeMillis();
        }

    }
}