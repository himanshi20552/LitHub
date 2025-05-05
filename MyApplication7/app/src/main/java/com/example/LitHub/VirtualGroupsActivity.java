package com.example.LitHub;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.android.material.navigation.NavigationView;

public class VirtualGroupsActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtualgroups);


        // Initialize views
        View createSessionCard = findViewById(R.id.create_session_card);
        View joinSessionCard = findViewById(R.id.join_session_card);
        View tabCabin = findViewById(R.id.cabin_booking_tab);

        // Set click listeners - Create goes directly to new meeting, Join shows dialog
        createSessionCard.setOnClickListener(v -> openGoogleMeet("https://meet.google.com/new"));
        joinSessionCard.setOnClickListener(v -> showJoinDialog());

        // Tab switch listener
        tabCabin.setOnClickListener(v -> {
            startActivity(new Intent(this, CabinBookingActivity.class));
            overridePendingTransition(0, 0);
        });

        // Menu icon click to open drawer
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // Initialize drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView displayNameTextView = headerView.findViewById(R.id.display_name);
        TextView emailTextView = headerView.findViewById(R.id.email);

        // Fetch from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();

            FirebaseFirestore.getInstance().collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String emailFetched = documentSnapshot.getString("email");

                            displayNameTextView.setText(name != null ? name : "User");
                            emailTextView.setText(emailFetched != null ? emailFetched : "No email");
                        }
                    })
                    .addOnFailureListener(e -> {
                        displayNameTextView.setText("Error");
                        emailTextView.setText("Error");
                    });

        }
        // Handle sidebar menu item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookmarks) {
                startActivity(new Intent(this, BookmarksActivity.class));
            } else if (id == R.id.nav_turnitin) {
                startActivity(new Intent(this, TurnitinActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                // Handle logout
                FirebaseAuth.getInstance().signOut(); // Sign out the user
                Intent intent = new Intent(VirtualGroupsActivity.this, MainActivity.class); // Redirect to Login Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear task stack
                startActivity(intent);
                finish(); // Finish current activity
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

    private void showJoinDialog() {
        EditText input = new EditText(this);
        input.setHint("abc-defg-hij");

        new AlertDialog.Builder(this)
                .setTitle("Join Meeting")
                .setMessage("Enter meeting code:")
                .setView(input)
                .setPositiveButton("Join", (d, w) -> {
                    String code = input.getText().toString().trim();
                    if (!code.isEmpty()) {
                        openGoogleMeet("https://meet.google.com/" + code);
                    } else {
                        Toast.makeText(this, "Please enter a meeting code", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openGoogleMeet(String url) {
        try {
            // Try to open Google Meet app directly
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.google.android.apps.meetings");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fallback to browser if app not installed
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
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