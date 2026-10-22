# Meal Manager Platform - API Documentation

Base URL: `https://api.mealmanager.com/api/v1`

## Authentication
All endpoints except `/api/v1/auth/*` require a valid JWT Bearer token in the `Authorization` header.

```
Authorization: Bearer <access_token>
```

## Recipes
### GET /recipes
List recipes with optional filtering.
Query params: `tag`, `dietary`, `page` (default 0), `size` (default 20)

### GET /recipes/{id}
Get a single recipe by ID.

### POST /recipes
Create a new recipe.
Body: `RecipeDto`

### PUT /recipes/{id}
Update an existing recipe.
Body: `RecipeDto`

### DELETE /recipes/{id}
Delete a recipe.

### GET /recipes/{id}/nutrition
Get per-serving nutrition for a recipe.

### POST /recipes/{id}/scale
Scale a recipe to a new serving count.
Query param: `servings`

### POST /recipes/{id}/favorite
Toggle favorite status.

### POST /recipes/{id}/share
Generate a shareable token for a recipe.

## Meal Plans
### GET /meal-plans
List meal plans.

### GET /meal-plans/{id}
Get a meal plan by ID.

### POST /meal-plans
Create a meal plan.
Body: `MealPlanDto`

### PUT /meal-plans/{id}
Update a meal plan.

### DELETE /meal-plans/{id}
Delete a meal plan.

### PUT /meal-plans/{id}/meals
Replace meal assignments.
Body: `List<MealAssignmentDto>`

### GET /meal-plans/{id}/grocery-list
Get the grocery list for a plan.

### POST /meal-plans/{id}/templates
Create a template from a plan.

### GET /meal-plans/templates
List all templates.

## Nutrition
### GET /nutrition/logs
List nutrition logs within a date range.
Query params: `startDate`, `endDate`

### POST /nutrition/logs
Create a nutrition log.

### GET /nutrition/goals
Get nutrition goals.

### PUT /nutrition/goals
Update nutrition goals.

### GET /nutrition/daily-totals/{date}
Get daily totals for a date.

### GET /nutrition/progress
Get progress percentages against goals.

### GET /nutrition/trends
Get nutrition trends.
Query param: `range`

## Grocery Lists
### GET /grocery-lists
List grocery lists.

### GET /grocery-lists/{id}
Get a grocery list by ID.

### POST /grocery-lists/generate
Generate a grocery list from a meal plan.
Body: `{ "mealPlanId": "<uuid>" }`

### PATCH /grocery-lists/{listId}/items/{itemId}
Toggle item checked state.
Body: `{ "checked": true }`

### DELETE /grocery-lists/{id}
Delete a grocery list.

### GET /grocery-lists/categories
List grocery categories.

### GET /grocery-lists/aisles
List grocery aisles.

## Barcode & Packaged Food
### POST /barcode/scan
Scan a barcode.
Body: `{ "barcode": "string" }`

### GET /barcode/food/{barcode}
Get packaged food by barcode.

### POST /barcode/food
Add packaged food.
Body: `PackagedFoodDto`

### GET /barcode/food/search
Search packaged foods.
Query params: `query`, `page`, `size`

### POST /barcode/log-nutrition
Log barcode nutrition.
Body: `{ "barcode", "userId", "date", "servings" }`

## Fridge & Pantry
### GET /fridge
List fridge items.

### POST /fridge
Add fridge item.

### PUT /fridge/{id}
Update fridge item.

### DELETE /fridge/{id}
Delete fridge item.

### GET /fridge/expiring?days=7
List items expiring within N days.

### GET /fridge/low-stock
List low stock items.

### GET /pantry
List pantry items.

### POST /pantry
Add pantry item.

### PUT /pantry/{id}
Update pantry item.

### DELETE /pantry/{id}
Delete pantry item.

### GET /pantry/reorder-thresholds
List items below reorder threshold.

### POST /pantry/auto-add-to-grocery
Generate grocery suggestion from low-stock pantry items.

## Health Tracking (Weight, Water, Fasting)
### GET /weight | POST /weight | GET /weight/bmi | GET /weight/progress-chart
### GET /water | POST /water | GET /water/daily-totals/{date} | POST /water/reminders
### GET /fasting | POST /fasting | PUT /fasting/{id}/end | GET /fasting/history

## AI, Meal Prep, Cooking, Voice, Wearable
### POST /ai/food-recognition
Upload image for AI food recognition.

### POST /ai/meal-photo-log
Log a meal photo.

### GET /ai/smart-suggestions
Get smart recipe suggestions.

### GET /ai/recipe-recommendations
Get recipe recommendations.

### GET /meal-prep/prep-steps
List prep steps.

### GET /meal-prep/make-ahead
List make-ahead meals.

### POST /meal-prep/timers
Create cooking timer.

### GET /meal-prep/cooking-mode/{recipeId}
Get cooking mode for a recipe.

### POST /voice/log-meal
Log a meal by voice transcript.

### GET /voice/shortcuts
List voice shortcuts.

### GET /wearable/sync
Sync wearable data.

### GET /wearable/google-fit | GET /wearable/healthkit
Get wearable platform data.

## Social (Sharing, Collections, Ratings)
### POST /social/sharing
Share a meal plan.

### GET /social/sharing/{shareId}
Get a shared plan.

### POST /social/collections
Create a recipe collection.

### GET /social/collections
List collections.

### GET /social/collections/{id}/recipes
Get recipes in a collection.

### POST /social/ratings
Create a recipe rating.

### GET /social/recipes/{recipeId}/ratings
List ratings for a recipe.

### GET /social/recipes/{recipeId}/average-rating
Get average rating.

## Users & Auth
### POST /auth/login
### POST /auth/register
### POST /auth/logout
### POST /auth/refresh
### POST /auth/biometric
### POST /auth/sso/family

### GET /users | GET /users/{id} | PUT /users/{id} | DELETE /users/{id}
### GET /users/{id}/profile | PUT /users/{id}/profile
### GET /users/{id}/dietary-preferences | PUT /users/{id}/dietary-preferences

## Compliance & Monitoring
### GET /audit | GET /audit/export
### POST /gdpr/export | DELETE /gdpr/delete | POST /gdpr/consent
### GET /feature-flags | POST /feature-flags | GET /feature-flags/{key}
### GET /ab-testing/experiments | POST /ab-testing/assign | POST /ab-testing/results
### GET /monitoring/metrics | GET /monitoring/health | GET /monitoring/traces/{traceId}
### GET /monitoring/translations/{locale}

## Data Transfer Objects
Key DTOs: `RecipeDto`, `IngredientDto`, `NutritionInfoDto`, `MealPlanDto`, `MealAssignmentDto`, `GroceryListDto`, `GroceryItemDto`, `NutritionLogDto`, `DailyTotalsDto`, `NutritionGoalDto`, `NutritionProgressDto`, `UserDto`, `ProfileDto`, `DietaryPreferencesDto`, `AuditLogDto`, `FeatureFlagDto`, `PackagedFoodDto`, `FridgeItemDto`, `PantryItemDto`, `WeightLogDto`, `WaterLogDto`, `FastingWindowDto`, `ApiResponse<T>`, `Page<T>`.

## Errors
All errors return `ApiResponse` with `success: false` and a `message`. HTTP status codes: 400 bad request, 401 unauthorized, 403 forbidden, 404 not found, 500 internal error.
