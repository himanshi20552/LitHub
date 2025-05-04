package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        cabin1BookBtn.setOnClickListener(v -> showBookingDialog());
        cabin2BookBtn.setOnClickListener(v -> showBookingDialog());



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
    public void showBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.booking_form_dialog, null);
        builder.setView(dialogView);

        EditText en1 = dialogView.findViewById(R.id.en1);
        EditText en2 = dialogView.findViewById(R.id.en2);
        EditText en3 = dialogView.findViewById(R.id.en3);
        EditText en4 = dialogView.findViewById(R.id.en4);
        EditText ts = dialogView.findViewById(R.id.ts);

        Button btnSubmit = dialogView.findViewById(R.id.submit_booking_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(v -> {
            String enr1 = en1.getText().toString().trim();
            String enr2 = en2.getText().toString().trim();
            String enr3 = en3.getText().toString().trim();
            String enr4 = en4.getText().toString().trim();
            String timeslot = ts.getText().toString().trim();


            if (enr1.isEmpty() || enr2.isEmpty() || enr3.isEmpty()|| enr4.isEmpty()|| timeslot.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cabin booked successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}

