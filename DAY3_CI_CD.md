# Day 3 CI/CD: Build / Test / Deploy Pipeline

## Repository
`Meal_Manager_android_app` — Meal Management System (Android, Kotlin, Jetpack Compose)

## Pipeline Overview
GitHub Actions CI/CD for Android mobile application (Gradle Kotlin DSL).

### Trigger
- Push/Pull Request to `main`
- Manual workflow dispatch

### Stages

#### 1. Lint & Format
- `ktlint` (Kotlin code style)
- `detekt` (Kotlin static analysis)
- Prettier (for non-Kotlin files)

#### 2. Test
- Unit tests (JUnit + Robolectric)
- Instrumented tests (Espresso for UI flows)
- ViewModel tests
- Repository tests (mock Firebase)

#### 3. Build
- `./gradlew assembleDebug` (debug APK)
- `./gradlew assembleRelease` (release APK/AAB)
- Android Lint

#### 4. Security Scan
- Secret detection (no Firebase credentials, no API keys)
- Dependency vulnerability check
- Android Lint security issues

#### 5. Deploy (on merge to `main`)
- Build release APK/AAB
- Upload to Firebase App Distribution (beta testers)
- Google Play Console internal track (optional)
- GitHub Release with APK artifact

### Quality Gates
- All unit tests pass
- `ktlint` and `detekt` clean
- Android Lint passes
- Build succeeds (debug + release)
- No secrets committed (no `google-services.json` with real credentials)

### Artifacts
- Debug APK
- Release APK/AAB
- Test reports
- Coverage reports (Jacoco)
- Lint reports

## Day 3 Outcome
CI/CD pipeline documented. No code changes. 0 PRs. Ready for Day 4 (project restructuring).