# Changelog

## [2.0.0-final] - 2023-10-27
### Added
- **User Roles Integration**: Fully implemented Admin and Student roles logic.
- **Settings Module**:
    - Dark mode toggle support.
    - Notification preferences toggle.
- **UI/UX Improvements**:
    - Empty states for Announcements and Tasks.
    - Loading indicators for data fetching.
    - Success/Error messages via Toasts and Snackbars.
- **Notifications**: Integrated local notification system for campus updates.
- **Clean Architecture**: Refactored data layer to follow repository pattern more strictly.

### Changed
- Improved Navigation flow with role-based access control.
- Updated UI theme with dynamic gradients and modern Material 3 components.

### Fixed
- Fixed session persistence issues in `SessionManager`.
- Resolved database synchronization bugs in Room.
