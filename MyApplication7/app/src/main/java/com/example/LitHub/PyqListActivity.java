package com.example.LitHub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PyqListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPyqs;
    private ResourceAdapter pyqAdapter;
    private List<Resource> pyqList = new ArrayList<>();
    private EditText searchBar;
    private Button btnApplyFilters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyq_list);

        recyclerViewPyqs = findViewById(R.id.recyclerViewPyqs);
        searchBar = findViewById(R.id.search_bar);
        btnApplyFilters = findViewById(R.id.btn_apply_filters);

        // Setup grid
        recyclerViewPyqs.setLayoutManager(new GridLayoutManager(this, 2));
        pyqAdapter = new ResourceAdapter(pyqList, this);
        recyclerViewPyqs.setAdapter(pyqAdapter);

        // Load all PYQs (replace with your actual data source)
        loadPyqs();

        // Search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPyqs(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter button click
        btnApplyFilters.setOnClickListener(v -> showFilterDialog());

        // Bottom nav listeners (example: finish() to go back to main page)
        findViewById(R.id.nav_resources).setOnClickListener(v -> finish());
        findViewById(R.id.nav_group_study).setOnClickListener(v -> {});
        findViewById(R.id.nav_collaborate).setOnClickListener(v -> {});
        findViewById(R.id.nav_practice).setOnClickListener(v -> {});
    }

    private void loadPyqs() {
        // TODO: Replace with your actual data source for PYQs
        pyqList.clear();
        pyqList.add(new Resource("Next-Gen Computer Architecture", "PYQ", "https://example.com/pyq1.pdf", "COA"));
        pyqList.add(new Resource("Software Engineering 2022", "PYQ", "https://example.com/pyq2.pdf", "Software Engineering"));
        // ...add more
        pyqAdapter.notifyDataSetChanged();
    }

    private void filterPyqs(String query) {
        List<Resource> filtered = new ArrayList<>();
        for (Resource r : pyqList) {
            if (r.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                r.getSubject().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(r);
            }
        }
        pyqAdapter.updateResources(filtered);
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pyq_filter, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Semester
        CheckBox sem1 = dialogView.findViewById(R.id.sem1);
        CheckBox sem2 = dialogView.findViewById(R.id.sem2);
        CheckBox sem3 = dialogView.findViewById(R.id.sem3);
        CheckBox sem4 = dialogView.findViewById(R.id.sem4);
        CheckBox sem5 = dialogView.findViewById(R.id.sem5);
        CheckBox sem6 = dialogView.findViewById(R.id.sem6);
        // Course
        CheckBox courseBtech = dialogView.findViewById(R.id.course_btech);
        CheckBox courseBba = dialogView.findViewById(R.id.course_bba);
        CheckBox courseBallb = dialogView.findViewById(R.id.course_ballb);
        // Subjects
        CheckBox subjectMad = dialogView.findViewById(R.id.subject_mad);
        CheckBox subjectCoa = dialogView.findViewById(R.id.subject_coa);
        CheckBox subjectSe = dialogView.findViewById(R.id.subject_se);
        CheckBox subjectMl = dialogView.findViewById(R.id.subject_ml);
        CheckBox subjectFdl = dialogView.findViewById(R.id.subject_fdl);
        // Year
        CheckBox year2020 = dialogView.findViewById(R.id.year_2020);
        CheckBox year2021 = dialogView.findViewById(R.id.year_2021);
        CheckBox year2022 = dialogView.findViewById(R.id.year_2022);
        CheckBox year2023 = dialogView.findViewById(R.id.year_2023);
        CheckBox year2024 = dialogView.findViewById(R.id.year_2024);
        // Branch
        CheckBox branchCse = dialogView.findViewById(R.id.branch_cse);
        CheckBox branchIt = dialogView.findViewById(R.id.branch_it);
        CheckBox branchEce = dialogView.findViewById(R.id.branch_ece);

        dialogView.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            // Collect selected filters
            List<String> selectedSubjects = new ArrayList<>();
            if (subjectMad.isChecked()) selectedSubjects.add("MAD");
            if (subjectCoa.isChecked()) selectedSubjects.add("COA");
            if (subjectSe.isChecked()) selectedSubjects.add("Software Engineering");
            if (subjectMl.isChecked()) selectedSubjects.add("Machine Learning");
            if (subjectFdl.isChecked()) selectedSubjects.add("FDL");

            // Example: Only subject filter is implemented for demo
            if (selectedSubjects.isEmpty()) {
                pyqAdapter.updateResources(pyqList);
                dialog.dismiss();
                return;
            }
            List<Resource> filtered = new ArrayList<>();
            for (Resource r : pyqList) {
                if (selectedSubjects.contains(r.getSubject())) {
                    filtered.add(r);
                }
            }
            pyqAdapter.updateResources(filtered);
            dialog.dismiss();
        });
        dialog.show();
    }
} 