package com.example.librarymanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem.AddEditBookActivity;
import com.example.librarymanagementsystem.BookDetailActivity;
import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private boolean isAdmin;
    public BookAdapter(Context context, List<Book> bookList, boolean isAdmin) {
        this.context = context;
        this.bookList = bookList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("By " + book.getAuthor());
        holder.tvYear.setText(book.getYear());

        holder.itemView.setOnClickListener(v -> {
            if (book.getBookId() == null || book.getBookId().isEmpty()) {
                Toast.makeText(context, "Error: Book ID is missing.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
            context.startActivity(intent);
        });

        if (isAdmin) {
            holder.ivEditBook.setVisibility(View.VISIBLE);
            holder.ivEditBook.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEditBookActivity.class);
                // Pass ALL existing data so the user doesn't have to re-type everything
                intent.putExtra("bookId", book.getBookId());
                intent.putExtra("categoryId", book.getCategoryId()); // Important to keep it in the same category
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("year", book.getYear());
                intent.putExtra("description", book.getDescription());
                context.startActivity(intent);
            });

            // Long Click Listener (Deletes the book)
            holder.itemView.setOnLongClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Delete Book")
                        .setMessage("Are you sure you want to delete '" + book.getTitle() + "'?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Perform the Delete in Firestore
                            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                    .collection("books")
                                    .document(book.getBookId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Book Deleted", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            });
        } else {
            holder.ivEditBook.setVisibility(View.GONE);
            holder.itemView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvYear;
        ImageView ivEditBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvYear = itemView.findViewById(R.id.tvBookYear);
            ivEditBook = itemView.findViewById(R.id.ivEditBook);
        }
    }
}
