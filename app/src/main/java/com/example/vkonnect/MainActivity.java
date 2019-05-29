package com.example.vkonnect;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAcessorAdapter myTabsAcessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

       // mToolbar = (Toolbar) findViewById(R.id.main_page_tool);
       // mToolbar.inflateMenu(R.menu.menu_options);

       // setSupportActionBar(mToolbar);

        //getSupportActionBar().setTitle("VKonnect");



        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAcessorAdapter = new TabsAcessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAcessorAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {

            sendUserToLoginPage();
        }
    }

    private void sendUserToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_options,menu);

       // setSupportActionBar(mToolbar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            sendUserToLoginPage();
            return true;
        }
        if (item.getItemId() == R.id.settings) {
            return true;

        }
        if (item.getItemId() == R.id.find_friends_option) {
            return true;

        }

        return super.onOptionsItemSelected(item);

    }
}