package com.l19cntt1b.milos;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.l19cntt1b.milos.Adapters.FragmentsAdapter;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    Button btnSignOut,btnDeleteAccount;
    FirebaseAuth auth;
    ViewPager viewPager;
    TabLayout tabLayoutMainMenu;
    RelativeLayout toolBar;
    ImageView imgSearch;
    TextView txtSearch;
    String authid;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AnhXa();

        authid=FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        tabLayoutMainMenu.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMainMenu));

        tabLayoutMainMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for(int i=0;i<5;i++){
                    tabLayoutMainMenu.getTabAt(i).getIcon().setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_IN);
                }
                tab.getIcon().setColorFilter(Color.parseColor("#1194FF"), PorterDuff.Mode.SRC_IN);
                if(tab.getId()==4){
                    toolBar.setVisibility(View.GONE);
                imgSearch.setVisibility(View.GONE);
                txtSearch.setVisibility(View.GONE);
                } else {
                    toolBar.setVisibility(View.VISIBLE);
                    imgSearch.setVisibility(View.VISIBLE);
                    txtSearch.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabIcons();
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this,FindFriendsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void AnhXa(){
        viewPager = (ViewPager) findViewById(R.id.viewpager_body);
        tabLayoutMainMenu = (TabLayout) findViewById(R.id.tablayout_main_menu);
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        toolBar = (RelativeLayout) findViewById(R.id.toolBar);
        imgSearch =(ImageView) findViewById(R.id.imgSearch);

    }
    private void setupTabIcons() {
        tabLayoutMainMenu.getTabAt(0).setIcon(R.drawable.ic_menu_messenger);
        tabLayoutMainMenu.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#1194FF"), PorterDuff.Mode.SRC_IN);
        tabLayoutMainMenu.getTabAt(1).setIcon(R.drawable.ic_menu_phonebook);
        tabLayoutMainMenu.getTabAt(2).setIcon(R.drawable.ic_menu_profile);
        tabLayoutMainMenu.getTabAt(3).setIcon(R.drawable.ic_menu_clock);
        tabLayoutMainMenu.getTabAt(4).setIcon(R.drawable.ic_menu_avatar);
        tabLayoutMainMenu.getTabAt(0).setId(0);
        tabLayoutMainMenu.getTabAt(1).setId(1);
        tabLayoutMainMenu.getTabAt(2).setId(2);
        tabLayoutMainMenu.getTabAt(3).setId(3);
        tabLayoutMainMenu.getTabAt(4).setId(4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("presence").child(authid).child("online").setValue(new Date().getTime());
    }

    @Override
    protected void onStop() {
        super.onStop();
        database.getReference().child("presence").child(authid).child("online").setValue(new Date().getTime());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.getReference().child("presence").child(authid).child("online").setValue(new Date().getTime());
    }

}
