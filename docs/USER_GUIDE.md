# Meal Manager Platform - User Guide

## Getting Started
1. Download the app or build from source: `./gradlew assembleDebug`
2. Create an account via email/password or biometric auth.
3. Complete your profile with dietary preferences, restrictions, and nutrition goals.
4. Start adding recipes, planning meals, and tracking nutrition.

## Recipes
- Create recipes with ingredients, instructions, prep/cook time, servings, and tags.
- View per-serving nutrition and scale recipes to any serving size.
- Favorite recipes and share them with friends via share tokens.
- Filter recipes by tag or dietary preference.

## Meal Planning
- Create weekly meal plans with per-day, per-meal-type recipe assignments.
- Use templates for recurring plans.
- Swap meals via drag-and-drop in the plan view.

## Nutrition Tracking
- Set calorie and macro goals (protein, carbs, fat).
- Log daily meals and view progress percentages against your goals.
- View daily/weekly/monthly nutrition trends.

## Grocery Lists
- Auto-generate grocery lists from meal plans; ingredients are aggregated and scaled by servings.
- Items are categorized by aisle (Dairy, Produce, Meat, Pantry, Other).
- Check off items as you shop.

## Barcode & Packaged Foods
- Scan packaged food barcodes to auto-log nutrition.
- Search the packaged food database by brand or name.

## Fridge & Pantry
- Track fridge items with expiration dates and get expiring-soon alerts.
- Manage pantry staples with reorder thresholds and auto-add low-stock items to your grocery list.

## Health Tracking
- Log weight and view BMI and progress charts.
- Track daily water intake with reminders.
- Log intermittent fasting windows and view history.

## Meal Photos & AI
- Log meals by taking a photo; AI recognizes food and estimates portions and nutrition.
- Get smart recipe suggestions based on fridge ingredients.

## Meal Prep & Cooking
- View prep steps and make-ahead meal storage instructions.
- Use cooking mode with step timers and hands-free guidance.

## Voice & Wearables
- Log meals by voice using Google Assistant/Siri shortcuts.
- Sync with Google Fit and Apple HealthKit.

## Social
- Share meal plans, recipes, and grocery lists with friends and family.
- Create public recipe collections and rate/review recipes.

## Account & Privacy
- Manage dietary preferences and timezone.
- Export or delete your data (GDPR).
- Control consent preferences.
- Admins can manage feature flags and run A/B tests.

## Offline Support
- The app caches data locally and syncs with the backend when online.
- Conflict resolution uses last-write-wins.
