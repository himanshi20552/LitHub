package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class CabinBookingActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabinbooking);

        // Initialize drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Initialize views
        Button cabin1BookBtn = findViewById(R.id.cabin_1_book_btn);
        Button cabin2BookBtn = findViewById(R.id.cabin_2_book_btn);
        RadioGroup toggleGroup = findViewById(R.id.toggle_group);

        // Cabin booking buttons
        cabin1BookBtn.setOnClickListener(v -> bookCabin(1));
        cabin2BookBtn.setOnClickListener(v -> bookCabin(2));

        // Toggle between Cabin Booking and Virtual Groups
        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.virtual_groups_tab) {
                startActivity(new Intent(this, VirtualGroupsActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        // Menu icon click to open drawer
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookmarks) {
                // Handle bookmarks
            } else if (id == R.id.nav_turnitin) {
                startActivity(new Intent(this, TurnitinActivity.class));
            } else if (id == R.id.nav_settings) {
                // Handle settings
            } else if (id == R.id.nav_logout) {
                // Handle logout
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
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
    }

    private void bookCabin(int cabinNumber) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Book Cabin " + cabinNumber + "?")
                .setPositiveButton("Book", (dialog, which) -> {
                    // Handle successful booking
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(0, 0);
        }
    }
}