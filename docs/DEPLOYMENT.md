# Deployment Guide

## Backend (Kubernetes)
1. Build image: `docker build -t mealmanager/backend:latest backend/`
2. Push to registry: `docker push mealmanager/backend:latest`
3. Apply Helm chart: `helm install meal-manager ./deploy/helm/meal-manager`
4. ArgoCD sync: Application auto-syncs from `main` branch.

### Environment Variables
- `DB_PASSWORD`: PostgreSQL password
- `JWT_SECRET`: JWT signing secret
- `REDIS_HOST`, `REDIS_PORT`: Redis cache

## Android
1. Create Firebase project, add `google-services.json` to `app/`.
2. Enable Email/Password auth and Firestore.
3. Build: `./gradlew assembleDebug` or `assembleRelease`.
4. Distribute via Firebase App Distribution.

## CI/CD (GitHub Actions)
- Push to `development` → lint, unit tests, build APK, security scan, deploy to Firebase.
- Push to `main` → build backend image, deploy to Kubernetes via ArgoCD.

## Feature Flags
- Toggle features remotely via `/api/v1/feature-flags`.
- Example flags: `dark_mode`, `ai_food_recognition`, `social_sharing`, `offline_mode`.

## Monitoring
- Metrics: `/api/v1/monitoring/metrics`
- Health: `/api/v1/monitoring/health`
- Tracing: `/api/v1/monitoring/traces/{traceId}`
