package com.example.librarymanagementsystem;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem.models.Book;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthorYear, tvDescription;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        db = FirebaseFirestore.getInstance();
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAuthorYear = findViewById(R.id.tvDetailAuthorYear);
        tvDescription = findViewById(R.id.tvDetailDescription);

        String bookId = getIntent().getStringExtra("bookId");

        if (bookId != null) {
            loadBookDetails(bookId);
        }

        setTitle("Book Details");
    }

    private void loadBookDetails(String bookId) {
        db.collection("books").document(bookId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Book book = documentSnapshot.toObject(Book.class);
                        if (book != null) {
                            tvTitle.setText(book.getTitle());
                            tvAuthorYear.setText("By " + book.getAuthor() + " (" + book.getYear() + ")");
                            tvDescription.setText(book.getDescription());
                        }
                    } else {
                        Toast.makeText(this, "Book not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading book details.", Toast.LENGTH_SHORT).show();
                });
    }
}
