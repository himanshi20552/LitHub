package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class CollaborateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collaborate);

        // WebView setup
        WebView webView = findViewById(R.id.webViewTrello);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://trello.com");

        // Initialize menu icon (now with proper ID)
        View menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(v -> {
            // Open main activity which has the drawer
            startActivity(new Intent(this, MainActivity.class));
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

        findViewById(R.id.nav_practice).setOnClickListener(v -> {
            startActivity(new Intent(this, PracticeMainActivity.class));
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}