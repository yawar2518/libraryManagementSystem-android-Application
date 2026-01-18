package com.example.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem.models.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.librarymanagementsystem.adapters.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageBooksActivity extends AppCompatActivity {
    private boolean isAdminUser = false;
    private RecyclerView rvBooks;
    private FloatingActionButton fabAddBook;

    private FirebaseFirestore db;
    private List<Book> bookList;
    private BookAdapter adapter;

    private String currentCategoryId;
    private String currentCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);

        currentCategoryId = getIntent().getStringExtra("categoryId");
        currentCategoryName = getIntent().getStringExtra("categoryName");
        isAdminUser = getIntent().getBooleanExtra("isAdmin", false);

        setTitle(currentCategoryName + " Books");

        db = FirebaseFirestore.getInstance();
        rvBooks = findViewById(R.id.rvBooks);
        fabAddBook = findViewById(R.id.fabAddBook);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();

        adapter = new BookAdapter(this, bookList,isAdminUser);
        rvBooks.setAdapter(adapter);

        if(isAdminUser) {
            fabAddBook.setOnClickListener(v -> {
                Intent intent = new Intent(ManageBooksActivity.this, AddEditBookActivity.class);
                intent.putExtra("categoryId", currentCategoryId);
                startActivity(intent);
            });
        }
        else{
            fabAddBook.setVisibility(View.GONE);    // Hide button for Student
        }

        loadBooks();
    }

    private void loadBooks() {
        db.collection("books")
                .whereEqualTo("categoryId", currentCategoryId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading books", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bookList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Book book = doc.toObject(Book.class);
                        book.setBookId(doc.getId()); // Important: Set the book ID
                        bookList.add(book);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}