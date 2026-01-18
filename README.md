# 📚 Library Management System (Android)

A robust, full-featured Android application designed to streamline library operations. This system features **Role-Based Access Control (RBAC)**, distinguishing between **Admin** (Faculty) and **Student** interfaces to ensure secure and efficient management of library resources.

Built with **Java** and powered by **Google Firebase** for real-time data synchronization and authentication.

---

## 📱 Features

### 🔐 Authentication & Security
* **Secure Login/Registration:** Email and Password authentication via Firebase Auth.
* **Role-Based Access Control:**
    * **Admins:** Full CRUD (Create, Read, Update, Delete) access.
    * **Students:** Restricted Read-Only access.

### 👨‍💼 Admin Panel (Faculty)
* **Dashboard:** Quick access to manage Categories and Books.
* **Category Management:** Create, Update, and Delete book categories (e.g., "Computer Science").
* **Book Management:** Add new books, Edit existing details, or Delete outdated records.
* **Gestures:** Long-press to delete items; Tap "Pencil" icon to edit.

### 👨‍🎓 Student Panel
* **Browse Catalog:** View all available categories and books.
* **Book Details:** Access detailed information including Author, Publication Year, and Description.
* **Security:** "Add" and "Edit" buttons are automatically hidden based on user role.

---

## 🛠️ Tech Stack

* **Language:** Java (Android SDK)
* **Architecture:** MVC (Model-View-Controller)
* **Backend & Database:** Google Firebase Firestore (NoSQL)
* **Authentication:** Firebase Authentication
* **UI/UX:** Material Design components, CardViews, Custom Adapters
* **IDE:** Android Studio

---

## 📸 Screenshots

| Login Screen | Admin Dashboard | Student View |
|:---:|:---:|:---:|
| <img src="https://github.com/yawar2518/libraryManagementSystem-android-Application/blob/main/images/login_screen.png" width="200"> | <img src="https://github.com/yawar2518/libraryManagementSystem-android-Application/blob/main/images/admin_screen.png" width="200"> | <img src="https://github.com/yawar2518/libraryManagementSystem-android-Application/blob/main/images/student_screen.png" width="200"> |

---

## 🚀 How to Run

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/yawar2518/libraryManagementSystem-android-Application.git
    ```
2.  **Open in Android Studio**
    * Open Android Studio -> File -> Open -> Select the cloned folder.
3.  **Firebase Setup**
    * This project relies on Firebase. Ensure `google-services.json` is present in the `app/` directory.
    * *Note: If you are cloning this for testing, you may need to create your own Firebase project and replace the JSON file.*
4.  **Run the App**
    * Connect an Android device or use an Emulator.
    * Click the green **Run** button.

---

## 🔮 Future Improvements

* **Search Functionality:** Implementing a search bar to filter books by title or author.
* **Borrowing System:** Feature for students to "Check Out" books with due dates.
* **Profile Management:** User profile pictures and password reset functionality.

---

## 📝 License

This project was developed as a Semester Project for the University of Management and Technology.
