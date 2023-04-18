package com.example.petshopp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.petshopp.R;
import com.example.petshopp.fragment.CategoryFragment;
import com.example.petshopp.fragment.CommunityFragment;
import com.example.petshopp.fragment.HomeFragment;
import com.example.petshopp.fragment.NotificationFragment;
import com.example.petshopp.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mBottomNAV;
    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    CommunityFragment communityFragment = new CommunityFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    UserFragment userFragment =new UserFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNAV = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        mBottomNAV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.category:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,categoryFragment).commit();
                        return true;
                    case R.id.group:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,communityFragment).commit();
                        return true;
                    case R.id.noti:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                        return true;
                    case R.id.user:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,userFragment).commit();
                        return true;

                }
                return false;
            }
        });
    }
}