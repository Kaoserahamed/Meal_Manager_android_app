# Contributing to Meal Management System

First off, thank you for considering contributing to Meal Management System! It's people like you that make this project better for everyone.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to [your.email@example.com].

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps to reproduce the problem**
* **Provide specific examples to demonstrate the steps**
* **Describe the behavior you observed after following the steps**
* **Explain which behavior you expected to see instead and why**
* **Include screenshots and animated GIFs** if possible
* **Include your Android version and device model**

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a step-by-step description of the suggested enhancement**
* **Provide specific examples to demonstrate the steps**
* **Describe the current behavior** and **explain which behavior you expected to see instead**
* **Explain why this enhancement would be useful**

### Pull Requests

* Fill in the required template
* Do not include issue numbers in the PR title
* Follow the Kotlin coding style
* Include thoughtfully-worded, well-structured tests
* Document new code
* End all files with a newline

## Development Setup

1. Fork the repo
2. Clone your fork
3. Create a branch for your feature
4. Make your changes
5. Run tests
6. Push to your fork
7. Submit a pull request

### Coding Standards

#### Kotlin Style Guide

Follow the [official Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
fun calculateBalance(deposit: Double, mealRate: Double, mealCount: Double): Double {
    return deposit - (mealRate * mealCount)
}

// Bad
fun calc(d: Double, mr: Double, mc: Double): Double {
    return d-(mr*mc)
}
```

#### Naming Conventions

* **Classes**: PascalCase (e.g., `UserRepository`, `AuthViewModel`)
* **Functions**: camelCase (e.g., `getUserById`, `calculateMealRate`)
* **Variables**: camelCase (e.g., `userName`, `totalDeposit`)
* **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`)

#### Documentation

* Add KDoc comments for all public APIs
* Include parameter descriptions
* Include return value descriptions
* Include usage examples for complex functions

```kotlin
/**
 * Calculates the balance for a user based on their deposit and meal consumption.
 *
 * @param deposit The total amount deposited by the user
 * @param mealRate The cost per meal
 * @param mealCount The number of meals consumed
 * @return The remaining balance after deducting meal costs
 */
fun calculateBalance(deposit: Double, mealRate: Double, mealCount: Double): Double {
    return deposit - (mealRate * mealCount)
}
```

#### Testing

* Write unit tests for all business logic
* Write integration tests for repository methods
* Aim for at least 80% code coverage
* Use meaningful test names

```kotlin
@Test
fun `calculateBalance returns correct value when deposit is greater than meal cost`() {
    val result = calculateBalance(1000.0, 10.0, 50.0)
    assertEquals(500.0, result, 0.01)
}
```

#### Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

* `feat:` A new feature
* `fix:` A bug fix
* `docs:` Documentation only changes
* `style:` Changes that don't affect code meaning (formatting, etc.)
* `refactor:` Code change that neither fixes a bug nor adds a feature
* `perf:` Performance improvements
* `test:` Adding or correcting tests
* `chore:` Changes to build process or auxiliary tools

Examples:
```
feat: add deposit history filtering
fix: correct balance calculation for negative values
docs: update README with new installation steps
refactor: extract validation logic to utility class
```

## Project Structure

```
app/src/main/java/com/example/meal_management/
├── data/
│   ├── model/          # Data models
│   └── repository/     # Data access layer
├── presentation/
│   ├── screen/         # UI screens
│   └── viewmodel/      # ViewModels
└── util/               # Utility classes
```

## Testing Guidelines

### Unit Tests

Located in `app/src/test/`:

```kotlin
class AuthRepositoryTest {
    @Test
    fun `signIn returns success when credentials are valid`() {
        // Arrange
        val repository = AuthRepository()
        
        // Act
        val result = runBlocking { repository.signIn("test@example.com", "password123") }
        
        // Assert
        assertTrue(result is Result.Success)
    }
}
```

### Instrumented Tests

Located in `app/src/androidTest/`:

```kotlin
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @Test
    fun loginButton_isDisplayed() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }
        
        composeTestRule.onNodeWithText("Log in").assertIsDisplayed()
    }
}
```

## Questions?

Feel free to open an issue with your question or contact the maintainers directly.

## Recognition

Contributors will be recognized in the README.md file.

Thank you for contributing! 🎉
