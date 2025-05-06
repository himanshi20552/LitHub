package com.example.LitHub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

public class Homepage extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private List<Resource> booksList = new ArrayList<>();
    private List<Resource> pyqsList = new ArrayList<>();
    private List<Resource> notesList = new ArrayList<>();
    private ResourceAdapter booksAdapter;
    private ResourceAdapter pyqsAdapter;
    private ResourceAdapter notesAdapter;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        // Initialize RecyclerViews
        RecyclerView booksRecyclerView = findViewById(R.id.books_container);
        RecyclerView pyqsRecyclerView = findViewById(R.id.pyq_container);
        RecyclerView notesRecyclerView = findViewById(R.id.notes_container);

        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pyqsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        booksAdapter = new ResourceAdapter(booksList, this);
        pyqsAdapter = new ResourceAdapter(pyqsList, this);
        notesAdapter = new ResourceAdapter(notesList, this);

        booksRecyclerView.setAdapter(booksAdapter);
        pyqsRecyclerView.setAdapter(pyqsAdapter);
        notesRecyclerView.setAdapter(notesAdapter);

        // Initialize search bar
        searchBar = findViewById(R.id.search_bar);
        setupSearchBar();

        // Load resources
        loadResources();

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
                Intent intent = new Intent(Homepage.this, MainActivity.class); // Redirect to Login Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear task stack
                startActivity(intent);
                finish(); // Finish current activity
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });



        // Bottom Navigation Click Listeners
        findViewById(R.id.nav_resources).setOnClickListener(v -> {
            // Already in MainActivity
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

        TextView pyqHeader = findViewById(R.id.pyq_header);
        pyqHeader.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, PyqListActivity.class);
            startActivity(intent);
        });

        TextView booksHeader = findViewById(R.id.books_header);
        booksHeader.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, BookListActivity.class);
            startActivity(intent);
        });

        TextView notesHeader = findViewById(R.id.notes_header);
        notesHeader.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, NoteListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.filter_mad).setOnClickListener(v -> openFilteredResourceList("MAD"));
        findViewById(R.id.filter_seminar).setOnClickListener(v -> openFilteredResourceList("Seminar"));
        findViewById(R.id.filter_coa).setOnClickListener(v -> openFilteredResourceList("COA"));
        findViewById(R.id.filter_ml).setOnClickListener(v -> openFilteredResourceList("ML"));
    }

    private void loadResources() {
        // Sample resources - replace these URLs with actual PDF URLs
        List<Resource> allResources = new ArrayList<>();

        // Books
        allResources.add(new Resource(
                "Android Development Guide",
                "book",
                "https://github.com/himanshi20552/LitHub/blob/main/PDFs/notes/1.%20Mobile%20Application%20Development%20CSE3709.pdf",
                "MAD"
        ));
        allResources.add(new Resource(
                "Machine Learning Basics",
                "book",
                "https://www.cs.cmu.edu/~tom/mlbook.pdf",
                "ML"
        ));
        // Add more books here
        allResources.add(new Resource(
                "Data Structures and Algorithms",
                "book",
                "https://example.com/books/dsa.pdf",
                "DSA"
        ));
        allResources.add(new Resource(
                "Computer Networks",
                "book",
                "https://example.com/books/cn.pdf",
                "CN"
        ));

        // PYQs
        allResources.add(new Resource(
                "2023 Final Exam",
                "pyq",
                "https://example.com/pyqs/2023-final.pdf",
                "COA"
        ));
        allResources.add(new Resource(
                "2022 Mid Semester",
                "pyq",
                "https://github.com/himanshi20552/LitHub/blob/main/PDFs/notes/1.%20Mobile%20Application%20Development%20CSE3709.pdf",
                "MAD"
        ));
        // Add more PYQs here
        allResources.add(new Resource(
                "2023 Mid Semester",
                "pyq",
                "https://example.com/pyqs/2023-mid.pdf",
                "OS"
        ));
        allResources.add(new Resource(
                "2022 Final Exam",
                "pyq",
                "https://example.com/pyqs/2022-final.pdf",
                "DBMS"
        ));

        // Notes
        allResources.add(new Resource(
                "Operating Systems Notes",
                "note",
                "https://example.com/notes/os-notes.pdf",
                "OS"
        ));
        allResources.add(new Resource(
                "Database Systems",
                "note",
                "https://example.com/notes/db-notes.pdf",
                "DBMS"
        ));
        // Add more notes here
        allResources.add(new Resource(
                "Computer Architecture",
                "note",
                "https://example.com/notes/coa-notes.pdf",
                "COA"
        ));
        allResources.add(new Resource(
                "Software Engineering",
                "note",
                "https://example.com/notes/se-notes.pdf",
                "SE"
        ));

        // Clear existing lists
        booksList.clear();
        pyqsList.clear();
        notesList.clear();

        // Sort resources by type
        for (Resource resource : allResources) {
            switch (resource.getType().toLowerCase()) {
                case "book":
                    booksList.add(resource);
                    break;
                case "pyq":
                    pyqsList.add(resource);
                    break;
                case "note":
                    notesList.add(resource);
                    break;
            }
        }

        // Update adapters
        booksAdapter.notifyDataSetChanged();
        pyqsAdapter.notifyDataSetChanged();
        notesAdapter.notifyDataSetChanged();
    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            private android.os.Handler handler = new android.os.Handler();
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String query = s.toString().trim();
                        if (!query.isEmpty()) {
                            try {
                                SearchManager.getInstance(Homepage.this).performSearch(query);
                            } catch (Exception e) {
                                e.printStackTrace();
                                android.widget.Toast.makeText(Homepage.this,
                                        "Error performing search",
                                        android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

                // Add a small delay to prevent too frequent searches
                handler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void openFilteredResourceList(String subject) {
        Intent intent = new Intent(this, FilteredResourceListActivity.class);
        intent.putExtra("subject", subject);
        startActivity(intent);
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
