package com.example.vkonnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private Button register;
    private EditText userEmail, userPassword;
    private TextView alreadyHaveAnAccountLink;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        initializeFields();
        alreadyHaveAnAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount(userEmail.getText().toString(), userPassword.getText().toString());
            }
        });

    }

    private void createNewAccount(final String email, final String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait, while we are creating an account for you...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        String currentUserId=mAuth.getCurrentUser().getUid();
                        rootRef.child("Users").child(currentUserId).setValue("");

                        sendUserToMainActivity();
                        Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    } else {
                        String errorMessage = task.getException().toString();
                        Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //cannot press back button to go back
        startActivity(intent);
        finish();
    }

    private void sendUserToLoginActivity(String email, String password) {
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivityForResult(intent,1);
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }



    private void initializeFields() {
        register = (Button) findViewById(R.id.regis_button);
        userEmail = (EditText) findViewById(R.id.regis_email);
        userPassword = (EditText) findViewById(R.id.regis_pwd);
        alreadyHaveAnAccountLink = (TextView) findViewById(R.id.already_have_an_account);
        loadingBar = new ProgressDialog(this);
    }
}
