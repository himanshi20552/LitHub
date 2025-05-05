package com.example.LitHub; // Replace with your package name

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
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


public class TurnitinActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnitin);



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
                Intent intent = new Intent(TurnitinActivity.this, MainActivity.class); // Redirect to Login Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear task stack
                startActivity(intent);
                finish(); // Finish current activity
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        WebView webView = findViewById(R.id.webViewTurnitin);

        // Configure WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JS if needed
        webSettings.setDomStorageEnabled(true);

        // Force links to open in WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Load Turnitin login page
        webView.loadUrl("https://www.turnitin.com/login_page.asp");
    }
}