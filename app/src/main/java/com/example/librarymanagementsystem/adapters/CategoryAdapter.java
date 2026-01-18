package com.example.librarymanagementsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem.R;
import com.example.librarymanagementsystem.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private boolean isAdmin;

    // Constructor: We need the context and the list of data to start
    // Update the Constructor
    public CategoryAdapter(Context context, List<Category> categoryList, boolean isAdmin) {
        this.context = context;
        this.categoryList = categoryList;
        this.isAdmin = isAdmin; // Save the role status
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout (item_category.xml) we created in Step 1
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        // 1. Handle the click!
        holder.itemView.setOnClickListener(v -> {
            // We need to tell the next screen WHICH category was clicked.
            // We do this using "Intent Extras".
            Intent intent = new Intent(context, com.example.librarymanagementsystem.ManageBooksActivity.class);
            intent.putExtra("categoryId", category.getCategoryId()); // Sending the ID (e.g., "cat_101")
            intent.putExtra("categoryName", category.getName());     // Sending the Name (e.g., "Science")
            intent.putExtra("isAdmin", isAdmin);
            context.startActivity(intent);
        });

        if (isAdmin) {
            holder.ivEditCategory.setVisibility(View.VISIBLE);

            // 2. Handle Click
            holder.ivEditCategory.setOnClickListener(v -> {
                Intent intent = new Intent(context, com.example.librarymanagementsystem.AddEditCategoryActivity.class);
                // PASS THE EXISTING DATA!
                intent.putExtra("categoryId", category.getCategoryId());
                intent.putExtra("currentName", category.getName());
                context.startActivity(intent);
            });

            // 2. Long Click Listener (Deletes the category)
            holder.itemView.setOnLongClickListener(v -> {

                // Create an Alert Dialog (Popup)
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure you want to delete '" + category.getName() + "'?")
                        .setPositiveButton("Delete", (dialog, which) -> {

                            // Perform the Delete in Firestore
                            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                    .collection("categories")
                                    .document(category.getCategoryId()) // Use the ID to find the specific document
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        android.widget.Toast.makeText(context, "Category Deleted", android.widget.Toast.LENGTH_SHORT).show();
                                        // The SnapshotListener in the Activity will automatically refresh the list!
                                    });
                        })
                        .setNegativeButton("Cancel", null) // Do nothing if cancelled
                        .show();

                return true; // "true" means we handled the long click, so don't do a normal click afterwards
            });
        } else {
            holder.ivEditCategory.setVisibility(View.GONE);
            holder.itemView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size(); // Tell the list how many items to draw
    }

    // This class holds the UI elements for ONE row
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivEditCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivEditCategory = itemView.findViewById(R.id.ivEditCategory);
        }
    }
}
