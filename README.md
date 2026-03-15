app/
├── data/ (Data Layer)
│   ├── local/ (Room Database & Entities)
│   │   ├── SmartCampusDatabase.kt  (Main Database)
│   │   ├── TaskDao.kt              (DAO for Task Management)
│   │   ├── TaskEntity.kt           (Task Data Schema)
│   │   ├── AnnouncementDao.kt      (DAO for Updates)
│   │   └── AnnouncementEntity.kt   (Announcement Data Schema)
│   ├── CampusRepository.kt         (Campus Info Provider)
│   ├── TaskRepository.kt           (Task Logic Provider)
│   ├── AnnouncementRepository.kt   (Updates Logic Provider)
│   └── Department.kt               (Data Model)
│
├── navigation/
│   └── AppNavGraph.kt              (Centralized Navigation Logic)
│
├── ui/ (Presentation Layer)
│   └── theme/
│       ├── screens/ (UI Components)
│       │   ├── LoginScreen.kt        (Auth UI)
│       │   ├── DashboardScreen.kt    (Main Hub UI)
│       │   ├── CampusInfoScreen.kt   (Information UI)
│       │   ├── AnnouncementScreen.kt (Updates UI)
│       │   ├── TaskScreen.kt         (Task Manager UI)
│       │   └── SettingsScreen.kt     (App Settings)
│       ├── viewmodels/ (State Management)
│       │   ├── LoginViewModel.kt
│       │   ├── AnnouncementViewModel.kt
│       │   └── TaskViewModel.kt
│       └── theme-config/ (Styling)
│           ├── Color.kt
│           ├── Theme.kt
│           ├── Type.kt
│           └── Shape.kt
│
├── util/
│   └── SessionManager.kt           (Auth & Session Logic)
│
└── MainActivity.kt                 (Application Entry Point)

# App Description
Smart Campus Companion is a comprehensive mobile application designed to enhance the student experience by streamlining access to essential university resources. The app serves as a centralized hub where students can view campus information, stay updated with real-time announcements, and manage their academic responsibilities through an integrated task manager. Built with Jetpack Compose and following the MVVM architecture, it provides a modern, responsive, and user-friendly interface tailored for the needs of a digital campus.

# Architecture Layer
The project follows the Clean Architecture pattern combined with MVVM (Model-View-ViewModel) to ensure a scalable and maintainable codebase.
###### 1. Data Layer (The Foundation)
• SmartCampusDatabase.kt: The central Room database that manages local persistence for tasks and announcements.
• DAOs (Data Access Objects): TaskDao and AnnouncementDao define the SQL queries for interacting with the database.
• Entities: TaskEntity and AnnouncementEntity represent the database tables.
• Repositories: CampusRepository, TaskRepository, and AnnouncementRepository act as the single source of truth, abstracting data sources from the rest of the app.
###### 2. Domain Layer (Business Logic)
• Use Cases: Handles the logic for filtering announcements, calculating task deadlines, and managing user sessions.
• SessionManager.kt: Manages user authentication states and secure access to dashboard features.
###### 3. Presentation Layer (UI)
• Jetpack Compose: All UI components are built using a declarative approach for a modern look and feel.
###### _• ViewModels:_
◦ LoginViewModel: Handles user authentication logic.
◦ AnnouncementViewModel: Manages the state of campus updates and "read/unread" status.
◦ TaskViewModel: (Implemented by KenAnthony) Handles the creation, deletion, and tracking of student tasks.

## Key Components Explained
##### _SmartCampusDatabase.kt (Room Database)_
• Provides the offline storage capability.

• Ensures that announcements and tasks are saved even when the app is closed.
##### _AnnouncementViewModel.kt_
• What it does: Fetches announcements from the repository and exposes them as a state to the UI.

• Logic: Includes a "refresh" mechanism and handles marking announcements as read.
##### _AppNavGraph.kt (Navigation)_
•Routes:
◦ "login" → Entry point for authentication.
◦ "dashboard" → Main hub for all features.
◦ "campus_info" → Static campus details.
◦ "announcements" → List of university updates.
◦ "task_manager" → Personal productivity tool for students.
##### _MainActivity.kt (Entry Point)_
• The ComponentActivity that hosts the NavHost. It initializes the theme and sets up the navigation graph upon app launch.

