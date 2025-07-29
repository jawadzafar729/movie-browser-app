Movie Browser App 🍿🎬
<p align="center">
<img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge">
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Badge">
<img src="https://img.shields.io/badge/Jetpack Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose Badge">
<img src="https://img.shields.io/badge/Hilt-000000?style=for-the-badge&logo=dagger&logoColor=white" alt="Hilt Badge">
<img src="https://img.shields.io/badge/Retrofit-E44C30?style=for-the-badge&logo=retrofit&logoColor=white" alt="Retrofit Badge">
<img src="https://img.shields.io/badge/Room-4CAF50?style=for-the-badge&logo=androidx&logoColor=white" alt="Room Badge">
</p>

✨ Overview
Welcome to the Movie Browser App! This project is a modern Android application built entirely with Jetpack Compose that allows users to explore a vast collection of movies and TV shows. Powered by The Movie Database (TMDb) API, it showcases best practices in Android development, including dependency injection with Hilt, reactive programming with Kotlin Coroutines, efficient data loading with Paging 3, and local data persistence with Room.

Dive in to discover trending titles, search for your favorites, and get detailed information all within a beautiful and responsive user interface!

🚀 Features
Browse Trending/Popular Movies & TV Shows: Discover what's hot and new.

Search Functionality: Easily find specific movies or TV shows.

Detailed Content Views: View comprehensive information including synopsis, cast, crew, ratings, and trailers.

Infinite Scrolling: Seamlessly load more content as you scroll using Paging 3.

Offline Support (Coming Soon/Planned): Access previously viewed content even without an internet connection (via Room database).

Responsive UI: Optimized for various screen sizes and orientations.

🛠️ Tech Stack
This project leverages the latest and greatest Android technologies:

Kotlin: The primary programming language.

Jetpack Compose: Declarative UI toolkit for building native Android interfaces.

Hilt: A dependency injection library for Android that provides a standard way to incorporate Dagger DI into Android applications.

Kotlin Coroutines & Flow: For asynchronous programming and reactive data streams, ensuring a smooth and responsive user experience.

Retrofit & OkHttp: Type-safe HTTP client for making network requests to TMDb API, with OkHttp for efficient HTTP operations and logging.

Glide: Fast and efficient image loading library for displaying movie posters and backdrops.

Room Persistence Library: For local data caching and persistence, ensuring data is available offline and improves performance.

Paging 3: Enables efficient loading of large datasets, providing a smooth scrolling experience for lists of movies/TV shows.

Jetpack Navigation Component: For managing in-app navigation between different screens.

Gradle Kotlin DSL: For declarative and type-safe build configurations.

Kotlin Symbol Processing (KSP): Replaces Kapt for faster annotation processing.

📦 Architecture
The app follows a modern MVVM (Model-View-ViewModel) architectural pattern, combined with principles of Clean Architecture to ensure separation of concerns, testability, and maintainability.

UI Layer (Compose): Handles user interaction and displays data.

ViewModel Layer: Exposes data from the domain layer to the UI and handles UI logic.

Domain Layer: Contains business logic and use cases.

Data Layer: Responsible for fetching data from network (TMDb API) and local database (Room).

🎬 Getting Started
Follow these steps to get the project up and running on your local machine.

Prerequisites
Android Studio Flamingo or newer.

JDK 11 or higher.

A TMDb API Key. You can get one by registering at The Movie Database API.

Cloning the Repository
git clone [https://github.com/jawadzafar729/MovieBrowserApp.git](https://github.com/jawadzafar729/movie-browser-app.git)
cd MovieBrowserApp

API Key Setup
Create local.properties: In the root directory of your project (the same level as settings.gradle.kts and build.gradle.kts), create a file named local.properties.

Add your API Key: Open local.properties and add your TMDb API key in the following format:

TMDB_API_KEY="YOUR_TMDB_API_KEY_HERE"

Replace YOUR_TMDB_API_KEY_HERE with the actual API key you obtained from TMDb.
Important: local.properties should NOT be committed to version control (.gitignore usually handles this by default). This keeps your API key secure.

Running the App
Open the project in Android Studio.

Sync Gradle files (File > Sync Project with Gradle Files).

Run the app on an emulator or a physical device.

🖼️ Screenshots / GIF
(Add screenshots or a GIF showcasing the app's features here)

🔮 Future Enhancements
Implement movie/TV show favoriting and watchlist features.

Enhance search with filters and sorting options.

Detailed actor/actress profiles.

Improved error handling and UI feedback.

Robust offline support for cached data.

🤝 Contributing
Contributions are welcome! If you find a bug or have a feature request, please open an issue. If you'd like to contribute code, please fork the repository and create a pull request.

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

🙏 Acknowledgements
The Movie Database (TMDb) for providing the API.

The Android Jetpack team for amazing libraries.
