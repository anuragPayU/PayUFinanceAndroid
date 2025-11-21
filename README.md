# PayU Finance Android

App for managing repayment and loan details following MVVM architecture pattern.

## Architecture

This project follows **MVVM (Model-View-ViewModel)** architecture with clean architecture principles.

See [ARCHITECTURE.md](./ARCHITECTURE.md) for detailed architecture documentation.

## Project Structure

```
app/src/main/java/com/payu/finance/
├── common/          # Common utilities and shared code
├── ui/              # Presentation Layer (Compose + ViewModels)
├── domain/          # Domain Layer (Use Cases + Repository Interfaces)
├── data/            # Data Layer (API Services + Repository Implementations)
└── di/              # Dependency Injection (Koin)
```

## Key Features

- ✅ MVVM Architecture
- ✅ Jetpack Compose UI
- ✅ Clean Architecture
- ✅ Dependency Injection (Koin)
- ✅ Retrofit for API calls
- ✅ StateFlow for reactive state management
- ✅ Separation of concerns
- ✅ Coil for image loading
- ✅ Lottie for animations
- ✅ Lazypay Elevate integration
- ✅ Comprehensive testing setup

## Setup

1. Update `Constants.kt` with your API base URL
2. Sync Gradle files (all dependencies are already configured)
3. Build and run the app

## Dependencies

All dependencies are configured using **Gradle Version Catalog** in `gradle/libs.versions.toml`.

### Key Libraries Included:

- **Jetpack Compose**: UI framework with Material 3
- **Koin**: Dependency injection
- **Retrofit + OkHttp**: Network layer
- **Coil**: Image loading library
- **Lottie**: JSON-based animations
- **Lazypay Elevate**: PayU internal library (v2.2)
- **Chucker**: Network debugging (debug builds)
- **Testing**: JUnit, MockK, Truth, Turbine, Compose UI tests

See [DEPENDENCIES.md](./DEPENDENCIES.md) for complete dependency list.

## Notes

- The project structure is ready for Figma designs integration
- ViewModels are set up to receive domain models and convert them to UI models
- API services are ready to be connected to your backend
- All layers are properly separated and testable
