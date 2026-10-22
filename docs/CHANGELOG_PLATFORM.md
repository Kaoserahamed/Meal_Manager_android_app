# Changelog

## [Unreleased] - Full Platform Expansion
### Added
- Backend API: 20+ REST controllers covering recipes, meal plans, nutrition, grocery, barcode, fridge, pantry, health tracking, AI, social, users, auth, compliance, monitoring.
- Backend services, entities, DTOs, repositories, security, config, global exception handling.
- Android: phase models (barcode, packaged food, fridge, pantry, weight, water, fasting, meal photo, AI recognition, smart suggestions, prep steps, cooking mode, voice, wearable, audit log, feature flags, A/B testing).
- Android repositories and StateFlow ViewModels for each phase domain.
- Android Compose screens: barcode, fridge, pantry, weight, water, fasting, meal photo, audit log, feature flags, A/B testing.
- Navigation routes for all new screens.
- Unit tests for backend services/controllers/security/util and Android domain models.
- Instrumented Android tests.
- Documentation: API reference, user guide, architecture, deployment guide.
- CI/CD, Docker, Kubernetes, Helm, ArgoCD, feature-flag configs.

### Changed
- `Constants` extended with Phase 2/3 navigation routes.
- `MainActivity` registers all new composable destinations.
- README features and roadmap updated.

## [2.0.0] - 2024-01-15
(see previous CHANGELOG.md)