### Data Flow
The app follows a Unidirectional Data Flow (UDF):
1. User Interaction: User clicks a button in the Compose UI.
2. Action: The UI calls a function in the ViewModel.
3. Data Processing: The ViewModel interacts with the Repository.
4. Local Storage: The Repository fetches/saves data via the Room DAO.
5. State Update: The Repository returns a Flow of data.
6. UI Refresh: The ViewModel updates the State, and the UI automatically recomposes to show the new data.

## Complete App Flow
1. LAUNCH: MainActivity starts → AppNavGraph loads.
2. AUTH: User logs in via LoginScreen → SessionManager saves the state.
3. DASHBOARD: User lands on DashboardScreen (the central hub).
4. UPDATES: User checks AnnouncementScreen → AnnouncementViewModel triggers a database fetch.
5. PRODUCTIVITY: User adds a task in TaskManager → TaskRepository saves it to Room.
6. NAVIGATION: User moves between screens seamlessly via the NavController without losing app state.

## Key Concepts Demonstrated
• Room Database: Local persistence and offline support.

• Jetpack Compose: Modern, reactive UI development.

• MVVM Architecture: Separation of concerns for easier testing and updates.

• Kotlin Coroutines & Flow: Asynchronous programming for smooth UI performance.

• Dependency Management: Organized package structure for a professional codebase.

# Team Roles:
### Reynalin Sexiona – Team Leader / Lead Developer
#### Responsibilities:
• Overall project planning, coordination, and management.

• Defined application architecture (MVVM + Compose).

• Implemented the Data Layer (CampusRepository, models, and Room database planning).

• Developed the Login system and session management.

• Final Integration: Responsible for merging all components and performing final testing.

• Support: Stepped in to complete and fix all pending tasks or features that were left unfinished by other members to ensure project completion.

• QA & Documentation.

### Aljay Roasario – UI Developer (Campus Info & Announcements)
#### Responsibilities:
• Designed and implemented CampusInfoScreen.kt.

• Announcement Screens: Created the user interface for viewing and interacting with campus announcements.

• Handled back navigation behavior and UI consistency across assigned modules.

### Anthony Carl Silo – UI Developer (Dashboard & Logic)
#### Responsibilities:
• Implemented DashboardScreen.kt and designed the main user entry point.

• Announcement Logic: Created the AnnouncementViewModel.kt to handle the business logic and state for the announcement system.

• Ensured smooth user experience through optimized navigation actions.

### KenAnthony Villena – Navigation & Task Management
#### Responsibilities:
• Implemented AppNavGraph.kt and defined all navigation routes.

• Task Manager: Designed and implemented the Task Management system, allowing users to track their to-do lists.

• Managed the navigation flow, start destinations, and back-stack handling.

# Git Workflow
We utilized a Feature Branch Workflow to maintain code organized and prevent project-wide errors during development.
1. Main Branch: Used only for stable, tested, and integrated code.
2. Feature Branches: Each member worked on a specific branch (e.g., feature/announcements, feature/task-manager) to avoid interfering with others' work.
3. Pull Requests: Members submitted their code for review before it was merged into the main branch by the Team Leader.

## Command Flow:
Java
git checkout -b feature/your-feature  # Create new branch
git add .                             # Stage changes
git commit -m "Describe changes"      # Commit work
git push origin feature/your-feature  # Push to GitHub

## Git Challenges
During the collaborative process, the team faced several technical hurdles:

• Merge Conflicts: Occurred frequently when multiple members modified shared files like AppNavGraph.kt or MainActivity.kt.

• Outdated Branches: Some members fell behind the main branch, making the final integration process difficult.

• Tracking Large Files: Initially, some IDE-specific files (like .idea or build folders) were tracked, causing clutter in the repository.

## Conflict Resolution
To maintain a healthy codebase, the following strategies were implemented:

• Communication: Before merging significant changes, members communicated through chat to ensure no one else was working on the same logic.

• Frequent Pulls: Members were encouraged to git pull origin main daily to sync their local work with the latest team updates.

• Manual Merging: In cases of hard conflicts, the Team Leader used the Android Studio Merge Tool to manually select the correct lines of code, ensuring that no functionality from either side was lost.

• Strict .gitignore: Refined the .gitignore file to ensure only source code and necessary resources were tracked, preventing configuration conflicts.
##### Buy Implementing this it,
• Prevents code conflicts

• Encourages modular development

• Keeps main branch stable

• Makes collaboration organized and traceable
