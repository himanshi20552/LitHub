package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class CabinBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabinbooking);

        // Initialize views
        Button cabin1BookBtn = findViewById(R.id.cabin_1_book_btn);
        Button cabin2BookBtn = findViewById(R.id.cabin_2_book_btn);
        RadioGroup toggleGroup = findViewById(R.id.toggle_group);
        View menuIcon = findViewById(R.id.menu_icon);

        // Cabin booking buttons
        cabin1BookBtn.setOnClickListener(v -> bookCabin(1));
        cabin2BookBtn.setOnClickListener(v -> bookCabin(2));

        // Toggle between Cabin Booking and Virtual Groups
        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.virtual_groups_tab) {
                // Switch to Virtual Groups view
                startActivity(new Intent(this, VirtualGroupsActivity.class));
                overridePendingTransition(0, 0); // Remove transition animation
            }
            // Stay on current activity if Cabin Booking is selected
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

        // Menu icon opens the navigation drawer
        menuIcon.setOnClickListener(v -> {
            // If using same MainActivity drawer, you may need to implement this differently
            // For now launching MainActivity which has the drawer
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void bookCabin(int cabinNumber) {
        // Implement your cabin booking logic here
        // Example: Show confirmation dialog
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
        super.onBackPressed();
        overridePendingTransition(0, 0); // Remove back transition animation
    }
}