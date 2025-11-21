# PayU Finance Android

A modern Android application for managing loans and repayments, built with Jetpack Compose and following MVVM architecture with clean architecture principles.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [Building the Project](#building-the-project)
- [Running the App](#running-the-app)
- [Project Structure](#project-structure)
- [Key Technologies](#key-technologies)
- [Testing](#testing)
- [Contributing](#contributing)

## ✨ Features

- ✅ **MVVM Architecture** - Clean separation of concerns
- ✅ **Jetpack Compose** - Modern declarative UI framework
- ✅ **Clean Architecture** - Domain-driven design with clear layer separation
- ✅ **Dependency Injection** - Koin for dependency management
- ✅ **Retrofit** - Type-safe HTTP client for API calls
- ✅ **StateFlow** - Reactive state management
- ✅ **Coil** - Efficient image loading
- ✅ **Lottie** - Smooth JSON-based animations
- ✅ **Lazypay Elevate** - PayU internal library integration
- ✅ **Chucker** - Network debugging tool (debug builds)

## 🏗️ Architecture

This project follows **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles, ensuring:

- **Separation of Concerns**: Clear boundaries between UI, business logic, and data layers
- **Testability**: Each layer can be tested independently
- **Maintainability**: Easy to understand and modify
- **Scalability**: Ready for future feature additions

### Architecture Layers

1. **UI Layer (Presentation)**
   - Jetpack Compose screens
   - ViewModels for state management
   - UI models for presentation-specific data

2. **Domain Layer (Business Logic)**
   - Use cases for business operations
   - Repository interfaces
   - Domain models (framework-independent)

3. **Data Layer**
   - API services (Retrofit)
   - Data sources
   - Repository implementations
   - DTOs (Data Transfer Objects)

For detailed architecture documentation, see [ARCHITECTURE.md](./ARCHITECTURE.md).

### Data Flow

```
User Action → Compose Screen → ViewModel → UseCase → Repository → Data Source → API Service
                                                                                      ↓
UI Update ← StateFlow/State ← UI Model ← Domain Model ← DTO ← Network Response
```

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio** Hedgehog (2023.1.1) or later
- **JDK 17** or later
- **Android SDK** with:
  - Minimum SDK: 24 (Android 7.0)
  - Target SDK: 35
  - Compile SDK: 35
- **Git** for version control
- **Gradle** 8.6.1+ (included via wrapper)

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd PayUFinanceAndroid
```

### 2. Configure GitHub Packages (for Lazypay Elevate)

The project uses Lazypay Elevate library from GitHub Packages. You need to configure authentication:

**Option A: Using local.properties (Recommended)**

Create or edit `local.properties` in the project root and add:

```properties
gpr.usr=your_github_username
gpr.key=your_github_personal_access_token
```

**Option B: Using Environment Variables**

```bash
export GPR_USR=your_github_username
export GPR_KEY=your_github_personal_access_token
```

**Note**: Generate a GitHub Personal Access Token with `read:packages` permission from [GitHub Settings](https://github.com/settings/tokens).

### 3. Sync Gradle Files

Open the project in Android Studio and let it sync Gradle files automatically, or run:

```bash
./gradlew build --refresh-dependencies
```

### 4. Configure API Base URL

Update the API base URL in `app/src/main/java/com/payu/finance/common/Constants.kt`:

```kotlin
object Constants {
    const val BASE_URL = "https://your-api-base-url.com/"
    // ... other constants
}
```

## ⚙️ Configuration

### API Configuration

The app uses the following configuration (can be modified in `Constants.kt`):

- **Base URL**: `https://sbox-intrasetu.payufin.io/` (default)
- **Network Timeout**: 30 seconds
- **Connect Timeout**: 10 seconds
- **Read Timeout**: 30 seconds

### Build Configuration

- **Application ID**: `com.payu.finance`
- **Version Code**: 1
- **Version Name**: 1.0
- **Min SDK**: 24
- **Target SDK**: 35

### Gradle Properties

The project includes optimized Gradle settings in `gradle.properties`:

- JVM heap size: 4096MB
- Parallel builds enabled
- Build cache enabled
- Configuration cache enabled
- R8 full mode enabled

## 🔨 Building the Project

### Build Debug APK

```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK

```bash
./gradlew assembleRelease
```

The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

### Build and Install on Connected Device

```bash
./gradlew installDebug
```

### Clean Build

```bash
./gradlew clean build
```

## ▶️ Running the App

### Using Android Studio

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator (API 24+)
4. Click the "Run" button or press `Shift + F10`

### Using Command Line

```bash
# Install and run on connected device
./gradlew installDebug

# Or use adb directly
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.payu.finance/.MainActivity
```

## 📁 Project Structure

```
app/src/main/java/com/payu/finance/
├── common/                    # Common utilities and shared code
│   ├── Constants.kt           # App-wide constants
│   ├── Extensions.kt          # Extension functions
│   ├── Resource.kt            # UI state wrapper
│   └── Result.kt              # Domain layer result wrapper
│
├── ui/                        # Presentation Layer
│   ├── base/                  # Base classes
│   │   └── BaseViewModel.kt   # Base ViewModel
│   ├── model/                 # UI Models
│   ├── screen/                # Compose Screens
│   ├── theme/                 # Material Design theme
│   └── viewmodel/             # ViewModels
│
├── domain/                    # Domain Layer
│   ├── model/                 # Domain Models
│   ├── repository/            # Repository Interfaces
│   └── usecase/               # Use Cases
│
├── data/                      # Data Layer
│   ├── api/                   # API Services
│   ├── datasource/            # Data Sources
│   ├── model/                 # DTOs
│   ├── network/               # Network utilities
│   ├── preferences/           # SharedPreferences
│   └── repository/            # Repository Implementations
│
├── di/                        # Dependency Injection
│   ├── NetworkModule.kt       # Network dependencies
│   ├── DataModule.kt          # Data layer dependencies
│   ├── DomainModule.kt        # Domain layer dependencies
│   └── ViewModelModule.kt     # ViewModel dependencies
│
├── navigation/                 # Navigation
│   ├── AppNavigation.kt
│   ├── AuthNavigation.kt
│   └── MainNavigation.kt
│
└── PayUFinanceApplication.kt  # Application class
```

## 🛠️ Key Technologies

### Core Libraries

- **Kotlin** 2.0.21 - Modern programming language
- **Jetpack Compose** - UI toolkit
- **Material 3** - Design system
- **Koin** 3.5.6 - Dependency injection
- **Retrofit** 2.11.0 - HTTP client
- **OkHttp** 4.12.0 - HTTP client implementation
- **Gson** 2.11.0 - JSON serialization
- **Coil** 2.7.0 - Image loading
- **Lottie** 6.6.2 - Animations

### Development Tools

- **Chucker** 4.0.0 - Network debugging (debug builds)
- **Gradle Version Catalog** - Centralized dependency management

### Testing Libraries

- **JUnit** 4.13.2 - Unit testing
- **MockK** 1.13.11 - Mocking framework
- **Truth** 1.4.4 - Assertion library
- **Turbine** 1.1.0 - Flow testing
- **Espresso** 3.6.1 - UI testing
- **Compose UI Test** - Compose testing utilities

For complete dependency list, see `gradle/libs.versions.toml`.

## 🧪 Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumented Tests

```bash
./gradlew connectedAndroidTest
```

### Run All Tests

```bash
./gradlew check
```

### Test Reports

Test reports are generated at:
- Unit tests: `app/build/reports/tests/test/index.html`
- Instrumented tests: `app/build/reports/androidTests/connected/index.html`

## 📝 Code Style

The project follows official Kotlin code style. Ensure your IDE is configured to use:
- Kotlin code style: `official` (configured in `gradle.properties`)

## 🔍 Debugging

### Network Debugging

The app includes **Chucker** for network debugging in debug builds. When running a debug build, you'll see a notification that allows you to inspect all network requests and responses.

### Logging

Network requests are logged using OkHttp's logging interceptor (only in debug builds).

## 🤝 Contributing

1. Create a feature branch from `main`
2. Make your changes following the project's architecture
3. Write/update tests for your changes
4. Ensure all tests pass
5. Submit a pull request with a clear description

### Code Guidelines

- Follow MVVM architecture pattern
- Keep layers separated (UI → Domain → Data)
- Write unit tests for use cases and ViewModels
- Use meaningful variable and function names
- Add comments for complex logic
- Follow Kotlin coding conventions

## 📄 License

[Add your license information here]

## 📞 Support

For issues and questions, please contact the development team or create an issue in the repository.

---

**Note**: This project is configured for PayU Finance internal use. Ensure you have proper access to GitHub Packages and API endpoints before running the application.
