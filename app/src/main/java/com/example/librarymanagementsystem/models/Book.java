package com.example.librarymanagementsystem.models;

public class Book {
    // 1. Fields
    private String bookId;      // Unique ID for the book
    private String title;       // Book Title
    private String author;      // Author Name
    private String year;        // Publication Year (String is easier than Int for simple text fields)
    private String description; // Short summary

    // IMPORTANT: This links the Book to the Category!
    private String categoryId;

    // 2. Empty Constructor (REQUIRED)
    public Book() {
    }

    // 3. Full Constructor
    public Book(String bookId, String title, String author, String year, String description, String categoryId) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
        this.categoryId = categoryId;
    }

    // 4. Getters and Setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
}