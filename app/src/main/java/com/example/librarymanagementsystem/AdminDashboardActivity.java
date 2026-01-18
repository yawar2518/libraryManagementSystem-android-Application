package com.example.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem.adapters.CategoryAdapter; // Import the Adapter
import com.example.librarymanagementsystem.models.Category; // Import the Model
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private FloatingActionButton fabAddCategory;

    // NEW Variables for List and Adapter
    private FirebaseFirestore db;
    private List<Category> categoryList;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setTitle("Admin Dashboard");

        db = FirebaseFirestore.getInstance(); // Init Database

        rvCategories = findViewById(R.id.rvCategories);
        fabAddCategory = findViewById(R.id.fabAddCategory);

        // 1. Setup RecyclerView
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        // 2. Initialize List and Adapter
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList,true);
        rvCategories.setAdapter(adapter); // Plug the adapter into the RecyclerView

        // 3. Setup Button
        fabAddCategory.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AddEditCategoryActivity.class));
        });

        // 4. Fetch Data!
        loadCategories();
    }

    private void loadCategories() {
        // Listen to the "categories" collection
        db.collection("categories")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading categories", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Clear the old list so we don't get duplicates
                    categoryList.clear();

                    // Loop through the documents from Firestore
                    for (QueryDocumentSnapshot doc : value) {
                        // Convert the Firestore document into a Category object automatically!
                        Category category = doc.toObject(Category.class);
                        categoryList.add(category);
                    }

                    // Tell the adapter the data changed so it refreshes the screen
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