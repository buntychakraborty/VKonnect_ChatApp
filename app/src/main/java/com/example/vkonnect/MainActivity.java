package com.example.vkonnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAcessorAdapter myTabsAcessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAcessorAdapter = new TabsAcessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAcessorAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        rootRef= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {

            sendUserToLoginPage();
        }
        else{
            verifyUserExistence();
        }
    }

    private void verifyUserExistence() {

        String currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show(); //user has done all settings
                }

                else{
                    sendUserToSettingActivity();//If user has created his account and he needs to do system setup
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendUserToSettingActivity() {
        Intent settingIntent=new Intent(getApplicationContext(),SettingActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //cannot press back button to go back
        startActivity(settingIntent);
        finish();
    }

    private void sendUserToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //cannot press back button to go back
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
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
            sendUserToSettingActivity();
            return true;

        }
        if (item.getItemId() == R.id.find_friends_option) {
            return true;

        }

        if (item.getItemId() == R.id.groups) {

            requestNewGroup();


        }

        return super.onOptionsItemSelected(item);

    }

    //Creating Groups
    private void requestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name");
       final EditText gName=new EditText(getApplicationContext());
        gName.setHint("Example : AssiVelle24X7");

        gName.setGravity(Gravity.BOTTOM);
        gName.setTextColor(Color.BLACK);
        builder.setView(gName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName=gName.getText().toString();

                if(TextUtils.isEmpty(groupName)){


                    Toast.makeText(MainActivity.this,"Please write the group Name",Toast.LENGTH_SHORT).show(); //user has done all settings
                }
                else{
                    createNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        builder.show();
    }

    private void createNewGroup(final  String gname) {

        rootRef.child("Groups").child(gname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Group :"+gname+ " is created succesfully...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}