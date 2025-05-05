package com.example.LitHub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.LitHub.Homepage;

public class MainActivity extends AppCompatActivity {

    // Hardcoded credentials (demo purpose)
    private static final String VALID_EMAIL = "admin";
    private static final String VALID_PASSWORD = "123";

    EditText usernameInput, passwordInput;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // your XML file name

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInBtn = findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = usernameInput.getText().toString().trim();
                String inputPassword = passwordInput.getText().toString().trim();

                if (inputEmail.equals(VALID_EMAIL) && inputPassword.equals(VALID_PASSWORD)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Go to next screen
                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
