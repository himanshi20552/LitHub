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

public class NoteListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotes;
    private ResourceAdapter noteAdapter;
    private List<Resource> noteList = new ArrayList<>();
    private EditText searchBar;
    private Button btnApplyFilters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        searchBar = findViewById(R.id.search_bar);
        btnApplyFilters = findViewById(R.id.btn_apply_filters);

        recyclerViewNotes.setLayoutManager(new GridLayoutManager(this, 2));
        noteAdapter = new ResourceAdapter(noteList, this);
        recyclerViewNotes.setAdapter(noteAdapter);

        loadNotes();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnApplyFilters.setOnClickListener(v -> showFilterDialog());

        findViewById(R.id.nav_resources).setOnClickListener(v -> finish());
        findViewById(R.id.nav_group_study).setOnClickListener(v -> {});
        findViewById(R.id.nav_collaborate).setOnClickListener(v -> {});
        findViewById(R.id.nav_practice).setOnClickListener(v -> {});
    }

    private void loadNotes() {
        // TODO: Replace with your actual data source for Notes
        noteList.clear();
        noteList.add(new Resource("Operating Systems Notes", "note", "https://example.com/notes/os-notes.pdf", "OS"));
        noteList.add(new Resource("Database Systems", "note", "https://example.com/notes/db-notes.pdf", "DBMS"));
        // ...add more
        noteAdapter.notifyDataSetChanged();
    }

    private void filterNotes(String query) {
        List<Resource> filtered = new ArrayList<>();
        for (Resource r : noteList) {
            if (r.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                r.getSubject().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(r);
            }
        }
        noteAdapter.updateResources(filtered);
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
                noteAdapter.updateResources(noteList);
                dialog.dismiss();
                return;
            }
            List<Resource> filtered = new ArrayList<>();
            for (Resource r : noteList) {
                if (selectedSubjects.contains(r.getSubject())) {
                    filtered.add(r);
                }
            }
            noteAdapter.updateResources(filtered);
            dialog.dismiss();
        });
        dialog.show();
    }
} 