package com.example.LitHub;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchManager {
    private static SearchManager instance;
    private List<Resource> allResources;
    private Context context;

    private SearchManager(Context context) {
        this.context = context.getApplicationContext();
        this.allResources = new ArrayList<>();
        loadAllResources();
    }

    public static synchronized SearchManager getInstance(Context context) {
        if (instance == null) {
            instance = new SearchManager(context);
        }
        return instance;
    }

    private void loadAllResources() {
        try {
            // Add all resources from different sections
            // Books
            allResources.add(new Resource(
                "Android Development Guide",
                "book",
                "https://developer.android.com/guide/pdf/android-developer-guide.pdf",
                "MAD"
            ));
            allResources.add(new Resource(
                "Machine Learning Basics",
                "book",
                "https://www.cs.cmu.edu/~tom/mlbook.pdf",
                "ML"
            ));
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
                "https://example.com/pyqs/2022-mid.pdf",
                "MAD"
            ));
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error loading resources", Toast.LENGTH_SHORT).show();
        }
    }

    public void performSearch(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                // If search is empty, return to main activity
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                return;
            }

            query = query.toLowerCase().trim();
            ArrayList<Resource> searchResults = new ArrayList<>();

            // Search in all resources
            for (Resource resource : allResources) {
                if (resource.getTitle().toLowerCase().contains(query) ||
                    resource.getSubject().toLowerCase().contains(query)) {
                    searchResults.add(resource);
                }
            }

            if (searchResults.isEmpty()) {
                Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start SearchResultsActivity with the results
            Intent intent = new Intent(context, SearchResultsActivity.class);
            intent.putExtra("search_query", query);
            intent.putParcelableArrayListExtra("search_results", searchResults);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error performing search", Toast.LENGTH_SHORT).show();
        }
    }
} 