package com.example.LitHub;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBooks;
    private ResourceAdapter bookAdapter;
    private List<Resource> bookList = new ArrayList<>();
    private EditText searchBar;
    private Button btnApplyFilters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        searchBar = findViewById(R.id.search_bar);
        btnApplyFilters = findViewById(R.id.btn_apply_filters);

        recyclerViewBooks.setLayoutManager(new GridLayoutManager(this, 2));
        bookAdapter = new ResourceAdapter(bookList, this);
        recyclerViewBooks.setAdapter(bookAdapter);

        loadBooks();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnApplyFilters.setOnClickListener(v -> showFilterDialog());

        findViewById(R.id.nav_resources).setOnClickListener(v -> finish());
        findViewById(R.id.nav_group_study).setOnClickListener(v -> {});
        findViewById(R.id.nav_collaborate).setOnClickListener(v -> {});
        findViewById(R.id.nav_practice).setOnClickListener(v -> {});
    }

    private void loadBooks() {
        // TODO: Replace with your actual data source for Books
        bookList.clear();
        bookList.add(new Resource("Android Development Guide", "book", "https://example.com/books/android.pdf", "MAD"));
        bookList.add(new Resource("Machine Learning Basics", "book", "https://example.com/books/ml.pdf", "ML"));
        // ...add more
        bookAdapter.notifyDataSetChanged();
    }

    private void filterBooks(String query) {
        List<Resource> filtered = new ArrayList<>();
        for (Resource r : bookList) {
            if (r.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                r.getSubject().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(r);
            }
        }
        bookAdapter.updateResources(filtered);
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pyq_filter, null); // reuse the same filter dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Example: Only subject filter is implemented for demo
        CheckBox subjectMad = dialogView.findViewById(R.id.subject_mad);
        CheckBox subjectCoa = dialogView.findViewById(R.id.subject_coa);
        CheckBox subjectSe = dialogView.findViewById(R.id.subject_se);
        CheckBox subjectMl = dialogView.findViewById(R.id.subject_ml);
        CheckBox subjectFdl = dialogView.findViewById(R.id.subject_fdl);

        dialogView.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            List<String> selectedSubjects = new ArrayList<>();
            if (subjectMad.isChecked()) selectedSubjects.add("MAD");
            if (subjectCoa.isChecked()) selectedSubjects.add("COA");
            if (subjectSe.isChecked()) selectedSubjects.add("Software Engineering");
            if (subjectMl.isChecked()) selectedSubjects.add("Machine Learning");
            if (subjectFdl.isChecked()) selectedSubjects.add("FDL");

            if (selectedSubjects.isEmpty()) {
                bookAdapter.updateResources(bookList);
                dialog.dismiss();
                return;
            }
            List<Resource> filtered = new ArrayList<>();
            for (Resource r : bookList) {
                if (selectedSubjects.contains(r.getSubject())) {
                    filtered.add(r);
                }
            }
            bookAdapter.updateResources(filtered);
            dialog.dismiss();
        });
        dialog.show();
    }
} 