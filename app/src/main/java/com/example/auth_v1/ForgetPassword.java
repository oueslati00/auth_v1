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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText email_reset;
    private Button reset_btn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email_reset = findViewById(R.id.email_reset);
        reset_btn = findViewById(R.id.reset_btn);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        String email = email_reset.getText().toString().trim();

        if (email.isEmpty()) {
            email_reset.setError("email is request");
            email_reset.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_reset.setError("Please enter a valid email!");
            email_reset.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "check your email to reset your password! ", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ForgetPassword.this, "try again! Something wrong happened!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn:
                resetPassword();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}