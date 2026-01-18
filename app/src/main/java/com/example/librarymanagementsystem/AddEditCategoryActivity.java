package com.example.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem.models.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID; // Used to generate random IDs

public class AddEditCategoryActivity extends AppCompatActivity {

    private String existingId = null;
    private EditText etCategoryName;
    private Button btnSaveCategory;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        db = FirebaseFirestore.getInstance();
        etCategoryName = findViewById(R.id.etCategoryName);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);

        // 1. CHECK FOR EDIT MODE
        if (getIntent().hasExtra("categoryId")) {
            existingId = getIntent().getStringExtra("categoryId");
            String currentName = getIntent().getStringExtra("currentName");

            etCategoryName.setText(currentName); // Pre-fill the box
            btnSaveCategory.setText("Update Category"); // Change button text
            setTitle("Edit Category");
        }

        btnSaveCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) return;

            if (existingId != null) {
                updateCategory(name); // Logic for EDIT
            } else {
                saveCategory(name);   // Logic for ADD (Existing function)
            }
        });
    }

    private void saveCategory(String name) {
        // 1. Generate a random unique ID for this category
        String categoryId = UUID.randomUUID().toString();

        // 2. Create the Model Object
        Category newCategory = new Category(categoryId, name);

        // 3. Save to Firestore
        // Note: This will automatically create the "categories" collection!
        db.collection("categories").document(categoryId).set(newCategory)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Category Added!", Toast.LENGTH_SHORT).show();
                    finish(); // Close this screen and go back to Dashboard
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 2. NEW FUNCTION FOR UPDATING
    private void updateCategory(String newName) {
        db.collection("categories").document(existingId)
                .update("name", newName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Category Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}