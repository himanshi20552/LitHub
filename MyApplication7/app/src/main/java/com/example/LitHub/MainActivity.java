package com.example.LitHub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button signInBtn, signUpBtn;
    private FirebaseAuth mAuth;

    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        signInBtn.setOnClickListener(v -> loginUser());
        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });

        passwordInput.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2; // drawableEnd index

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordInput.getRight() -
                        passwordInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    if (isPasswordVisible) {
                        // Hide password
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass_ic, 0, R.drawable.eyes, 0);
                        isPasswordVisible = false;
                    } else {
                        // Show password
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass_ic, 0, R.drawable.eyes_closed, 0);
                        isPasswordVisible = true;
                    }

                    // Keep cursor at end
                    passwordInput.setSelection(passwordInput.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    private void loginUser() {
        String email = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(MainActivity.this, Homepage.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String email = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(MainActivity.this, Homepage.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, Homepage.class));
            finish();
        }
    }
}
