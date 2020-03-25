package com.example.kanbanlist;

import android.content.Intent;
import android.os.Bundle;

import com.example.kanbanlist.ui.teamBoards.TeamBoardsFragment;
import com.example.kanbanlist.ui.personalBoards.PersonalBoardsFragment;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Personal Boards");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PersonalBoardsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_personal_board);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_personal_board:
                toolbar.setTitle("Personal Boards");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PersonalBoardsFragment()).commit();
                break;
            case R.id.nav_team_board:
                toolbar.setTitle("Team Boards");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TeamBoardsFragment()).commit();
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
