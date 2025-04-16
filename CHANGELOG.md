# Changelog

All notable changes to this project will be documented in this file.

---

## [0.1.9] - 2025-04-17

### Added

- Implemented `If-Modified-Since` support for optimized data fetching:
  - Backend now checks the `If-Modified-Since` HTTP header in both `GET /availability/latest` and `GET /weather/latest`.
  - Controllers return `304 Not Modified` when data hasn't changed since the last known update.
  - `App.jsx` tracks `lastModified` timestamps and includes them in future polling requests.
- Added `fetchAvailability` and `fetchWeather` helper functions in `api.js` that support conditional headers.
- Added console logs to `App.jsx` to visualize polling and update behavior during development.

### Removed

- Deprecated `GET /availability/latest-timestamp` and its corresponding frontend logic (no longer needed due to `If-Modified-Since` optimization).

### Notes

- Confirmed that both weather and availability data update dynamically only when changes occur.
- Merging changes from `dev` into `main` to mark completion of full backend-frontend integration for dynamic data updates.

---

## [0.1.8] - 2025-04-14

### Added

- Created `fetchWeather` helper function in `api.js` to retrieve latest weather data from `GET /weather/latest`.
- Created `WeatherBar` component to display weather details as a horizontal bar above the map.
- Added basic styling for `.weather-bar` and `.weather-item` in `index.css` for layout consistency.
- Moved polling logic for availability and weather data into `App.jsx` for centralized control and cleaner component responsibilities:
  1. App now fetches and updates availability and weather whenever a new timestamp is detected.
  2. `MapWrapper` now receives `availability` via props.
  3. `WeatherBar` now receives `weather` via props.

### Updated

- Removed internal polling logic from `MapWrapper.jsx`.

### Notes

- Verified that availability and weather data update together in sync with scraper activity.
- Console logs and observed value changes confirm accurate, real-time updates.
- Layout is now visually structured with the weather bar anchored above the map.

---

## [0.1.7] - 2025-04-10

### Added

- Created backend endpoint `GET /availability/latest-timestamp` to return the most recent availability scrape timestamp.
- Added `fetchAvailability` and `fetchLatestTimestamp` helper functions to `api.js` for modular frontend API calls.
- Updated `MapWrapper` component to implement polling logic:
  1. Checks for updates every 60 seconds using the `/availability/latest-timestamp` endpoint.
  2. Refreshes availability data only when a new timestamp is detected.
  3. Ensures map markers remain up to date without redundant API calls.

### Notes

- Verified polling logic through console logs and backend Hibernate queries.
- Confirmed that re-fetching only occurs after new scraper data is saved to the database.
- Marker data remains in sync with the backend without requiring a full page reload.
- Next steps: create visualization for weather and integrate weather data.

---

## [0.1.6] - 2025-04-09

### Added

- Implemented Google Map integration using React Leaflet (`react-leaflet`) and `leaflet` libraries.
- Created `MapWrapper` component to render Dublin city map with bike station markers.
- Connected `MapWrapper` to backend endpoint `GET /stations` to fetch live station data.
- Configured Vite dev server proxy to route API calls to `localhost:8080`.

### Notes

- Verified that all station markers appear correctly and display station name and ID in popups.
- First major integration between Spring Boot backend and React frontend is now functional.
- Next steps: integrate `GET /availability/latest` to display real-time bike and stand availability per station, and conditionally style map markers based on availability data.

---

## [0.1.5] - 2025-04-08

### Added

- Created initial React frontend using Vite with the `react` template.
- Folder `dublin-bikes-frontend` added at the root of the project.
- Installed all default dependencies and verified local dev server on `http://localhost:5173/`.

### Updated

- Renamed `backend/` to `dublin-bikes-backend/` for naming consistency.
- Renamed `database/` to `dublin-bikes-database/`.
- Deleted unused `backend/target` folder left over after renaming.
- Cleaned up and confirmed that Spring Boot backend still compiles and runs normally under new folder structure.

### Notes

- Confirmed that both `DublinBikesScraper` and `DublinWeatherScraper` continue to run successfully using `mvnw.cmd spring-boot:run`.
- Daily workflow now includes: starting Docker Desktop, running the backend, and running the frontend.
- Next step: build static layout for the React frontend to match the original app shell.

