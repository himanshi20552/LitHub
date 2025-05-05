package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;
    private TextView searchQueryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize views
        recyclerView = findViewById(R.id.search_results_recycler_view);
        searchQueryText = findViewById(R.id.search_query_text);

        // Get search results from intent
        String query = getIntent().getStringExtra("search_query");
        ArrayList<Resource> searchResults = getIntent().getParcelableArrayListExtra("search_results");

        // Set up search query text
        searchQueryText.setText("Search results for: " + query);

        // Set up RecyclerView with GridLayoutManager (2 items per row)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SearchResultsAdapter(searchResults, this);
        recyclerView.setAdapter(adapter);
    }
} 