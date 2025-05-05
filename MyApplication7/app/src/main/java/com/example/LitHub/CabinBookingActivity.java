package com.example.LitHub;

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

public class CabinBookingActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private List<Cabin> cabinList = new ArrayList<>();
    private CabinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabinbooking);

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
                Intent intent = new Intent(CabinBookingActivity.this, MainActivity.class); // Redirect to Login Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear task stack
                startActivity(intent);
                finish(); // Finish current activity
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Initialize views
        RadioGroup toggleGroup = findViewById(R.id.toggle_group);
        recyclerView = findViewById(R.id.recyclerViewCabins);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CabinAdapter(cabinList, this);
        recyclerView.setAdapter(adapter);

        loadCabinsFromFirebase();

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
    private void loadCabinsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cabins");
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    cabinList.clear();
                    System.out.println("Firebase data received. Number of children: " + snapshot.getChildrenCount());
                    
                    for (DataSnapshot data : snapshot.getChildren()) {
                        try {
                            System.out.println("Processing cabin: " + data.getKey());
                            Cabin cabin = new Cabin();
                            cabin.name = data.child("name").getValue(String.class);
                            Object capacityObj = data.child("capacity").getValue();
                            cabin.setCapacity(capacityObj);
                            cabin.isBooked = Boolean.TRUE.equals(data.child("isBooked").getValue(Boolean.class));
                            
                            // Check if cabin is booked and has bookings
                            if (cabin.isBooked && data.child("bookings").exists()) {
                                boolean shouldBeAvailable = true;
                                // Check each booking's end time
                                for (DataSnapshot bookingSnapshot : data.child("bookings").getChildren()) {
                                    String endTimeStr = bookingSnapshot.child("endTime").getValue(String.class);
                                    if (endTimeStr != null) {
                                        // Parse the end time (format: "h:mm AM/PM")
                                        String[] timeParts = endTimeStr.split(" ");
                                        String[] hourMin = timeParts[0].split(":");
                                        int hour = Integer.parseInt(hourMin[0]);
                                        int minute = Integer.parseInt(hourMin[1]);
                                        boolean isPM = timeParts[1].equals("PM");
                                        
                                        // Convert to 24-hour format
                                        if (isPM && hour != 12) hour += 12;
                                        if (!isPM && hour == 12) hour = 0;
                                        
                                        // Get current time
                                        java.util.Calendar now = java.util.Calendar.getInstance();
                                        int currentHour = now.get(java.util.Calendar.HOUR_OF_DAY);
                                        int currentMinute = now.get(java.util.Calendar.MINUTE);
                                        
                                        // Compare times
                                        if (hour > currentHour || (hour == currentHour && minute > currentMinute)) {
                                            shouldBeAvailable = false;
                                            break;
                                        }
                                    }
                                }
                                
                                // If all bookings have expired, update cabin availability
                                if (shouldBeAvailable) {
                                    DatabaseReference cabinRef = ref.child(data.getKey());
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("isBooked", false);
                                    cabinRef.updateChildren(updates);
                                    cabin.isBooked = false;
                                }
                            }
                            
                            if (cabin.name != null && cabin.capacity != null) {
                                System.out.println("Cabin loaded - Name: " + cabin.name + ", Capacity: " + cabin.capacity + ", Booked: " + cabin.isBooked);
                                if (!cabin.isBooked) {
                                    cabinList.add(cabin);
                                }
                            } else {
                                System.out.println("Failed to load cabin data for key: " + data.getKey() + " - Missing required fields");
                            }
                        } catch (Exception e) {
                            System.out.println("Error processing cabin " + data.getKey() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    
                    System.out.println("Total cabins loaded: " + cabinList.size());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    System.out.println("Error loading cabins: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Firebase error: " + error.getMessage());
                error.toException().printStackTrace();
            }
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