---

## [0.1.4] - 2025-04-04

### Added

- Completed and debugged `DublinWeatherScraper` to fetch and save live weather data using the OpenWeatherMap API.
- Implemented `@Scheduled` annotation to run the weather scraper every 5 minutes.
- Added custom JPA query and new endpoint `GET /weather/latest` to retrieve the most recent weather data.
- Added custom JPA query and endpoint `GET /availability/latest` to retrieve the latest availability snapshot per station.
- Confirmed correct integration of composite keys (`WeatherId`, `AvailabilityId`) across scraping and endpoint layers.
- Successfully tested all endpoints via browser.

### Fixed

- Resolved multiple casting issues in the weather scraper, including numeric type mismatches (e.g. string vs integer).
- Ensured weather scraper handles optional and missing fields gracefully.

### Notes

- Backend is now fully functional and API-complete.
- Next stage: React frontend.

---

## [0.1.3] - 2025-04-03

### Added

- Created `DublinBikesScraper` service to fetch station and availability data from the JCDecaux API.
- Implemented scheduled task using `@Scheduled` to run the `DublinBikesScraper` every 5 minutes.
- Triggered the scraper for the first time, resulting in real-time station and availability data being successfully inserted into the PostgreSQL database.
- Verified scraper functionality through logs and SQL queries.
- Began work on `DublinWeatherScraper` to fetch live weather data from the `OpenWeatherMap API`.

### Updated

- Updated `.gitignore` to exclude `application.properties` and added environment variable setup for API keys to prevent accidental commits.

### Notes

- The `DublinBikesScraper` is fully operational and stores both static and dynamic data reliably.
- The `DublinWeatherScraper` is not yet functional. Further debugging or refactoring is needed.
- PostgreSQL database must be running for scrapers to function properly. The scheduled tasks will silently fail if the database or Spring Boot app is not active.

---

## [0.1.2] - 2025-04-01

### Added

- Created `StationRepository` interface extending `JpaRepository`.
- Created `StationController` with `GET /stations` and `POST /stations` endpoints.
- Verified database connection and JSON response using browser and Postman.
- Confirmed JPA-Hibernate table creation with clean terminal logs.
- Created `Availability` entity and `AvailabilityId` composite key class.
- Created `AvailabilityRepository` and `AvailabilityController` with `GET /availability` and `POST /availability` endpoints.
- Created `Weather` entity and `WeatherId` composite key class.
- Created `WeatherRepository` and `WeatherController` with `GET /weather` and `POST /weather` endpoints.

### Updated

- Renamed main application class to `DublinBikesApp`.
- Moved `DublinBikesApp.java` to correct package (`com.dublinbikes`) to simplify structure.
- Cleaned up project folders to follow standard Spring Boot conventions.
- Changed `spring.jpa.hibernate.ddl-auto` from `create` to `update` to persist data across restarts.

### Notes

- Endpoint `GET /stations` now reflects added data and is ready for scraping integration.
- PostgreSQL container must be running for Spring Boot to connect successfully.
- All three entity types (Station, Availability, Weather) have been tested for full insert-retrieve flow.
- Added dummy station for local dev testing to enable Availability insertions. Test station removed from database after successful testing.

---

## [0.1.1] - 2025-03-31

### Added

- Initialized Spring Boot backend using Spring Initializr.
- Included dependencies: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, `Lombok`, and `DevTools`.
- Added `.gitignore` files to the root and backend directories to ignore environment files, IDE metadata, Maven output, and Docker volumes.
- Created initial `Station` entity with JPA annotations.

### Configured

- Docker Compose setup for PostgreSQL with persistent volume and `.env` file for credentials.
- Spring Boot `application.properties` for PostgreSQL connection.
- `Lombok` integration for reducing boilerplate in backend entities.

### Notes

- Docker container must be restarted manually with `docker-compose up -d` after reboot.

---

## [0.1.0] - 2025-03-29

### Added

- Created `Dev` branch for active development.
- Cleaned repository to prepare for full-stack rebuild.
- Wrote new README to reflect updated project goals.
- Planned initial tech stack and development roadmap.
