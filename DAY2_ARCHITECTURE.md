# Day 2 Architecture: Service Boundaries, Database Schema, API Contracts

## Repository
`Meal_Manager_android_app` — Meal Management System (Android, Kotlin, Jetpack Compose, Firebase)

## Proposed Concept
Comprehensive meal management platform with grocery list generation, nutrition tracking, recipe scaling, dietary preference AI, meal prep scheduling, and social sharing.

## Service Boundaries
| Service | Responsibility | Technology |
|---------|---------------|------------|
| Recipe Service | Recipe CRUD, ingredients, instructions, nutrition, tags | Kotlin, Firebase Firestore |
| Meal Plan Service | Weekly/monthly plans, meal assignments, templates | Kotlin, Firebase Firestore |
| Nutrition Service | Daily macros, calories, micronutrients, goals | Kotlin, Firebase Firestore |
| Grocery Service | List generation, categorization, store integration | Kotlin, Firebase Firestore |
| Social Service | Sharing, shared meal plans, collections, ratings | Kotlin, Firebase Firestore |
| Auth Service | Firebase Auth (email/password), biometric | Firebase Auth |
| Frontend | Android app (Jetpack Compose, MVVM) | Kotlin, Jetpack Compose |

## Database Schema (Firestore)
- `users` — {email, name, messId, isManager, deposit, mealCount, dietaryPreferences, restrictions, goals, timezone, createdAt}
- `recipes` — {id, title, ingredients[{name, quantity, unit}], instructions, prepTime, cookTime, servings, tags, nutrition {calories, macros, micronutrients}, images, createdBy, createdAt}
- `meal_plans` — {id, userId, weekStartDate, meals[{day, mealType, recipeId, servings}], templates, createdAt}
- `grocery_lists` — {id, userId, mealPlanId, items[{name, quantity, unit, category, aisle, checked]}, generatedAt}
- `nutrition_logs` — {id, userId, date, meals[{mealType, recipeId, servings}], totals {calories, protein, carbs, fat}, goals, createdAt}
- `shared_plans` — {id, planId, sharedWith[], permissions, createdAt}
- `recipe_collections` — {id, userId, name, recipeIds[], isPublic, createdAt}
- `ratings_reviews` — {id, recipeId, userId, rating, review, createdAt}

## API Contracts (Firebase Firestore + Functions)
### Recipes
- `GET /api/v1/recipes` → list (filters: tags, dietary)
- `POST /api/v1/recipes` — {title, ingredients, instructions, servings} → {recipe}
- `GET /api/v1/recipes/{id}` → {recipe, nutrition}
- `PUT /api/v1/recipes/{id}` → {recipe}
- `DELETE /api/v1/recipes/{id}` → 204

### Meal Plans
- `POST /api/v1/meal-plans` — {weekStartDate, meals} → {plan}
- `GET /api/v1/meal-plans/{id}` → {plan, groceryList}
- `PUT /api/v1/meal-plans/{id}/meals` — {day, mealType, recipeId} → 204

### Grocery Lists
- `POST /api/v1/grocery-lists/generate` — {mealPlanId} → {list}
- `GET /api/v1/grocery-lists/{id}` → {items}
- `PATCH /api/v1/grocery-lists/{id}/items/{itemId}` — {checked} → 204

### Nutrition
- `GET /api/v1/nutrition/logs` → list (date range)
- `POST /api/v1/nutrition/logs` — {date, meals} → {log, totals}
- `GET /api/v1/nutrition/goals` → {calories, macros, micronutrients}

### Social
- `POST /api/v1/sharing` — {planId, sharedWith[], permissions} → {shareId}
- `GET /api/v1/collections` → list
- `POST /api/v1/collections` — {name, recipeIds, isPublic} → {collection}
- `POST /api/v1/ratings` — {recipeId, rating, review} → {rating}

## Technical Notes
- Currently Firebase-only backend; vendor lock-in
- No grocery list generation, nutrition tracking, recipe scaling, dietary AI, meal prep, or social sharing yet
- Android only; no iOS support
- No offline mode; Firebase-dependent
- Meal rate calculation: Total Shopping / Total Meals; Member balance: Deposit - (Meal Rate × Meals)
- No CI/CD for mobile builds

## Day 2 Outcome
Architecture design documented. No code changes. 0 PRs. Ready for Day 3 (CI/CD pipeline).