package com.example.LitHub;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FilteredResourceListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResourceAdapter adapter;
    private List<Resource> filteredList = new ArrayList<>();
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_resource_list);

        recyclerView = findViewById(R.id.recyclerViewFilteredResources);
        searchBar = findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ResourceAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        String subject = getIntent().getStringExtra("subject");
        setTitle(subject + " Resources");

        loadFilteredResources(subject);
    }

    private void loadFilteredResources(String subject) {
        // You may want to fetch from a shared source or database
        List<Resource> allResources = new ArrayList<>();
        // Add all your resources here, or fetch from a manager/singleton
        allResources.add(new Resource("Android Development Guide", "book", "https://example.com/books/android.pdf", "MAD"));
        allResources.add(new Resource("Machine Learning Basics", "book", "https://example.com/books/ml.pdf", "ML"));
        allResources.add(new Resource("2023 Final Exam", "pyq", "https://example.com/pyqs/2023-final.pdf", "COA"));
        allResources.add(new Resource("Operating Systems Notes", "note", "https://example.com/notes/os-notes.pdf", "OS"));
        // ...add all resources

        filteredList.clear();
        for (Resource r : allResources) {
            if (r.getSubject().equalsIgnoreCase(subject)) {
                filteredList.add(r);
            }
        }
        adapter.notifyDataSetChanged();
    }
} 