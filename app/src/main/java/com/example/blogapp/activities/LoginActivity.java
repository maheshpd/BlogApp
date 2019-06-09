package com.example.blogapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blogapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.loginEmail);
        userPassword = findViewById(R.id.loginpassword);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.loginProgress);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                String mail = userMail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify all field");
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }else {
                    signIn(mail,password);
                }
            }
        });
    }

    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    updateUI();
                }else {
                    String message = task.getException().getMessage().toString();
                    showMessage(message);
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void updateUI() {
        HomeActivity = new Intent(this,HomeActivity.class);
        startActivity(HomeActivity);
        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user != null) {
            //User is already connected so we need to redirect him to
            updateUI();
        }
    }
}
