# Taskly

Taskly is a sleek and intuitive To-Do app built with Jetpack Compose, designed to help you stay organized and boost your productivity effortlessly.

## Features

### Authentication

- Sign in with Email/Password
- Sign in with Google Account
- Firebase Authentication & Firestore integration

### Main Screens

Taskly consists of four main screens:

1. **Home Screen** - Displays today's tasks and completed tasks.
2. **Calendar Screen** - Allows browsing tasks by date and editing them.
3. **Alerts Screen** - Shows pending tasks and lets users set reminders.
4. **Profile Screen** - Manages user profile settings and account information.

### Task Management

- Create tasks by entering:
  - **Title** & **Description**
  - **Due Date** (via Calendar Picker)
  - **Time** to complete the task
  - **Category** (Choose from predefined or create a custom one with title, icon, and background color)
  - **Priority Level**
- Edit or delete tasks
- Mark tasks as completed

### Home Screen

- Displays only today's tasks (both pending and completed)
- Clicking a task opens the **Edit Task** screen

### Calendar Screen

- Features a horizontal calendar to browse tasks by date
- View all tasks for the selected date (pending & completed)
- Edit tasks directly from this screen

### Alerts Screen

- Displays only unfinished tasks
- Users can set reminders by selecting date & time (Notification permission required)
- Long-press on a task to delete an alert

### Profile Screen

- Displays user profile picture
- Edit account name
- Change password (only for Email/Password users)
- Change profile picture (opens phone gallery to select an image)
- Log out (redirects to the Start Screen for login/register selection)

## Tech Stack

- **Kotlin** / **Jetpack Compose**
- **Room Database**
- **Firebase Authentication & Firestore**
- **Dagger Hilt** for Dependency Injection
- **Coil** for image loading
- **Gson** for JSON serialization/deserialization

## Screenshots


### Start Screen / Authentication Screen

<p align="center">
  <img src="https://github.com/user-attachments/assets/6c495bfb-3e30-4767-bdf7-acdbfadc9df5"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/0297feb9-b3f9-41c8-a3d2-2ee9575a8492"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/dbd3172d-bb4f-42c7-856c-d9987a313e4a"
 width="30%" />
</p>

### Home Screen / Add Task Dialog

<p align="center">
  <img src="https://github.com/user-attachments/assets/418b6c0f-29ec-4bbc-95e9-cc1097496641"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/984924eb-a67e-470c-b033-989a705d2e4a"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/0b0a7f45-67ce-4a8a-acf8-b52e1ac08182"
 width="30%" /> &nbsp;&nbsp;&nbsp;
</p>

### Calendar Screen / Alerts Screen / Profile Screen

<p align="center">
  <img src="https://github.com/user-attachments/assets/bef46f8a-d7db-4a7c-aa72-a786097aaccf"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/6fbc2895-cb00-4e87-9b08-1e6fec6e4633"
 width="30%" /> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/4e663803-4b16-446a-89de-739398b59e6e"
 width="30%" /> &nbsp;&nbsp;&nbsp;
</p>


## Acknowledgements

Design used for this project is from [Figma](https://shorturl.at/YMbPI).
