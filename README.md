﻿# MatchPets

## Project Overview

MatchPets is an innovative social application designed to help pet owners find the perfect match for their pets. The app allows users to create detailed profiles for their pets, including essential information such as breed, color, height, and type. Through these profiles, pet owners can connect with others to arrange playdates and meetings for their pets, creating a vibrant community for pets and their owners.

## Key Features

### 1. User Authentication
- **Login**: Secure login using email and password, allowing users to access their profiles and the app's features.
- **Register**: New users can create an account by providing their email, name, password, and pet details, which helps set up their pet's profile on the platform.

### 2. Profile Management
- **Profile Creation and Update**: Users can create and update their pet's profile, including uploading photos and adding details such as breed, color, and height. This information is visible to others, helping in selecting potential matches.
- **Profile Review**: Users can browse through pet profiles and decide whether they want to connect with a particular pet based on its details.

### 3. Matching System
- **Swipe Functionality**:
  - **Right Swipe (Interested)**: Indicates interest in a profile, adding it to the list of potential matches.
  - **Left Swipe (Not Interested)**: Dismisses a profile from search results, ensuring only relevant matches are shown.
- **Potential Matches**: The app suggests nearby matches based on the provided details and location, aligning with user preferences.

### 4. Communication
- **Chat Functionality**: Once a mutual match is established (both users like each other’s profiles), they can initiate a conversation within the app to arrange meetups for their pets.

### 5. Error Handling
- **Login Error Alerts**: Real-time alerts guide users if incorrect credentials are entered.
- **Logout**: Users can securely log out to ensure data privacy.

## Challenges Addressed
- **Material UI Implementation**: Ensures a consistent, user-friendly experience across devices with Material Design.
- **Real-Time Chat**: Implemented real-time chat functionality for instant communication between matched users, utilizing Firebase's Realtime Database for live data updates.
- **Firebase Authentication and Database**: Used Firebase for secure, efficient user authentication and data storage, ensuring user data protection and reliable access control.
