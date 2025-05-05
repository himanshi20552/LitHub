package com.example.LitHub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BookmarksActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);

        // Initialize drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Menu icon click to open drawer
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookmarks) {
                // Already in bookmarks, just close drawer
            } else if (id == R.id.nav_turnitin) {
                startActivity(new Intent(this, TurnitinActivity.class));
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                finishAffinity();
                System.exit(0);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
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

        findViewById(R.id.nav_practice).setOnClickListener(v -> {
            startActivity(new Intent(this, PracticeMainActivity.class));
            overridePendingTransition(0, 0);
        });
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