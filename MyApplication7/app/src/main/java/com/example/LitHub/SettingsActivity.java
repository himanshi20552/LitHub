package com.example.LitHub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView displayNameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Menu icon click to open drawer
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Handle navigation drawer clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookmarks) {
                startActivity(new Intent(this, BookmarksActivity.class));
            } else if (id == R.id.nav_turnitin) {
                startActivity(new Intent(this, TurnitinActivity.class));
            } else if (id == R.id.nav_settings) {
                // Already in settings
            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Bottom Navigation
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

        findViewById(R.id.nav_practice).setOnClickListener(v -> {
            startActivity(new Intent(this, PracticeMainActivity.class));
            overridePendingTransition(0, 0);
        });

        // Firebase: Set user info
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        displayNameTextView = findViewById(R.id.display_name);
        emailTextView = findViewById(R.id.email);

        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            if (name != null && !name.isEmpty()) {
                displayNameTextView.setText(name);
            } else {
                displayNameTextView.setText("User");
            }

            emailTextView.setText(email != null ? email : "Email not found");
        } else {
            displayNameTextView.setText("Not logged in");
            emailTextView.setText("");
        }
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
