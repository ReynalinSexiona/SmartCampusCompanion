# Smart Campus Companion 🎓

Smart Campus Companion is a modern Android application designed to centralize campus utilities for students and faculty. It integrates task management, real-time announcements, and campus navigation into a single, cohesive experience built with state-of-the-art Android development tools.

---

## ✨ Key Features

- **🔐 Secure Authentication**: Integrated login system with robust session handling via `SessionManager`.
- **📊 Interactive Dashboard**: A central hub for quick access to all campus services.
- **✅ Task & Schedule Manager**: 
    - Full CRUD operations for personal tasks.
    - Integrated Date & Time pickers for deadline management.
    - Local persistence using Room Database.
- **📢 Announcement Board**: Stay informed with persistent updates and campus-wide news.
- **🏛️ Campus Directory**: Detailed information about departments, facilities, and campus layout.
- **🌓 Dynamic Theming**: User-controlled Dark/Light mode support persisted across the app.

---

## 🏗️ Technical Architecture

The project implements the **MVVM (Model-View-ViewModel)** design pattern, ensuring a clean separation between data, logic, and UI.

### 📂 Directory Structure

```text
app/src/main/java/com/example/smartcampus/
├── 🗄️ data/
│   ├── local/               # Persistence Layer (Room)
│   │   ├── SmartCampusDatabase.kt
│   │   ├── TaskDao.kt / TaskEntity.kt
│   │   └── AnnouncementDao.kt / Announcement.kt
│   └── CampusRepository.kt  # Single source of truth for UI
├── 🗺️ navigation/
│   └── AppNavGraph.kt       # Navigation routes (login, dashboard, tasks, etc.)
├── 🎨 ui/theme/
│   ├── Screens/             # Jetpack Compose UI Components
│   │   ├── LoginScreen.kt, DashboardScreen.kt, TaskScreen.kt
│   │   ├── AnnouncementScreen.kt, CampusInfoScreen.kt, SettingsScreen.kt
│   └── ViewModels/          # State & Business Logic
│       ├── LoginViewModel.kt, TaskViewModel.kt, AnnouncementViewModel.kt
├── 🛠️ util/
│   └── SessionManager.kt    # SharedPreferences/Auth Utilities
└── 🚀 MainActivity.kt        # Entry point & Theme Provider
```

---

## 🛠️ Technology Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (100% Declarative UI)
- **Language**: [Kotlin](https://kotlinlang.org/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room) (SQLite Abstraction)
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **State**: [StateFlow & LiveData](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- **Theming**: [Material 3](https://m3.material.io/)

---

## 👥 Team & Contributions

### **Reynalin Sexiona** - *Team Lead & Architect*
- **Architecture**: Established MVVM structure and Room integration.
- **Backend-Local**: Developed `SmartCampusDatabase`, `TaskDao`, and `SessionManager`.
- **Integration**: Coordinated multi-feature merging and dependency management.

### **Aljay Rosario** - *UI/UX (Campus Info)*
- **Module**: Built the `CampusInfoScreen` using custom components.
- **Design**: Maintained visual consistency across informational screens. and Created na Campusscreen Feature screen, added some uodates on readme.

### **Anthony Carl Silo** - *UI/UX (Dashboard)*
- **Module**: Crafted the `DashboardScreen` interface and user workflow.
- **Interactions**: Implemented dashboard navigation logic and state feedback.and updated the viewmodel foe thw announcement featurw

### **KenAnthony Villena** - *Navigation Engineer*
- **Module**: Developed the `AppNavGraph` and route management.
- **Flow**: Ensured smooth back-stack handling and deep-link readiness. and updated the Task Manager Feauture 
  

---

## 📈 Git Workflow

To maintain a stable codebase, the team follows a **Feature Branching** strategy:

1.  **Main**: Always deployable, stable code.
2.  **Feature Branches** (`feature/*`): Development of specific modules (e.g., `feature/task-crud`).
3.  **Code Reviews**: Merges into `main` require validation and conflict resolution by the Team Lead.

---

*© 2024 Smart Campus Initiative*
