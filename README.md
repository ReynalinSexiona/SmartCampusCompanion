app/
├── data/ (Data Layer)
│   ├── CampusRepository.kt (Data Repository)
│   └── Department.kt (Data Model)
│
├── navigation/
│   └── AppNavGraph.kt (Navigation Graph)
│
├── ui/ (Presentation Layer)
│   └── theme/
│       ├── LoginScreen.kt (Login UI)
│       ├── DashboardScreen.kt (Main Dashboard)
│       ├── CampusInfoScreen.kt (Campus Information UI)
│       ├── LoginViewModel.kt (Business Logic)
│       ├── Color.kt (Theme Colors)
│       ├── Theme.kt (App Theme)
│       └── Type.kt (Typography)
│
├── util/
│   └── SessionManager.kt (Session Handling)
│
└── MainActivity.kt (App Entry Point)

Architecture Layer
Data Layer Where data comes from

1.Handles application data

2.Manages repositories and models

3.Provides data to other layers

4.Acts as a single source of truth


Domain Layer (Business rules)

• Contains business logic
• Handles login and session state
• Independent from UI rendering

Presentation Layer (What user sees)

• UI components built with Jetpack Compose
• ViewModels manage UI state
• Handles user interaction and navigation

Department.kt (Data Model)

• Defines the structure of a Department object
• Represents campus department data
• Used by repository and UI
• Simple Kotlin data class

CampusRepository.kt (Data Repository)
What it does:

• Centralized location for campus-related data
• Supplies department information to ViewModels
• Abstracts data source from UI

Why it’s important:

• UI doesn’t need to know HOW data is retrieved
• Easy to replace static data with API or database
• Keeps code clean and scalable

LoginViewModel.kt (ViewModel)

• Holds and manages login UI state
• Handles authentication logic
• Communicates with SessionManager
• Survives configuration changes (rotation)
• Separates UI from business logic

SessionManager.kt (Session Utility)

• Manages user login session
• Stores authentication state
• Controls access to protected screens
• Helps maintain logged-in state

AppNavGraph.kt (Navigation)

• Defines all navigation routes in the app
• Uses Jetpack Compose Navigation
• Controls screen transitions
• Sets login as start destination

Routes:

• "login" → LoginScreen
• "dashboard" → DashboardScreen
• "campus" → CampusInfoScreen

MainActivity.kt (Entry Point)

• Entry point of the Android app
• Extends ComponentActivity
• Hosts Jetpack Compose UI
• Loads the navigation graph

Data Flow:

UI → ViewModel → Repository
↓               ↑
UI automatically updates based on state

Complete Step-by-Step Flow
USER LAUNCHES APP

└── MainActivity.onCreate() is called

SETUP COMPOSE UI

└── setContent { AppNavGraph() }

NAVIGATION INITIALIZES

└── NavController created
└── Start destination set to "login"

LOGIN SCREEN DISPLAYS

└── User enters credentials
└── LoginViewModel handles logic

SESSION VALIDATION

└── SessionManager updates login state

DASHBOARD SCREEN

└── Navigation to "dashboard"
└── User accesses campus features

CAMPUS INFO SCREEN

└── User navigates to "campus"
└── Campus information is displayed

BACK NAVIGATION

└── NavController safely pops back stack

Key Concepts Demonstrated

• Jetpack Compose UI
• MVVM Architecture
• Repository Pattern
• Navigation Component (Compose)
• State management with ViewModel
• Clean package separation

Team Roles:

Team Leader / Lead Developer

Reynalin Sexiona

Responsibilities:

Overall project planning and coordination

Application architecture (MVVM + Compose)

Data layer implementation (CampusRepository, models)

Login system and session management

Integration of all components

Final testing and project integration

UI Developer – Campus Information

Aljay Roasrios

Responsibilities:

Designed and implemented CampusInfoScreen.kt

Displayed campus-related information

Handled back navigation behavior

Ensured UI consistency with app theme

UI Developer – Dashboard

Anthony Carl Silo

Responsibilities:

Implemented DashboardScreen.kt

Designed the main user dashboard

Connected dashboard navigation actions

Ensured smooth user experience

Navigation Developer

KenAnthony Villena

Responsibilities:

Implemented AppNavGraph.kt

Defined navigation routes and destinations

Managed navigation flow between screens

Set up start destination and back stack handling


Git Workflow:

The project followed a Git-based collaborative workflow to ensure clean version control and organized development.

Branching Strategy

master / main branch

Contains stable and integrated code

Final submission branch

Feature branches

Each member worked on their assigned feature

Examples:

feature/campus-info-screen

feature/dashboard-screen

feature/navigation-graph

Development Workflow

Create feature branch

git checkout -b feature/feature-name


Implement assigned task

Each member works only on their assigned files

Commit changes

git commit -m "Add CampusInfoScreen UI"


Push to GitHub

git push origin feature/feature-name


Merge to main branch

Team leader reviews and merges changes

Resolves conflicts if necessary

Benefits of This Workflow

• Prevents code conflicts
• Encourages modular development
• Keeps main branch stable
• Makes collaboration organized and traceable
