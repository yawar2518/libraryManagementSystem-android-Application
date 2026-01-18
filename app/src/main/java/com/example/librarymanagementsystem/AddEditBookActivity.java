package com.example.librarymanagementsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem.models.Book;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddEditBookActivity extends AppCompatActivity {
    private String existingBookId = null;
    private EditText etTitle, etAuthor, etYear, etDescription;
    private Button btnSaveBook;
    private FirebaseFirestore db;
    private String categoryId; // The ID of the category this book belongs to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        // Connect Views
        etTitle = findViewById(R.id.etBookTitle);
        etAuthor = findViewById(R.id.etBookAuthor);
        etYear = findViewById(R.id.etBookYear);
        etDescription = findViewById(R.id.etBookDescription);
        btnSaveBook = findViewById(R.id.btnSaveBook);

        db = FirebaseFirestore.getInstance();

        // Get the Category ID passed from the previous screen
        categoryId = getIntent().getStringExtra("categoryId");

        // CHECK FOR EDIT MODE
        if (getIntent().hasExtra("bookId")) {
            existingBookId = getIntent().getStringExtra("bookId");

            // Pre-fill all the boxes
            etTitle.setText(getIntent().getStringExtra("title"));
            etAuthor.setText(getIntent().getStringExtra("author"));
            etYear.setText(getIntent().getStringExtra("year"));
            etDescription.setText(getIntent().getStringExtra("description"));

            // We still need the Category ID, but it comes from the book now
            categoryId = getIntent().getStringExtra("categoryId");

            btnSaveBook.setText("Update Book");
        }

        if (categoryId == null) {
            Toast.makeText(this, "Error: No Category Selected", Toast.LENGTH_SHORT).show();
            finish(); // Close if something went wrong
            return;
        }

        // Handle Save Click
        btnSaveBook.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String year = etYear.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(year)) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingBookId != null) {
                updateBook(title, author, year, description);
            } else {
                saveBook(title, author, year, description);
            }
        });
    }

    private void updateBook(String title, String author, String year, String description) {
        // We create a new Book object, but use the OLD ID
        Book updatedBook = new Book(existingBookId, title, author, year, description, categoryId);

        db.collection("books").document(existingBookId)
                .set(updatedBook) // .set() overwrites the old data with new data
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Book Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void saveBook(String title, String author, String year, String description) {
        // Create Book Object
        String bookId = UUID.randomUUID().toString(); // Random ID

        // Notice we are passing 'categoryId' here. This links the book!
        Book newBook = new Book(bookId, title, author, year, description, categoryId);

        // Save to "books" collection
        db.collection("books").document(bookId).set(newBook)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Book Saved!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the book list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
