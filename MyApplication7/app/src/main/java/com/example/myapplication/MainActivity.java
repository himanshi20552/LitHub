package com.example.myapplication;

import android.content.Intent; // Add this import
import android.os.Bundle;
import android.widget.LinearLayout; // Add this import

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change ID to match your bottom nav's Practice item
        LinearLayout practiceNav = findViewById(R.id.nav_practice);
        practiceNav.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PracticeMainActivity.class));
        });
    }
}