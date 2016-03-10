package com.example.mypc.myapplication;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {

    UserLocalStore userLocalStore;
    //EditText username;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private android.support.v7.app.ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private String[] osArray;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    private void addDrawerItems() {
        osArray = new String[]{"Update Skills", "Find Trainings", "Upload Webcast", "Review Trainer", "Arrange a meetup", "Logout" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        navList.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);
        //username = (EditText) findViewById(R.id.dispUsername);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        navList.setOnItemClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.opendrawer,R.string.closedrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);

            }

        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings)
            return true;
        else  if( id == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(navList)) {
                drawerLayout.closeDrawer(navList);
            } else {
                drawerLayout.openDrawer(navList);
            }
        }
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override

    protected void onStart() {
        super.onStart();

        if (authenticate() == true) {
            displayUserDetails();
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails() {
        User loggedInUser = userLocalStore.getLoggedInUser();
        //username.setText(loggedInUser.name);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectItem(position);
        drawerLayout.closeDrawer(navList);
        switch (position) {
            case 0:
                Skills skillFragment = new Skills();
                fragmentTransaction.replace(R.id.maincontent,skillFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                startActivity(new Intent(this, Login.class));
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                break;
        }
    }
    private void selectItem(int position) {

        navList.setItemChecked(position, true);
        setTitle(osArray[position]);

    }
    @Override
    public void setTitle(CharSequence title) {

        actionBar.setTitle(title);

    }

}

