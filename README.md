# Movie List App

## Overview
This is a Movie List Application that allows users to browse movies by category, search for movies using keywords, and view detailed information about selected movies. The app uses The Movie Database (TMDb) API to fetch movie data, caches the movies for offline use, and automatically syncs with the API every 4 hours to update the cache.

## Screenshots


## Screenshots

### Movie List App Screenshots

<p align="center">
  <img src="https://i.ibb.co/dKQGj8v/screen2.png" alt="Screen 2" width="240"/>
  <img src="https://i.ibb.co/SK7ddGf/screen3.png" alt="Screen 3" width="240"/>
  <img src="https://i.ibb.co/SXfXjtY/secreen1.png" alt="Screen 1" width="240"/>
  <img src="https://i.makeagif.com/media/11-17-2024/ZBl4-z.gif" alt="Movie List App Demo | Browse, Search, and View Movie Details (TMDb API + Offline Caching)" width="260"/>

</p>

### Features:
- **Browse Movies by Category**: Users can view movies categorized by `Now Playing`, `Popular`, `Top Rated`, and `Upcoming`.
- **Search for Movies**: Users can search for movies using keywords and get a paged list of results.
- **Movie Details**: Users can view detailed information about a movie, including its overview, release date, and poster.
- **Offline Mode**: The app caches movie data locally for offline viewing, with automatic cache invalidation every 4 hours.
- **Network Error Handling**: The app handles network errors gracefully, providing feedback to the user when there is a connectivity issue.

## Functionality
- **Fetch Categorized Lists**: Fetch movies from the TMDb API categorized by `Popular`, `Top Rated`, `Upcoming`, and `Now Playing`.
- **Search Functionality**: Search for movies by keywords and display the results in a paginated list.
- **Cache and Sync**: Cache movies in a local database and invalidate the cache every 4 hours to sync the movie data again from the API.
  - If cached data exists, it will be displayed.
  - After 4 hours, cached data is deleted and a fresh sync with the API happens.
- **Error Handling**: Show appropriate error messages if there are issues with fetching data or network connectivity.
- **Loading State**: Display a progress indicator while data is being fetched.

## Technical Specifications
- **Programming Language**: Kotlin
- **UI Layout**: XML-based layout for the UI.
- **Architecture**: The app follows modern Android architecture using MVVM (Model-View-ViewModel).
- **Networking**: Retrofit library is used for fetching data from the TMDb API.
- **Database**: Room database is used to cache movie data for offline use.
- **Error Handling**: The app displays error messages for network failures and other issues.
- **Caching Strategy**: Caches data locally and invalidates the cache every 4 hours to ensure up-to-date movie data.

## API Integration
- **TMDb API Documentation**: The app interacts with the TMDb API to fetch movie data.
  - **Now Playing**: Fetch movies currently playing in theaters.
  - **Popular**: Fetch the most popular movies.
  - **Top Rated**: Fetch the highest-rated movies.
  - **Upcoming**: Fetch upcoming movies.
  - **Search by Keywords**: Use the `discover/movie` endpoint to search movies by keywords.
  - **Movie Details**: Fetch detailed information for each movie using the `movie/{movie_id}` endpoint.
  - **Movie Images**: Fetch movie poster and backdrop images.

## Features Breakdown:
### 1. **Movie Categories**
- Display a list of movies based on categories: `Now Playing`, `Popular`, `Top Rated`, `Upcoming`.
- The app allows users to switch between categories using a `TabLayout`.

### 2. **Search Movies**
- Users can search for movies by typing keywords into a search bar. The results are displayed in a paginated list.

### 3. **Movie Details**
- Clicking on a movie item opens a detailed page showing information like the movie's title, overview, vote average, release date, and poster.

### 4. **Offline Support**
- Movies are cached in a Room database, and the app ensures that data is available offline.
- The cached movie data is invalidated every 4 hours and synced again from the TMDb API.

### 5. **Loading State and Error Handling**
- When the app is fetching data, a loading indicator is shown.
- If there is a network error, an appropriate error message is displayed.

## Database
- The app uses **Room Database** to store movies for offline viewing.
- The cache is invalidated automatically every 4 hours to ensure that the data is up-to-date.

## Libraries Used
- **Retrofit**: Used for making API requests to the TMDb API.
- **Glide**: Used for loading images like movie posters.
- **Room Database**: Used for caching movie data locally.
- **ViewModel and LiveData**: Used for managing UI-related data in a lifecycle-conscious way.

## Installation

### Requirements:
- Android Studio
- Kotlin 1.5 or higher

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/movie-list-app.git
   ```

2. Open the project in Android Studio.

3. Add your **TMDb API Key**:
   - Go to the `strings.xml` file and add your TMDb API key:
   ```xml
   <string name="tmdb_api_key">YOUR_API_KEY</string>
   ```

4. Sync Gradle to ensure dependencies are installed.

5. Run the app on your emulator or device.



## Contributing
Feel free to fork this repository and submit pull requests for improvements, bug fixes, or new features. Please ensure that your code follows the existing coding style and includes tests for any new functionality.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
