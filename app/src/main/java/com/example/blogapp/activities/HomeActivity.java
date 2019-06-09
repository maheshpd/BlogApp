package com.example.blogapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.example.blogapp.fragment.HomeFragment;
import com.example.blogapp.fragment.ProfileFragment;
import com.example.blogapp.fragment.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    Dialog popAddPost;
    ImageView popupPostImage,popupAddBtn;
    EditText popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    CircleImageView popupUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //init popup
        initPopup();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              popAddPost.show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Home");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        updateNavHeader();
    }

    private void initPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //init popup widgets
        popupUser = popAddPost.findViewById(R.id.popupUserImage);
        popupPostImage = popAddPost.findViewById(R.id.popupPostImage);
        popupAddBtn = popAddPost.findViewById(R.id.popUpbtn);
        popupTitle = popAddPost.findViewById(R.id.popupTitle);
        popupDescription = popAddPost.findViewById(R.id.popupDescription);
        popupClickProgress = popAddPost.findViewById(R.id.popupProgressBar);

        //load current user profile photo
        Glide.with(this).load(currentUser.getPhotoUrl()).into(popupUser);

        //Add post click Listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
         getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        } else if (id == R.id.profile) {
            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
        } else if (id == R.id.settings) {
            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingFragment()).commit();
        } else if (id == R.id.nav_singout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.name);
        TextView navUserEmail = headerView.findViewById(R.id.email);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_pohoto);

        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}
