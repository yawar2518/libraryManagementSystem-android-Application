package com.example.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem.adapters.CategoryAdapter;
import com.example.librarymanagementsystem.models.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private FirebaseFirestore db;
    private List<Category> categoryList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Note: Make sure activity_main.xml has a RecyclerView with ID "rvCategories"
        // If your activity_main.xml is empty, copy the XML below this code block.

        setTitle("Library Home"); // Student View

        db = FirebaseFirestore.getInstance();
        rvCategories = findViewById(R.id.rvCategories);

        // Setup List
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList,false);
        rvCategories.setAdapter(adapter);

        // Load Data (Same as Admin!)
        loadCategories();
    }

    private void loadCategories() {
        db.collection("categories")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    categoryList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        categoryList.add(doc.toObject(Category.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    // 1. Inflate the Menu (Show the button)
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // 2. Handle the Click
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // A. Sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            // B. Go back to Login Screen
            Intent intent = new Intent(this, LoginActivity.class);
            // This flag clears the history so pressing "Back" won't let them back in!
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}