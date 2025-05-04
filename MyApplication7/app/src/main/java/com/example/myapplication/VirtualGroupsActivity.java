package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class VirtualGroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtualgroups);

        // Initialize views
        View createSessionCard = findViewById(R.id.create_session_card);
        View joinSessionCard = findViewById(R.id.join_session_card);
        View tabCabin = findViewById(R.id.tab_cabin);
        View menuIcon = findViewById(R.id.menu_icon);

        // Card click listeners

        // Tab switch listener
        tabCabin.setOnClickListener(v -> {
            startActivity(new Intent(this, CabinBookingActivity.class));
            overridePendingTransition(0, 0);
        });

        // Bottom Navigation Click Listeners
        findViewById(R.id.nav_resources).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        });

        findViewById(R.id.nav_collaborate).setOnClickListener(v -> {
            startActivity(new Intent(this, CollaborateActivity.class));
            overridePendingTransition(0, 0);
        });

        findViewById(R.id.nav_practice).setOnClickListener(v -> {
            startActivity(new Intent(this, PracticeMainActivity.class));
            overridePendingTransition(0, 0);
        });

        // Menu icon click (opens navigation drawer)
        menuIcon.setOnClickListener(v -> {
            // If using same MainActivity drawer, launch MainActivity
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0); // Remove back transition animation
    }
}