# PayU Finance Android - MVVM Architecture

## Project Structure

This project follows **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles.

```
app/src/main/java/com/payu/finance/
├── common/                    # Common utilities and shared code
│   ├── Constants.kt           # App-wide constants
│   ├── Extensions.kt          # Extension functions
│   ├── Resource.kt            # UI state wrapper (Success/Error/Loading)
│   └── Result.kt              # Domain layer result wrapper
│
├── ui/                        # Presentation Layer (View)
│   ├── base/                  # Base classes for UI
│   │   └── BaseViewModel.kt   # Base ViewModel with common functionality
│   ├── model/                 # UI Models (presentation-specific)
│   │   ├── LoanUiModel.kt
│   │   └── RepaymentUiModel.kt
│   ├── screen/                # Compose Screens
│   │   ├── LoansScreen.kt
│   │   └── RepaymentsScreen.kt
│   ├── theme/                 # Material Design theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/             # ViewModels
│       ├── LoansViewModel.kt
│       └── RepaymentsViewModel.kt
│
├── domain/                    # Domain Layer (Business Logic)
│   ├── model/                 # Domain Models (clean business models)
│   │   ├── Loan.kt
│   │   └── Repayment.kt
│   ├── repository/            # Repository Interfaces
│   │   ├── BaseRepository.kt
│   │   ├── LoanRepository.kt
│   │   └── RepaymentRepository.kt
│   └── usecase/               # Use Cases (business logic)
│       ├── GetLoansUseCase.kt
│       ├── GetLoanByIdUseCase.kt
│       └── GetRepaymentsUseCase.kt
│
├── data/                      # Data Layer
│   ├── api/                   # API Service Interfaces (Retrofit)
│   │   ├── LoanApiService.kt
│   │   └── RepaymentApiService.kt
│   ├── datasource/            # Data Sources
│   │   ├── LoanRemoteDataSource.kt
│   │   └── RepaymentRemoteDataSource.kt
│   ├── model/                 # Data Transfer Objects (DTOs)
│   │   ├── LoanDto.kt
│   │   └── RepaymentDto.kt
│   └── repository/            # Repository Implementations
│       ├── LoanRepositoryImpl.kt
│       └── RepaymentRepositoryImpl.kt
│
└── di/                        # Dependency Injection (Koin)
    ├── NetworkModule.kt       # Network dependencies
    ├── DataModule.kt          # Data layer dependencies
    ├── DomainModule.kt        # Domain layer dependencies
    └── ViewModelModule.kt     # ViewModel dependencies
```

## Architecture Layers

### 1. **UI Layer (Presentation)**
- **Screens**: Jetpack Compose UI components
- **ViewModels**: Hold UI state and handle user interactions
- **UI Models**: Presentation-specific models with formatted data

**Flow**: Screen → ViewModel → UseCase → Repository

### 2. **Domain Layer (Business Logic)**
- **Use Cases**: Single responsibility business logic operations
- **Repository Interfaces**: Contracts for data operations
- **Domain Models**: Clean business models (no framework dependencies)

**Flow**: UseCase → Repository Interface

### 3. **Data Layer**
- **API Services**: Retrofit interfaces for network calls
- **Data Sources**: Handle API calls and error handling
- **DTOs**: Data Transfer Objects matching API response structure
- **Repository Implementations**: Map DTOs to Domain models

**Flow**: Repository Implementation → Data Source → API Service

## Data Flow

```
User Action
    ↓
Compose Screen
    ↓
ViewModel (handles event)
    ↓
UseCase (business logic)
    ↓
Repository Interface
    ↓
Repository Implementation
    ↓
Data Source
    ↓
API Service (Retrofit)
    ↓
Network Response
    ↓
DTO → Domain Model → UI Model
    ↓
StateFlow/State
    ↓
Compose Screen (UI updates)
```

## Key Components

### Resource & Result
- **Resource**: Used in UI layer for state management (Success/Error/Loading)
- **Result**: Used in domain/data layer for operation results

### Base Classes
- **BaseViewModel**: Common ViewModel functionality with error handling
- **BaseRepository**: Common repository error handling patterns

### Dependency Injection
Using **Koin** for dependency injection:
- Network module: Retrofit, OkHttp, API services
- Data module: Data sources, repository implementations
- Domain module: Use cases
- ViewModel module: ViewModels

## Best Practices Implemented

1. ✅ **Separation of Concerns**: Clear layer separation
2. ✅ **Single Responsibility**: Each class has one responsibility
3. ✅ **Dependency Inversion**: Domain layer doesn't depend on data layer
4. ✅ **Clean Architecture**: Domain models are framework-independent
5. ✅ **State Management**: Using StateFlow for reactive UI updates
6. ✅ **Error Handling**: Centralized error handling in base classes
7. ✅ **Type Safety**: Strong typing throughout the app
8. ✅ **Testability**: Easy to test with clear interfaces

## Next Steps

1. Add local database (Room) for caching
2. Add network error handling and retry logic
3. Add pagination for large lists
4. Add unit tests and UI tests
5. Add navigation component
6. Implement proper date formatting
7. Add loading states and empty states
8. Add pull-to-refresh functionality

