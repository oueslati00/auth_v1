package com.example.auth_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Registre extends AppCompatActivity implements View.OnClickListener{

    private Button back,signup;
    private EditText username,password,email,cin,phone_number,confirm_password;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
         back=findViewById(R.id.return_btn);
         signup =findViewById(R.id.registre);
         username=findViewById(R.id.name_input);
         password=findViewById(R.id.password_input);
         confirm_password=findViewById(R.id.confirm_password_input);
         email= findViewById(R.id.email_input);
         cin=findViewById(R.id.cin_input);
         phone_number=findViewById(R.id.editTextPhone);
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case  R.id.registre:
                registre();
                break;
            case R.id.return_btn:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void registre() {
        String email_content= email.getText().toString().trim();
        String password_content = password.getText().toString().trim();
        String username_content = username.getText().toString().trim();
        String phonenumber_content = phone_number.getText().toString().trim();
        String cin_content = cin.getText().toString().trim();
        String confirmPassword_content = confirm_password.getText().toString().trim();
        System.out.println("hello word");
        if(username_content.isEmpty()){
            username.setError("Full name is required!");
            username.requestFocus();
            return;
        }
        if(phonenumber_content.isEmpty()){
            phone_number.setError("Age is required!");
            phone_number.requestFocus();
            return;
        }
        if(cin_content.isEmpty()){
            cin.setError("Email is required!");
            cin.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_content).matches()){
            email.setError("Please Provid a valid email!");
            email.requestFocus();
            return;
        }
        if(password_content.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(password_content.length() < 6 ){
            password.setError("Password should be more then 6 char !!");
            password.requestFocus();
            return;
        }
        if(confirmPassword_content.isEmpty()){
            confirm_password.setError("Password is required!");
            confirm_password.requestFocus();
            return;
        }
        if(!password_content.equals(confirmPassword_content) ){
            confirm_password.setError("Not Match");
            confirm_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_content,password_content)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username_content,email_content,cin_content,phonenumber_content);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete( Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Registre.this, "User has been registered sussesful ", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                        // redirect to login directory
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.sendEmailVerification();
                                        Toast.makeText(Registre.this, "check your email to verified your accout !", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(Registre.this, "Failed to registere try agin", Toast.LENGTH_LONG).show();
                                         progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });
    }
}