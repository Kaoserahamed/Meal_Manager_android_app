# 🍽️ Meal Management System

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A modern Android application for managing shared meal expenses in hostels, shared apartments, or any group living situation. Built with Jetpack Compose, MVVM architecture, and Firebase backend for real-time synchronization.

## ✨ Features

### 👥 User Management
- **Secure Authentication**: Email/password authentication via Firebase Auth
- **Role-Based Access**: Manager and Member roles with different permissions
- **Real-time Sync**: Live updates across all devices

### 💰 Financial Tracking
- **Deposit Management**: Track member deposits
- **Shopping Expenses**: Record shopping with item details
- **Automatic Calculations**: 
  - Meal rate = Total Shopping / Total Meals
  - Member balance = Deposit - (Meal Rate × Meals)
- **Transaction History**: Complete audit trail

### 📊 Dashboard & Reports
- Overview of total meals, deposits, shopping, and balance
- Individual member statistics
- Detailed transaction history

### 🍳 Meal Platform (Phase 1)
- **Recipe Management**: Create, edit, view and delete recipes with ingredients, instructions, prep/cook time, servings and tags
- **Ingredient Database**: Per-ingredient quantity and unit with automatic serving-size scaling
- **Meal Planning**: Weekly meal plans with per-day, per-meal-type recipe assignments and templates
- **Nutrition Tracking**: Daily calories/macros logging, per-serving nutrition, goals and progress percentages
- **Grocery List Generation**: Auto-generate grocery lists from meal plans, aggregated by ingredient, categorized by aisle, with check-off support

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM + Clean Architecture
- **Backend**: Firebase (Auth, Firestore, Realtime Database)
- **Async**: Coroutines + Flow
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 33 (Android 13)

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Composables, ViewModels, UI State)   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Data Layer                    │
│  (Repositories, Firebase, Data Models)  │
└─────────────────────────────────────────┘
```

**Key Components:**
- **Repositories**: Handle all Firebase operations
- **ViewModels**: Manage UI state with StateFlow
- **Composables**: Modern, reactive UI

## 🚀 Quick Start

### Prerequisites
- Android Studio Hedgehog | 2023.1.1+
- JDK 8+
- Firebase account

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Kaoserahamed/Meal_Manager_android_app.git
   cd Meal_Manager_android_app
   ```

2. **Firebase Setup**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add Android app with package name: `com.example.meal_management`
   - Download `google-services.json` and place in `app/` directory
   - Enable Email/Password authentication
   - Create Firestore database (start in test mode)

3. **Configure Firestore Security Rules**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId} {
         allow read: if request.auth != null;
         allow write: if request.auth.uid == userId;
       }
       match /messes/{messId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null;
       }
       match /{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or click Run ▶️ in Android Studio

## 📖 Usage

### For Managers
1. Sign up as Manager
2. Create a Mess
3. Add members
4. Record deposits and shopping
5. Track meal consumption
6. View reports

### For Members
1. Sign up as Member
2. Join a Mess
3. View your balance
4. Check transaction history

## 🔐 Security Features

- ✅ Firebase Auth encryption (no plaintext passwords)
- ✅ Input validation (email, password strength, amounts)
- ✅ Firestore security rules
- ✅ Transaction safety with atomic updates

## 📊 Database Schema

### Firestore Collections

**users**
```json
{
  "email": "user@example.com",
  "name": "John Doe",
  "messId": "mess_id",
  "isManager": true,
  "deposit": 1000.0,
  "mealCount": 25.0
}
```

**messes**
```json
{
  "name": "My Mess",
  "managerId": "user_id",
  "totalMeal": 100.0,
  "totalDeposit": 5000.0,
  "totalShopping": 3000.0
}
```

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

**Coding Standards:**
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Write meaningful commit messages
- Add tests for new features

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Kaoserahamed**
- GitHub: [@Kaoserahamed](https://github.com/Kaoserahamed)

## 🙏 Acknowledgments

- Firebase for backend infrastructure
- Jetpack Compose for modern UI
- Material Design 3 for design guidelines

## 🗺️ Roadmap

- [x] User authentication
- [x] Mess management
- [x] Deposit & meal tracking
- [x] Shopping expense management
- [x] Transaction history
- [x] Recipe management
- [x] Ingredient database & serving scaling
- [x] Meal planning
- [x] Nutrition tracking
- [x] Grocery list generation
- [ ] In-app chat
- [ ] Push notifications
- [ ] Data export (PDF/Excel)
- [ ] Offline mode
- [ ] Dark mode

## 📞 Contact

For issues or questions, please open an issue on [GitHub](https://github.com/Kaoserahamed/Meal_Manager_android_app/issues).

---

<p align="center">Made with ❤️ using Kotlin and Jetpack Compose</p>
