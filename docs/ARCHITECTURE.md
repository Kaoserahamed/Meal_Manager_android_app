# Meal Manager Platform - Architecture

## Service Boundaries
| Service | Responsibility | Technology |
|---------|---------------|------------|
| Recipe Service | Recipe CRUD, ingredients, nutrition, scaling, tags | Kotlin, Spring Boot, Firestore/PostgreSQL |
| Meal Plan Service | Weekly/monthly plans, assignments, templates | Kotlin, Spring Boot |
| Nutrition Service | Daily macros, calories, goals, trends | Kotlin, Spring Boot |
| Grocery Service | List generation, categorization, aisle sorting | Kotlin, Spring Boot |
| Social Service | Sharing, collections, ratings, reviews | Kotlin, Spring Boot |
| Barcode Service | Packaged food scan, nutrition database | Kotlin, Spring Boot |
| Fridge/Pantry Service | Inventory, expiration, reorder thresholds | Kotlin, Spring Boot |
| Health Service | Weight, water, fasting tracking | Kotlin, Spring Boot |
| AI Service | Food recognition, smart suggestions, recommendations | Kotlin, Vision ML |
| Meal Prep Service | Prep steps, make-ahead meals, cooking timers | Kotlin, Spring Boot |
| Voice Service | Voice meal logging, shortcuts | Kotlin, Spring Boot |
| Wearable Service | Google Fit, HealthKit sync | Kotlin, Spring Boot |
| User/Auth Service | Accounts, OAuth2, JWT, biometric, family SSO | Kotlin, Spring Security |
| Audit Service | Change audit log | Kotlin, Spring Boot |
| GDPR Service | Data export, deletion, consent | Kotlin, Spring Boot |
| Feature Flag Service | Remote config toggles | Kotlin, Spring Boot |
| A/B Testing Service | Experiment assignment and results | Kotlin, Spring Boot |
| Monitoring Service | Metrics, health checks | Kotlin, Micrometer |
| Tracing Service | Distributed tracing (OpenTelemetry) | Kotlin, OpenTelemetry |
| I18n Service | Multi-language translations | Kotlin, Spring Boot |

## Android Layers
- **Presentation**: Jetpack Compose screens, ViewModels with StateFlow, navigation routes.
- **Data**: Firestore repositories, offline cache, sync on connect.
- **Domain**: Data models for recipes, meal plans, nutrition, grocery, health, AI, social.

## Data Flow
1. User creates recipe → RecipeService validates → persisted → ViewModel updates UI via StateFlow.
2. Meal plan created → GroceryService aggregates ingredients across assigned recipes → scaled by servings → categorized by aisle.
3. Barcode scanned → BarcodeService looks up packaged food → nutrition logged.
4. Actions audited → AuditLogService records userId, action, details, timestamp.

## Security
- JWT access tokens + refresh tokens via Spring Security OAuth2 Resource Server.
- Biometric auth supported on Android.
- Firestore security rules enforce per-user data access.
- GDPR: data export/deletion/consent management.

## Offline
- Room-backed local cache (conceptual), sync when online, last-write-wins conflict resolution.

## Deployment
- Backend: Docker, Kubernetes, Helm, ArgoCD canary releases.
- Android: Firebase App Distribution, Google Play.
