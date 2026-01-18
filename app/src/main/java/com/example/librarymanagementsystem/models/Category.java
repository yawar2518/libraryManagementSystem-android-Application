package com.example.librarymanagementsystem.models;

public class Category {
    // 1. Fields
    private String categoryId; // Unique ID (e.g., "science_001")
    private String name;       // The name (e.g., "Computer Science")

    // 2. Empty Constructor (REQUIRED for Firestore)
    public Category() {
    }

    // 3. Full Constructor (For Admin to create new categories)
    public Category(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // 4. Getters and Setters
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
