# Smart Campus Companion - Project Documentation

## 1. Architecture Overview
The app follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern:
- **UI Layer:** Jetpack Compose for modern, declarative UI.
- **Domain/Business Layer:** ViewModels manage state and handle user interactions.
- **Data Layer:** 
    - **Room Database:** Provides local persistence for Tasks, Users, and Announcements.
    - **Repository Pattern:** Abstracts data sources from the rest of the app.
- **Utility:** `SessionManager` for SharedPreferences and `NotificationHelper` for system-level alerts.

## 2. Git Workflow (Gitflow)
The project adheres to the following branch strategy:
- `main`: Production-ready code (tagged `v2.0-final`).
- `develop`: Integration branch for features.
- `feature/*`: Individual feature development (e.g., `feature/settings`, `feature/notifications`).
- `release`: Final polishing before merging to main.

## 3. Key Features
- **Role-Based Access:** Admin can post announcements; Students have read-only access with "Mark as Read" functionality.
- **Personalized Tasks:** Each user sees only their own tasks based on their login session.
- **Theme Engine:** Full support for Light and Dark modes.
- **Local Notifications:** Keeps users updated on critical campus news.

## 4. Final Deliverables
- **APK:** Located in `app/release/`.
- **Changelog:** Refer to `CHANGELOG.md`.
- **Version Tag:** `v2.0-final`
