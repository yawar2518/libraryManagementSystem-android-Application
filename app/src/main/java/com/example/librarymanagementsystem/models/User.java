package com.example.librarymanagementsystem.models;

public class User {
    private String uid;     // The unique ID from Firebase Authentication
    private String email;   // The user's email
    private String role;    // IMPORTANT: This will be "admin" or "student"

    // 2. Empty Constructor (REQUIRED for Firestore)
    // Firestore needs this empty door to enter the class and put data in.
    public User() {
    }

    // 3. Full Constructor (For us to use)
    // We use this when we want to create a new user in our code.
    public User(String uid, String email, String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    // 4. Getters and Setters
    // These allow other parts of the app to read (get) or change (set) the data.

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
