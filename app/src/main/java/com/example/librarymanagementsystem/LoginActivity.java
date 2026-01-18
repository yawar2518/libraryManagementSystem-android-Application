package com.example.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // 1. Declare Variables
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    // Firebase Tools
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Connects to your XML file

        // 2. Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 3. Connect Variables to XML IDs
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);
        progressBar = findViewById(R.id.progressBar);

        // 4. Handle Login Click
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser(String email, String password) {
        // Show loading spinner
        progressBar.setVisibility(View.VISIBLE);

        // A. Ask Firebase Auth to check email/password
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login Success! Now, WHO is this? Admin or Student?
                        checkUserRole(auth.getCurrentUser().getUid());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        // B. Go to Firestore database -> 'users' collection -> find document with this UID
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);

                    if (documentSnapshot.exists()) {
                        // Get the 'role' string we saved earlier
                        String role = documentSnapshot.getString("role");

                        if (role != null && role.equals("admin")) {
                            Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                            finish();
                        } else {
                            // Student goes to MainActivity (User Dashboard)
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Close login screen so back button doesn't come here
                        }
                    } else {
                        Toast.makeText(this, "User data not found in database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error checking role", Toast.LENGTH_SHORT).show();
                });
    }
}