package com.example.auth_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView signup,forgetPassword;
    private EditText editTextEmail,editTextPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup= findViewById(R.id.signup_link);
        signup.setOnClickListener(this);
        forgetPassword = findViewById(R.id.forget_password_link);
        forgetPassword.setOnClickListener(this);
        editTextEmail = findViewById(R.id.user_name);
        editTextPassword = findViewById(R.id.password_user);
        login = findViewById(R.id.login_btn);
        login.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn:
                userlogin();
                break;
            case R.id.signup_link:
                startActivity(new Intent(this,Registre.class));
                break;
            case R.id.forget_password_link:
                startActivity(new Intent(this,ForgetPassword.class));
        }
    }
    private void userlogin() {
        String email = editTextEmail.getText().toString().trim();
        String password =editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required !");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Min password length is  6 characteres!");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
     mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this,ProfilActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "check your email to verified your accout !", Toast.LENGTH_LONG).show();
                    }
                        //redirect to user profil
                        startActivity(new Intent(MainActivity.this,ProfilActivity.class));
                    }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login ! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}