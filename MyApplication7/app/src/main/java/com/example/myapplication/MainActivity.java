package com.example.myapplication; // Replace with your package name

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Menu icon click to open drawer
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Handle menu item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_bookmarks) {
                // Handle bookmarks (to be implemented)
            } else if (id == R.id.nav_turnitin) {
                // Launch Turnitin Activity
                startActivity(new Intent(MainActivity.this, TurnitinActivity.class));
            } else if (id == R.id.nav_settings) {
                // Handle settings (to be implemented)
            } else if (id == R.id.nav_logout) {
                // Handle logout (to be implemented)
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}