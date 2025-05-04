package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class PracticeMLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_ml);

        // Menu icon click (opens navigation drawer)
        findViewById(R.id.menu_icon).setOnClickListener(v -> {
            // If using same MainActivity drawer, launch MainActivity
            startActivity(new Intent(this, MainActivity.class));
        });

        // Bottom Navigation Click Listeners
        findViewById(R.id.nav_resources).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        });

        findViewById(R.id.nav_group_study).setOnClickListener(v -> {
            startActivity(new Intent(this, CabinBookingActivity.class));
            overridePendingTransition(0, 0);
        });

        findViewById(R.id.nav_collaborate).setOnClickListener(v -> {
            startActivity(new Intent(this, CollaborateActivity.class));
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0); // Remove back transition animation
    }
}